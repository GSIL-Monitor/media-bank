package com.syswin.temail.media.bank.utils.stoken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 136249 on 2015/5/6.
 */
public class Base64MessageDigestUtils {
	 public static final int UPLOAD_BLOCK_SIZE = 131072;
	public static byte[] digest(String algorithm, InputStream inputStream, int tempSize, long pos, long len)
			throws NoSuchAlgorithmException, IOException {
		if (tempSize <= 0) {
			tempSize = 1024;
		}
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		DigestInputStream dis = new DigestInputStream(inputStream, messageDigest);

		if (len < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (pos > 0) {
			long skip = inputStream.skip(pos);
			if (skip < pos) {
				throw new RuntimeException("skip to pos error!,pos:" + pos + ",skiped:" + skip);
			}
		}

		byte[] tmp = new byte[tempSize];
		int n = 0;
		while (n < len) {
			int readSize = tempSize;
			if (readSize > (len - n)) {
				readSize = (int) (len - n);
			}
			int count = dis.read(tmp, 0, readSize);
			if (count < 0)
				throw new EOFException();
			n += count;
		}

		return messageDigest.digest();
	}

//	public static byte[] digestBigFile(String algorithm, InputStream inputStream, int tempSize, long pos, long len)
//			throws NoSuchAlgorithmException, IOException {
//		if (tempSize <= 0) {
//			tempSize = 1024;
//		}
//		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
//		DigestInputStream dis = new DigestInputStream(inputStream, messageDigest);
//
//		if (len < 0) {
//			throw new IndexOutOfBoundsException();
//		}
//		if (pos > 0) {
//			long skip = inputStream.skip(pos);
//			if (skip < pos) {
//				throw new RuntimeException("skip to pos error!,pos:" + pos + ",skiped:" + skip);
//			}
//		}
//
//		byte[] tmp = new byte[tempSize];
//		int n = 0;
//		while (n < len) {
//			int readSize = tempSize;
//			if (readSize > (len - n)) {
//				readSize = (int) (len - n);
//			}
//			int count = dis.read(tmp, 0, readSize);
//			if (count < 0)
//				throw new EOFException();
//			n += count;
//		}
//
//		return messageDigest.digest();
//	}

	public static String digestReStr(String algorithm, InputStream inputStream, int tempSize, long pos, long len)
			throws NoSuchAlgorithmException, IOException {
		byte[] digestBytes = digest(algorithm, inputStream, tempSize, pos, len);
		return Base64Util.encode(digestBytes);
	}

	public static byte[] digest(String algorithm, byte[] data) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		return messageDigest.digest(data);
	}

	public static String digestReStr(String algorithm, byte[] data) throws NoSuchAlgorithmException {
		byte[] digestBytes = digest(algorithm, data);
		return Base64Util.encode(digestBytes);
	}

	public static byte[] digest(String algorithm, byte[] b, int off, int len) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(b, off, len);
		return md.digest();
	}

	public static String digestReStr(String algorithm, byte[] b, int off, int len) throws NoSuchAlgorithmException {
		return Base64Util.encode(digest(algorithm, b, off, len));
	}

	public static String digestBigFileReStr(String algorithm, InputStream inputStream, int tempSize, long pos, long len)
			throws IOException, NoSuchAlgorithmException {
		if (tempSize <= 0) {
			tempSize = 1024;
		}
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

		if (len < 0) {
			throw new IndexOutOfBoundsException();
		}
		if (pos > 0) {
			long skip = inputStream.skip(pos);
			if (skip < pos) {
				throw new RuntimeException("skip to pos error!,pos:" + pos + ",skiped:" + skip);
			}
		}

		long c = 0;
		int buff_size=UPLOAD_BLOCK_SIZE;
		while (c  < len) {
			if(buff_size>len - c)
				buff_size=(int) (len - c);
			byte[] temp = new byte[buff_size];
			int size = inputStream.read(temp);
			messageDigest.update(temp,0,size);
			c += size;
			buff_size=size;
			if(size<=0) break;
		}
		
		inputStream.close();
		return Base64Util.encode(messageDigest.digest());
	}
}
