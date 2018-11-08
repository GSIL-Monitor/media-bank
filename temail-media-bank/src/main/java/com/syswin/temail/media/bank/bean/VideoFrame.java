package com.syswin.temail.media.bank.bean;

import com.syswin.temail.media.bank.utils.media.AudioVideoType;
import com.syswin.temail.media.bank.utils.media.MediaHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class VideoFrame {
	private Integer screenshotWidth;
	private Integer screenshotHeight;
	private String screenshotImgFormat;
	private Integer screenshotOffset;// 视频截取时间offset
	private String rotate;
	private String chanageRotate;
	private double minFrames = 0.1;// 视频最小帧数
	private String suffix; //视频文件后缀
	private String filePath; //视频文件地址

	public Integer getScreenshotWidth() {
		return screenshotWidth;
	}

	public void setScreenshotWidth(Integer screenshotWidth) {
		this.screenshotWidth = screenshotWidth;
	}

	public Integer getScreenshotHeight() {
		return screenshotHeight;
	}

	public void setScreenshotHeight(Integer screenshotHeight) {
		this.screenshotHeight = screenshotHeight;
	}

	public String getScreenshotImgFormat() {
		return screenshotImgFormat;
	}

	public void setScreenshotImgFormat(String screenshotImgFormat) {
		this.screenshotImgFormat = screenshotImgFormat;
	}

	public Integer getScreenshotOffset() {
		return screenshotOffset;
	}

	public void setScreenshotOffset(Integer screenshotOffset) {
		this.screenshotOffset = screenshotOffset;
	}

	public String getRotate() {
		return rotate;
	}

	public void setRotate(String rotate) {
		this.rotate = rotate;
	}

	public String getChanageRotate() {
		return chanageRotate;
	}

	public void setChanageRotate(String chanageRotate) {
		this.chanageRotate = chanageRotate;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<String> getParams() throws Exception {
		List<String> commandList = new ArrayList<String>();
		AudioVideoType fileType = MediaHelper.detected(suffix);
		if (fileType == AudioVideoType.UNKNOWN) {
			throw new Exception("unknown or unsupport audio or video file type");
		}
		commandList.add("-ss");
		if (screenshotOffset > 0) {
			commandList.add((screenshotOffset - minFrames) + "");
		} else {
			commandList.add(screenshotOffset + "");
		}
		commandList.add("-i");
		commandList.add(filePath);
		if (fileType != AudioVideoType.VIDEO) {
			throw new Exception("please check your param");
		}
		commandList.add("-f");
		commandList.add("image2");
		commandList.add("-vframes");
		commandList.add("1");
//		commandList.add("-s");
//		String wh = String.format("%d*%d", screenshotWidth,
//				screenshotHeight);
//		commandList.add(wh);

		if(!StringUtils.isBlank(chanageRotate)){
			if(chanageRotate.endsWith("90")){
				commandList.add("-vf");
				commandList.add("transpose=1");
			}
			if(chanageRotate.endsWith("180")){
				commandList.add("-vf");
				commandList.add("transpose=1,transpose=1");
			}
			if(chanageRotate.endsWith("270")){
				commandList.add("-vf");
				commandList.add("transpose=1,transpose=1,transpose=1");
			}
//			
//			String rotateFormat = String.format("rotate=(%s)*(PI/180)", 
//					chanageRotate);
//			commandList.add(rotateFormat);
		}
		commandList.add("-y");
		return commandList;
	}

	public String getSafeFileName(){
		return UUID.randomUUID().toString() + "." + screenshotImgFormat;
	}

}