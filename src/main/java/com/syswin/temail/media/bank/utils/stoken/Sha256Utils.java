package com.syswin.temail.media.bank.utils.stoken;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Utils {

	private static final String algorithm = "sha-256";

	private static byte[] sha256(InputStream inputStream, int tempSize, long pos, long len)
			throws NoSuchAlgorithmException, IOException {
		return Base64MessageDigestUtils.digest(algorithm, inputStream, tempSize, pos, len);

	}

	public static String sha256ReStr(InputStream inputStream, int tempSize, long pos, long len)
			throws NoSuchAlgorithmException, IOException {

		if (len <= Integer.MAX_VALUE)
			return Base64MessageDigestUtils.digestReStr(algorithm, inputStream, tempSize, pos, len).replace('/', '-')
					.substring(0, 43);
		else
			return Base64MessageDigestUtils.digestBigFileReStr(algorithm, inputStream, tempSize, pos, len)
					.replace('/', '-').substring(0, 43);

		// return
		// Base64CompressEncodeing.base64From16(Base64MessageDigestUtils.digestReStr(algorithm,inputStream,tempSize,pos,len));
		//
	}

	public static String sha256ReStrBigFile(InputStream inputStream, int tempSize, long pos, long len)
			throws NoSuchAlgorithmException, IOException {
		return Base64MessageDigestUtils.digestBigFileReStr(algorithm, inputStream, tempSize, pos, len).replace('/', '-')
				.substring(0, 43);
	}

	private static byte[] sha256(byte[] data) throws NoSuchAlgorithmException {
		return Base64MessageDigestUtils.digest(algorithm, data);
	}

	public static String sha256ReStr(byte[] data) throws NoSuchAlgorithmException {
		return Base64MessageDigestUtils.digestReStr(algorithm, data).replace('/', '-').substring(0, 43);

		// return
		// Base64CompressEncodeing.base64From16(Base64MessageDigestUtils.digestReStr(algorithm,data));

	}

	private static byte[] sha256(byte[] b, int off, int len) throws NoSuchAlgorithmException {
		return Base64MessageDigestUtils.digest(algorithm, b, off, len);
	}

	public static String sha256ReStr(byte[] b, int off, int len) throws NoSuchAlgorithmException {
		return Base64MessageDigestUtils.digestReStr(algorithm, b, off, len).replace('/', '-').substring(0, 43);

		// return
		// Base64CompressEncodeing.base64From16(Base64MessageDigestUtils.digestReStr(algorithm,
		// b, off, len));
	}

	public static String sha256ReStr(File file) throws NoSuchAlgorithmException, IOException {
		return Base64Util.encode(hashV2(file)).replace('/', '-').substring(0, 43);
	}

	public static byte[] hashV2(File file) throws IOException, NoSuchAlgorithmException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int sizeRead = -1;
		long offset = 0;
		long size = file.length();
		while ((sizeRead = in.read(buffer)) != -1) {
			if (size-offset<bufferSize) {
				sizeRead = new Long(size - offset).intValue();
			}
			digest.update(buffer, 0, sizeRead);
			offset+=sizeRead;
		}
		in.close();

		byte[] hash = null;
		hash = new byte[digest.getDigestLength()];
		hash = digest.digest();
		return hash;
	}

}
