package com.syswin.temail.media.bank.utils.images;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.summercool.gif.AnimatedGifEncoder;
import org.summercool.gif.GifDecoder;
import org.summercool.gif.Scalr;
import org.summercool.gif.Scalr.Mode;

public class GifImage {
	private static final Logger logger = LoggerFactory.getLogger("async-info");

	public static byte[] scale(byte[] src, int width, int height) {
		ByteArrayInputStream bai= null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream output= new ByteArrayOutputStream();
		try {
			bai = new ByteArrayInputStream(src);
			bis=new BufferedInputStream(bai);
			GifDecoder gd = new GifDecoder();
			//int status = gd.read(new FileInputStream(new File("D:/adx.gif")));
			int status = gd.read(bis);
//			if (status != GifDecoder.STATUS_OK) {
//				logger.error("GifImage->status != GifDecoder.STATUS_OK");
//				return null;
//			}
			if (status == GifDecoder.STATUS_OPEN_ERROR) {
				logger.error("GifImage->status != GifDecoder.STATUS_OPEN_ERROR");
				return null;
			}
			AnimatedGifEncoder ge = new AnimatedGifEncoder();
			ge.start(output);
			ge.setRepeat(0);

			for (int i = 0; i < gd.getFrameCount(); i++) {
				BufferedImage frame = gd.getFrame(i);
				width = (int) (width * 1);
				height = (int) (height * 1);
				BufferedImage rescaled = Scalr.resize(frame, Mode.AUTOMATIC, width, height);
				int delay = gd.getDelay(i);
				ge.setDelay(delay);
				ge.addFrame(rescaled);
			}
			ge.finish();
			byte[] arr = output.toByteArray();
			return arr;
		} catch (Exception e) {
			logger.debug("GifImage->scale", e);
		}finally {
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(bai);
			IOUtils.closeQuietly(output);
		}

		return null;
	}
	
	public static byte[] cut(byte[] src) {
		ByteArrayInputStream bai= null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			bai = new ByteArrayInputStream(src);
			bis=new BufferedInputStream(bai);
			GifDecoder gd = new GifDecoder();
			int status = gd.read(bis);
			if (status == GifDecoder.STATUS_OPEN_ERROR) {
				logger.error("GifImage->status != GifDecoder.STATUS_OPEN_ERROR");
				return null;
			}
//			if (status != GifDecoder.STATUS_OK) {
//				logger.error("GifImage->status != GifDecoder.STATUS_OK");
//				return null;
//			}
			if(gd.getFrameCount()==0){
				logger.warn("GifImage->getFrameCount() is 0");
				return null;
			}
			BufferedImage frame = gd.getFrame(0);
			if(!ImageIO.write(frame, "gif", baos)){
				logger.warn("GifImage->ImageIO.write rescaled return false");
				return null;
			}
			byte[] arr = baos.toByteArray();
			return arr;
		} catch (Exception e) {
			logger.debug("GifImage->cut", e);
		}finally {
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(bai);
			IOUtils.closeQuietly(baos);
		}

		return null;
	}
	
	
}
