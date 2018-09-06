package com.syswin.temail.media.bank.constants;

public class ResponseCodeConstants {

	/**
	 * 
	 * 成功
	 */
	public static final int SUCCESS = 200;

	/**
	 * 
	 * 传入参数错误
	 */
	public static final int PARAM_ERROR = 400;

	/**
	 * 
	 * 无权限访问服务器
	 */
	public static final int AUTO_ERROR = 401;

	/**
	 * 
	 * 禁止用户访问服务器
	 */
	public static final int FORBID_ACCESS_ERROR = 403;

	/**
	 * 
	 * 访问资源不存在
	 */
	public static final int NOT_FOUND_ERROR = 404;

	/**
	 * 
	 * 服务器异常
	 */
	public static final int SERVER_ERROR = 500;

	/**
	 * 
	 * 服务器繁忙
	 */
	public static final int UNAVAILABLE_SERVER = 503;

}