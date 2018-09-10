package com.syswin.temail.media.bank.utils.stoken;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class StokenHelper {

	public static String getStoken(int appId, String appSecret)
			throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException {
		SecurityToken securityToken = new SecurityToken();
		String auth = "w";
		String stoid = "";
		long ts = System.currentTimeMillis();
		int independence = 0;
		String version = "1";
		String product = "oss";
		String company = "syswin";
		securityToken.initObjectSecurityToken(company, product, version,
				independence, appId, ts, auth, stoid, appSecret);
		String stoken = securityToken.getSecurityToken();
		return stoken;
	}

	public static String defaultStoken()
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		return getStoken(1001, "123456");
	}

}