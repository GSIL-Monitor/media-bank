package com.syswin.temail.media.bank.utils.stoken;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;


public class Base64SafeUrlUtils {
	
	
	public static String safeUrlEncode(byte[] b) throws UnsupportedEncodingException {
		return Base64.encodeBase64URLSafeString(b);
	}
	
	
	public static String safeUrlEncode(String s) throws UnsupportedEncodingException {
		return Base64.encodeBase64URLSafeString(s.getBytes());
	}
	
	
	public static String safeUrlDecode(String s) throws UnsupportedEncodingException {
		
		return 	new String(Base64.decodeBase64(s),"utf-8");
	}
	
	public static String safeUrlDecode(byte[] b) throws UnsupportedEncodingException {
		return 	new String(Base64.decodeBase64(b),"utf-8");
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String s="a:,$$/nb人才盘点今天下班前给我哈dsfc+-/==";
		String s1=Base64SafeUrlUtils.safeUrlEncode(s);
		
		System.out.println(s1);
		String s2 = Base64SafeUrlUtils.safeUrlDecode(s1);
		System.out.println(s2);

	}
}
