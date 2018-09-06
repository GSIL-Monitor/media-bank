package com.syswin.temail.media.bank.utils.stoken;

public class SecurityTokenCheckResult {
	 private boolean pass;
	 private int  code;
	 private String   msg;
	 
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	 
	}