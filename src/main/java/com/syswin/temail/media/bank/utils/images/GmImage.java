package com.syswin.temail.media.bank.utils.images;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.im4java.process.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GmImage {
	private static final Logger logger = LoggerFactory.getLogger("async-info");
	/** * ImageMagick的路径 */
	public static String imageMagickPath = null;

	static {
		/** 获取ImageMagick的路径 */
		// linux下不要设置此值，不然会报错
		imageMagickPath = System.getProperty("imageMagicPath");
		if (StringUtils.isBlank(imageMagickPath)) {
//			imageMagickPath = "D:/GraphicsMagick-1.3.25-Q8";
		}
	}

	private static ConvertCmd getConvertCmd() {
		ConvertCmd convert = new ConvertCmd(true);
		// linux下不要设置此值，不然会报错
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf("win") != -1) {
			// linux下不要设置此值，不然会报错
			convert.setSearchPath(imageMagickPath);
		}
		return convert;
	}

	public static CompositeCmd getCompositeCmd() {
		CompositeCmd compositeCmd = new CompositeCmd(true);
		// linux下不要设置此值，不然会报错
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.indexOf("win") != -1) {
			// linux下不要设置此值，不然会报错
			compositeCmd.setSearchPath(imageMagickPath);
		}
		return compositeCmd;
	}

	/**
	 * 裁切图
	 * 
	 * @param in
	 *            输入直接数组
	 * @param format
	 *            文件类型
	 * @param x1
	 *            终点横坐标
	 * @param y1
	 *            终点纵坐标
	 * @param x
	 *            起点横坐标
	 * @param y
	 *            起点纵坐标
	 * @return 切好图的字节数组
	 */
	public static byte[] cropImage(byte[] in, String format, int x, int y, int x1, int y1, int quality) {
		ByteArrayOutputStream outStream = null;
		ByteArrayInputStream inStream = null;
		try {
			inStream = new ByteArrayInputStream(in);
			int width = x1 - x;
			int height = y1 - y;
			IMOperation op = new IMOperation();
			op.addImage("-"); // read from stdin
			op.addRawArgs("-auto-orient");
			op.addRawArgs("+profile", "*");
			op.crop(width, height, x, y); // 对图片进行操作
			op.addRawArgs("-quality", quality + "");
			op.addImage(format.toLowerCase() + ":-"); // 类型
			Pipe pipeIn = new Pipe(inStream, null);
			outStream = new ByteArrayOutputStream();
			Pipe pipeOut = new Pipe(null, outStream);

			ConvertCmd convertCmd = getConvertCmd();
			convertCmd.setInputProvider(pipeIn);
			convertCmd.setOutputConsumer(pipeOut);
			convertCmd.run(op);
			byte[] ret = outStream.toByteArray();
			return ret;
		} catch (Exception e) {
			logger.error("cropImage", e);
		} finally {
			IOUtils.closeQuietly(outStream);
			IOUtils.closeQuietly(inStream);
		}
		return null;
	}

	public static byte[] scaleImage(ByteArrayInputStream inStream, String type, Integer width, Integer height, Integer quality, boolean isCrop) {
		ByteArrayOutputStream outStream = null;
		try {
			IMOperation op = new IMOperation();
			op.addImage("-"); // read from stdin
			op.addRawArgs("-auto-orient");
			op.addRawArgs("+profile", "*");
			if (isCrop) {
				op.thumbnail(width, height, '^');
				op.gravity("center");
				op.crop(width, height, 0, 0);
			} else {
				op.thumbnail(width, height, '>');
			}
			op.addRawArgs("-quality", quality + "");
			op.addImage(type.toLowerCase() + ":-");
			ConvertCmd convert = getConvertCmd();
			Pipe pipeIn = new Pipe(inStream, null);
			outStream = new ByteArrayOutputStream();
			Pipe pipeOut = new Pipe(null, outStream);
			convert.setInputProvider(pipeIn);
			convert.setOutputConsumer(pipeOut);
			convert.run(op);
			byte[] result = outStream.toByteArray();
			return result;
		} catch (Exception e) {
			logger.error("scaleImage", e);
		} finally {
			IOUtils.closeQuietly(outStream);
		}
		return null;
	}
	
	public static byte[] scaleGif(ByteArrayInputStream inStream, String type, Integer width, Integer height, Integer quality) {
		ByteArrayOutputStream outStream = null;
		try {
			IMOperation op = new IMOperation();
			op.coalesce();
			op.addImage("-"); // read from stdin
			op.addRawArgs("-auto-orient");
			op.addRawArgs("+profile", "*");
			//op.addRawArgs("-coalesce","");
			op.resize(width, height, ">");
			op.addRawArgs("-quality", quality + "");
			op.addImage(type.toLowerCase() + ":-");
			ConvertCmd convert = getConvertCmd();
			Pipe pipeIn = new Pipe(inStream, null);
			outStream = new ByteArrayOutputStream();
			Pipe pipeOut = new Pipe(null, outStream);
			convert.setInputProvider(pipeIn);
			convert.setOutputConsumer(pipeOut);
			convert.run(op);
			byte[] result = outStream.toByteArray();
			return result;
		} catch (Exception e) {
			logger.error("scaleGif", e);
		} finally {
			IOUtils.closeQuietly(outStream);
		}
		return null;
	}

	public static byte[] convertImage(ByteArrayInputStream inStream, String format) {
		ByteArrayOutputStream outStream = null;
		try {
			IMOperation op = new IMOperation();
			op.addImage("-"); // read from stdin
			op.addRawArgs("-auto-orient");
			op.addRawArgs("+profile", "*");
			op.addImage(format.toLowerCase() + ":-");
			ConvertCmd convert = getConvertCmd();
			Pipe pipeIn = new Pipe(inStream, null);
			outStream = new ByteArrayOutputStream();
			Pipe pipeOut = new Pipe(null, outStream);
			convert.setInputProvider(pipeIn);
			convert.setOutputConsumer(pipeOut);
			convert.run(op);
			byte[] result = outStream.toByteArray();
			return result;
		} catch (Exception e) {
			logger.error("convertImage", e);
		} finally {
			IOUtils.closeQuietly(outStream);
		}
		return null;
	}

	/**
	 * 按尺寸裁剪
	 * 
	 * @param inStream
	 * @param type
	 * @param width
	 * @param height
	 * @param quality
	 * @return
	 */
	public static byte[] resizeImage(InputStream inStream, String type, Integer width, Integer height, Integer quality) {
		ByteArrayOutputStream outStream = null;
		try {
			IMOperation op = new IMOperation();
			op.addImage("-"); // read from stdin

			op.addRawArgs("-auto-orient");
			op.addRawArgs("+profile", "*");
			op.resize(width, height, "!");
			op.addRawArgs("-quality", quality + "");
			op.addImage(type.toLowerCase() + ":-");
			ConvertCmd convert = new ConvertCmd(true);
			/*
			 * String osName = System.getProperty("os.name").toLowerCase(); if (osName.indexOf("win") != -1) { // linux下不要设置此值，不然会报错 convert.setSearchPath(imageMagickPath); }
			 */
			Pipe pipeIn = new Pipe(inStream, null);
			outStream = new ByteArrayOutputStream();
			Pipe pipeOut = new Pipe(null, outStream);
			convert.setInputProvider(pipeIn);
			convert.setOutputConsumer(pipeOut);
			convert.run(op);
			byte[] result = outStream.toByteArray();
			return result;
		} catch (Exception e) {
			logger.error("scaleImage", e);
		} finally {
			IOUtils.closeQuietly(outStream);
		}
		return null;
	}

	/**
	 * 读取图片信息
	 * @param path
	 * @return
	 */
	public static String showImageInfo(String path){
           
		IdentifyCmd identifyCmd = new IdentifyCmd(true);
		ArrayListOutputConsumer output = new ArrayListOutputConsumer();
		identifyCmd.setOutputConsumer(output);
		IMOperation op = new IMOperation();
		op.addImage(1);
        try {
        	if(System.getProperty("os.name").toLowerCase().indexOf("windows") != -1){
                 op.format("{\"\"format\"\":\"\"%m\"\",\"\"width\"\": \"\"%W\"\",\"\"height\"\": \"\"%h\"\",\"\"frameNumber\"\": \"\"%p\"\",\"\"colorspace\"\":\"\"%[JPEG-Colorspace-Name]\"\"},");  
        		identifyCmd.setGlobalSearchPath("D:/GraphicsMagick-1.3.25-Q8");
        	}else{
        		op.format("{\"format\":\"%m\",\"width\": \"%W\",\"height\": \"%h\",\"frameNumber\": \"%p\",\"colorspace\":\"%[JPEG-Colorspace-Name]\"},");	
        	}
        	//System.out.println("==ImageMagic==/n" + "Op=" + op.toString() + ";Cmd=" + identifyCmd.toString());  
			identifyCmd.run(op, path);
		} catch (Exception e) {
			e.printStackTrace();
		}  
        ArrayList<String> cmdOutput = output.getOutput();
        if(cmdOutput != null && cmdOutput.size() == 1){
    		String line = cmdOutput.get(0);
    		return line.substring(0,line.lastIndexOf(","));
    	}
        return null;
	}

}