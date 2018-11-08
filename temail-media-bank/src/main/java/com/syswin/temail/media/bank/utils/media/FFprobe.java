package com.syswin.temail.media.bank.utils.media;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class FFprobe {
	private static String FFPROBE = "ffprobe";
	private static boolean isWindows=false;
	static {
		final String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf("win") != -1) {
			isWindows = true;
		}
		if (isWindows) {
			FFPROBE = "D:\\ffmpeg\\bin\\ffprobe.exe";
		}
	}
	public static String process(List<String> command) throws Exception {
		
		StringBuffer  buffer = new StringBuffer();
		buffer.append(FFPROBE);
		buffer.append(" ");
		for(String item:command){
			buffer.append(item);
			buffer.append(" ");
		}
		
		final String osName = System.getProperty("os.name").toLowerCase();
		File file = null ;
		if (osName.indexOf("win") != -1) {
			file = new File("D:/ffmpeg");
		}
		
		Process p = Runtime
				.getRuntime()
				.exec(buffer.toString(), null, file);
		
		InputStream is = null;
		try {
			is = p.getInputStream();
			String out = IOUtils.toString(is);
			return out;
		} catch (Exception e) {
			throw new Exception("process error, check your param:" + e.getMessage());
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}