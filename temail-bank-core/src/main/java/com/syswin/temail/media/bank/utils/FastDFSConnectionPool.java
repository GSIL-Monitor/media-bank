package com.syswin.temail.media.bank.utils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * fastdfs高性能连接池
 * 
 * @author wangye
 *
 */
public class FastDFSConnectionPool {

	private static final Logger logger = LoggerFactory.getLogger(FastDFSConnectionPool.class);
	/** 空闲的连接池 */
	private static LinkedBlockingQueue<TrackerServer> idleConnectionPool = new LinkedBlockingQueue<TrackerServer>();
	/** 连接池默认最小连接数 */
	private static long minPoolSize = 30;
	/** 连接池默认最大连接数 */
	private static long maxPoolSize = 100;
	/** 当前创建的连接数 */
	private static volatile long nowPoolSize = 0;
	/** 默认等待时间（单位：秒） */
	private static long waitTimes = 200;
	/** fastdfs客户端创建连接默认1次 */
	private static final int COUNT = 1;

	public FastDFSConnectionPool() {

	}
	
	static{
		String logId = UUID.randomUUID().toString();
		logger.info("[线程池构造方法(FastDFSConnectionPool)][" + logId + "][默认参数：minPoolSize=" + minPoolSize + ",maxPoolSize="
				+ maxPoolSize + ",waitTimes=" + waitTimes + "]");
		/** 初始化连接池 */
		poolInit(logId);
		/** 注册心跳 */
		FastDFSHeartBeat beat = new FastDFSHeartBeat(idleConnectionPool);
		beat.beat();
	}

	/**
	 *
	 * @Description: 连接池初始化 (在加载当前ConnectionPool时执行) 1).加载配置文件 2).空闲连接池初始化
	 *               3).创建最小连接数的连接，并放入到空闲连接池
	 *
	 */
	private static void poolInit(String logId) {
		try {
			/** 往线程池中添加默认大小的线程 */
			for (int i = 0; i < minPoolSize; i++) {
				createTrackerServer(logId, COUNT);
			}
		} catch (Exception e) {
			logger.error("[FASTDFS初始化(init)--异常][" + logId + "][异常：{}]", e);
		}
	}

	/**
	 * 
	 * @Description: 创建TrackerServer,并放入空闲连接池
	 *
	 */
	public static void createTrackerServer(String logId, int flag) {
		logger.info("[创建TrackerServer(createTrackerServer)][" + logId + "]");
		TrackerServer trackerServer = null;
		try {
			TrackerClient trackerClient = new TrackerClient();
			trackerServer = trackerClient.getConnection();
			while (trackerServer == null && flag < 5) {
				logger.info("[创建TrackerServer(createTrackerServer)][" + logId + "][第" + flag + "次重建]");
				flag++;
				trackerServer = trackerClient.getConnection();
			}
			org.csource.fastdfs.ProtoCommon.activeTest(trackerServer.getSocket());
			idleConnectionPool.add(trackerServer);
			/** 同一时间只允许一个线程对nowPoolSize操作 **/
			increase();
		} catch (Exception e) {
			logger.error("[创建TrackerServer(createTrackerServer)][" + logId + "][异常：{}]", e);
		} finally {
			if (trackerServer != null) {
				try {
					trackerServer.close();
				} catch (Exception e) {
					logger.error("[创建TrackerServer(createTrackerServer)--关闭trackerServer异常][" + logId + "][异常：{}]", e);
				}
			}

		}
	}
	
	 public static synchronized void increase() {
		 nowPoolSize ++ ;
	 }
	 
	 public static synchronized void reduce() {
		 if (nowPoolSize != 0) {
				nowPoolSize--;
		 }
	 }

	/**
	 * 
	 * @Description: 获取空闲连接 1).在空闲池（idleConnectionPool)中弹出一个连接；
	 *               2).把该连接放入忙碌池（busyConnectionPool）中; 3).返回 connection
	 *               4).如果没有idle connection, 等待 wait_time秒, and check again
	 * 
	 *
	 */
	public static TrackerServer checkout(String logId) {
		logger.info("[获取空闲连接(checkout)][" + logId + "]");
		TrackerServer trackerServer = idleConnectionPool.poll();
		if (trackerServer == null) {
			if (nowPoolSize < maxPoolSize) {
				createTrackerServer(logId, COUNT);
				try {
					trackerServer = idleConnectionPool.poll(waitTimes, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("[获取空闲连接(checkout)-error][" + logId + "][error:获取连接超时:{}]", e);
				}
			}
			if (trackerServer == null) {
				logger.error("[获取空闲连接(checkout)-error][" + logId + "][error:获取连接超时（" + waitTimes + "s）]");
			}

		}
		logger.info("[获取空闲连接(checkout)][" + logId + "][获取空闲连接成功]");
		return trackerServer;

	}

	/**
	 * 
	 * @Description: 释放繁忙连接 1.如果空闲池的连接小于最小连接值，就把当前连接放入idleConnectionPool；
	 *               2.如果空闲池的连接等于或大于最小连接值，就把当前释放连接丢弃；
	 * 
	 * @param client1
	 *            需释放的连接对象
	 * 
	 */
	public static void checkin(TrackerServer trackerServer, String logId) {
		logger.info("[释放当前连接(checkin)][" + logId + "][prams:" + trackerServer + "] ");
		if (trackerServer != null) {
			if (idleConnectionPool.size() < minPoolSize) {
				idleConnectionPool.add(trackerServer);
			} else {
				reduce();
			}
		}

	}

	/**
	 * 
	 * @Description: 删除不可用的连接，并把当前连接数减一（调用过程中trackerServer报异常，调用一般在finally中）
	 * @param trackerServer
	 * 
	 */
	public static void drop(TrackerServer trackerServer, String logId) {
		logger.info("[删除不可用连接方法(drop)][" + logId + "][parms:" + trackerServer + "] ");
		if (trackerServer != null) {
			try {
				reduce();
				trackerServer.close();
			} catch (IOException e) {
				logger.info("[删除不可用连接方法(drop)--关闭trackerServer异常][" + logId + "][异常：{}]", e);
			}
		}
	}

	public static LinkedBlockingQueue<TrackerServer> getIdleConnectionPool() {
		return idleConnectionPool;
	}


}
