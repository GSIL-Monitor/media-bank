package com.syswin.temail.media.bank.utils.logs;

import javax.servlet.http.HttpServletRequest;

public class StorageLogDto {
	/**
	 * 服务名称
	 */
	private String serverName;

	/**
	 * action, 动作
	 */
	private String action;

	/**
	 * begin time, 开始时间
	 */
	private Long beginTime;

	/**
	 * IpAddress, IP地址
	 */
	private String ip;

	/**
	 * FileSize, 文件大小, byte为单位
	 */
	private Long fileSize;

	/**
	 * fileId, 文件ID
	 */
	private String fileId;

	/**
	 * userId
	 */
	private Integer userId;

	/**
	 * state,响应状态
	 */
	private String state;

	/**
	 * consumedTime, 操作耗时
	 */
	private long consumedTime;

	/**
	 * 业务请求识别码
	 */
	private String tMark;

	/**
	 * user-agent
	 */
	private String userAgent;

	/**
	 * localAddr
	 */
	private String localAddr;

	/**
	 * localPort
	 */
	private Integer localPort;

	/**
	 * uri
	 * */
	private String uri;

	/**
	 * queryString
	 * */
	private String queryString;


	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getConsumedTime() {
		return consumedTime;
	}

	public void setConsumedTime(long consumedTime) {
		this.consumedTime = consumedTime;
	}

	public String getTMark() {
		return tMark;
	}

	public void setTMark(String tMark) {
		this.tMark = tMark;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	public Integer getLocalPort() {
		return localPort;
	}

	public void setLocalPort(Integer localPort) {
		this.localPort = localPort;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public StorageLogDto(){

	}

	//基本存储日志初始化
	public StorageLogDto(String action, long beginTime, long fileSize, String fileId, Integer userId, String state, String tMark, HttpServletRequest request){
		this.serverName = "media-bank";
		this.action = action;
		this.beginTime = beginTime;
		this.ip = getIpAddr(request);
		this.fileSize = fileSize;
		this.fileId = fileId;
		this.userId = userId;
		this.state = state;
		this.consumedTime = System.currentTimeMillis() - beginTime;
		this.tMark = tMark;
		this.userAgent = request.getHeader("user-agent");
		this.localAddr = request.getLocalAddr();
		this.localPort = request.getLocalPort();
		this.uri = request.getRequestURI();
		this.queryString = request.getQueryString();
	}

	//增值服务日志初始化
	public StorageLogDto(String action, long beginTime, String state, String tMark, HttpServletRequest request){
		this.serverName = "media-bank";
		this.action = action;
		this.beginTime = beginTime;
		this.ip = getIpAddr(request);
		this.state = state;
		this.consumedTime = System.currentTimeMillis() - beginTime;
		this.tMark = tMark;
		this.userAgent = request.getHeader("user-agent");
		this.localAddr = request.getLocalAddr();
		this.localPort = request.getLocalPort();
		this.uri = request.getRequestURI();
		this.queryString = request.getQueryString();
	}

	private static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}