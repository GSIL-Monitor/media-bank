package com.syswin.temail.media.bank.utils.stoken;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author admin
 * 云存储安全网关
 */
public class AES256Utils {

	/**
	 * 加密数据
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		KeyGenerator kgen = KeyGenerator.getInstance("AES");// 实例化AES密钥生成器
		kgen.init(256, new SecureRandom(key));// 根据密钥明文初始化256位密钥生成器
		SecretKey secretKey = kgen.generateKey();// 生成密钥
		byte[] enCodeFormat = secretKey.getEncoded();// 获取密钥字节信息RAW
		SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");// 创建密钥空间

		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return cipher.doFinal(src);
	}

	/**
	 * 加密数据
	 */
	public static byte[] encrypt(byte[] src, String key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		KeyGenerator kgen = KeyGenerator.getInstance("AES");// 实例化AES密钥生成器
		kgen.init(256, new SecureRandom(key.getBytes()));// 根据密钥明文初始化256位密钥生成器
		SecretKey secretKey = kgen.generateKey();// 生成密钥
		byte[] enCodeFormat = secretKey.getEncoded();// 获取密钥字节信息RAW
		SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");// 创建密钥空间

		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return cipher.doFinal(src);
	}

	/**
	 * 解密数据
	 */
	public static byte[] decrypt(byte[] src, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		KeyGenerator kgen = KeyGenerator.getInstance("AES");// 实例化AES密钥生成器
		// 实例化AES密钥生成器
		kgen.init(256, new SecureRandom(key.getBytes()));// 根据密钥明文初始化256位密钥生成器
		SecretKey secretKey = kgen.generateKey();// 生成密钥
		byte[] enCodeFormat = secretKey.getEncoded();// 获取密钥字节信息RAW
		SecretKeySpec key2 = new SecretKeySpec(enCodeFormat, "AES");// 创建密钥空间

		cipher.init(Cipher.DECRYPT_MODE, key2);
		return cipher.doFinal(src);
	}

	/**
	 * 解密数据
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		KeyGenerator kgen = KeyGenerator.getInstance("AES");// 实例化AES密钥生成器
		// 实例化AES密钥生成器
		kgen.init(256, new SecureRandom(key));// 根据密钥明文初始化256位密钥生成器
		SecretKey secretKey = kgen.generateKey();// 生成密钥
		byte[] enCodeFormat = secretKey.getEncoded();// 获取密钥字节信息RAW
		SecretKeySpec key2 = new SecretKeySpec(enCodeFormat, "AES");// 创建密钥空间

		cipher.init(Cipher.DECRYPT_MODE, key2);
		return cipher.doFinal(src);
	}
	
	
	 /**
	 * 加密文件数据用（NoPadding可以保持对称性）
	 *
	 * @throws Exception
	 */
	 public static byte[] encryptNoPaddingData(byte[] src, String key) throws
	 Exception {
	 Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

	 KeyGenerator kgen = KeyGenerator.getInstance("AES");//实例化AES密钥生成器
	 kgen.init(256, new SecureRandom(key.getBytes()));//根据密钥明文初始化256位密钥生成器
	 SecretKey secretKey = kgen.generateKey();//生成密钥
	 byte[] enCodeFormat = secretKey.getEncoded();//获取密钥字节信息RAW
	 SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");//创建密钥空间
	
	 int remainder = src.length % 16;
	 int paddingLength = (remainder == 0) ? 0 : 16 - remainder;
	 if(paddingLength>0) {
	 src=ByteBuffer.allocate(src.length + paddingLength).put(src).array();
	 }
	 cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
	 return cipher.doFinal(src);
	 }
	
	 /**
	 * 解密文件数据用
	 *
	 * @param src 加密后的数据
	 * @param key 解密key
	 * @param len 加密前文件长度
	 * @throws Exception
	 */
	 public static byte[] decryptNoPaddingData(byte[] src, String key, int
	 len) throws Exception {
	 Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
	 KeyGenerator kgen = KeyGenerator.getInstance("AES");//实例化AES密钥生成器
	 kgen.init(256, new SecureRandom(key.getBytes()));//根据密钥明文初始化256位密钥生成器
	 SecretKey secretKey = kgen.generateKey();//生成密钥
	 byte[] enCodeFormat = secretKey.getEncoded();//获取密钥字节信息RAW
	 SecretKeySpec key2 = new SecretKeySpec(enCodeFormat, "AES");//创建密钥空间
	
	 SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), "AES");
	
	 cipher.init(Cipher.DECRYPT_MODE, key2);
	 byte[] data= cipher.doFinal(src);
	 int remainder = len % 16;
	 int paddingLength = (remainder == 0) ? 0 : 16 - remainder;
	 if(paddingLength>0){
	 byte[] tmp = new byte[len];
	 System.arraycopy(data, 0, tmp, 0, len);
	 data=tmp;
	 }
	 return data;
	 }

	// /**
	// * 解密需要的文件数据
	// * @throws Exception
	// */
	// public static byte[] decryptNoPadding(byte[] src, String key, int off,
	// int len) throws Exception {
	// Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
	// SecretKeySpec securekey = new SecretKeySpec(getKey256(key), "AES");
	// cipher.init(Cipher.DECRYPT_MODE, securekey);
	// return cipher.doFinal(src, off, len);
	// }
	//
	//

	public static void main(String[] fwef) throws Exception {
		String key = "weewee";
		String value = "testValue2333weefdw  对对对33333333423";
		// System.out.println(new
		// String(decryptNoPaddingData(encryptNoPaddingData(value.getBytes(),key),key,value.getBytes().length)));
		System.out.println(System.currentTimeMillis());
		for(int i=0;i<10000;i++)
		new String(decrypt(encrypt((value+i).getBytes(), key), key));
		System.out.println(System.currentTimeMillis());
		System.out.println(new String(decrypt(encrypt(value.getBytes(), key), key)));

	}

}
