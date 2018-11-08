package com.syswin.temail.media.bank.utils.media;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FFmpeg {
	private static final Logger logger = LoggerFactory.getLogger("async-info");
	private static String FFMPEG = "ffmpeg";
	private static String FFMPEG_TMP_PATH = FileUtils.getTempDirectoryPath()+"/";
	private static boolean isWindows = false;

	static {
		final String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf("win") != -1) {
			isWindows = true;
		}
		if (isWindows) {
			//FFMPEG = "F:\\company\\ffmpeg\\ffmpeg.exe";
			FFMPEG = "D:\\ffmpeg\\bin\\ffmpeg.exe";
			FFMPEG_TMP_PATH = "d:\\";
		}
	}
	
	public static byte[] process(List<String> command, String tmpFileName)
			throws Exception {
		String processLogStr = "";
		String tmpPathFile = FFMPEG_TMP_PATH + tmpFileName;	
		InputStream is = null;
		
		///添加执行文件在参数最前面
		command.add(0, FFMPEG);
		//在参数最后面添加输出文件
		command.add(tmpPathFile);
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(true);
		if(isWindows){
			processBuilder.directory(new File("D:\\ffmpeg"));
		}
		try {
			Process process = processBuilder.start();
			is = process.getInputStream();
			processLogStr = IOUtils.toString(is);
			if (process.waitFor() != 0) {
				throw new Exception("process error");
			}

			byte[] out = FileUtils.readFileToByteArray(new File(tmpPathFile));
			//logger.info("process video success:{}",processLogStr);
			return out;

		} catch (Exception e) {
			logger.error("process video error:{},{}",processLogStr,e);
			throw new Exception("process error, check your param");
		} finally {
			IOUtils.closeQuietly(is);
			// 删除临时文件
			FileUtils.deleteQuietly(new File(tmpPathFile));
		}
	}
	
	public static String processFile(List<String> command, String tmpFileName)
			throws Exception {
		String processLogStr = "";
		String tmpPathFile = FFMPEG_TMP_PATH + tmpFileName;	
		InputStream is = null;
		
		///添加执行文件在参数最前面
		command.add(0, FFMPEG);
		//在参数最后面添加输出文件
		command.add(tmpPathFile);
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.redirectErrorStream(true);
		if(isWindows){
			processBuilder.directory(new File("D:\\ffmpeg"));
		}
		try {
			Process process = processBuilder.start();
			is = process.getInputStream();
			processLogStr = IOUtils.toString(is);
			if (process.waitFor() != 0) {
				throw new Exception("process error");
			}

		//	logger.info("process video success:{}",processLogStr);
			return tmpPathFile;

		} catch (Exception e) {
			logger.error("process video error:{},{}",processLogStr,e);
			throw new Exception("process error, check your param");
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}