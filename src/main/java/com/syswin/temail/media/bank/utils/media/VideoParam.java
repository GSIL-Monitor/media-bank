package com.syswin.temail.media.bank.utils.media;

import java.io.Serializable;
import java.util.Arrays;

public class VideoParam implements Serializable {
	private static final long serialVersionUID = 1586211334341573464L;
	//文件存放位置
	private String pathFile = "";
	
	// 输入文件，目前只支持http
	private String video = "";
	private byte[] byteData=null;
	
	//小视频关键帧图片大小
	private Integer screenshotWidth=350;
	private Integer screenshotHeight=240;
	private String  screenshotImgFormat="png";
	private Integer screenshotOffset = 1;//视频截取时间offset
	private String rotate="";
	
	//转码相关
	// 需要转换的目标格式
	private String audioFormat = "";
	// 音频码率，单位：比特每秒（bit/s），常用码率：64k，128k，192k，256k，320k等。若指定码率大于原音频码率，则使用原音频码率进行转码。
	private String audioBitRate = "0k";
	// 音频质量，取值范围为0-9（mp3），仅支持mp3,值越小越高。不能与上述码率参数共用。
	private String audioQuality = "0";
	//音频采样率，取值有 8k 16k 32k 44.1k
	private String audioSampleRate= "8k";
	
	//视频封装格式支持 flv mp4 avi 3gp mkv mov wmv
	private String videoFormat="";
	private String videoCodec="libx264";
	private String videoBitRate="";
	private String videoFrameRate="";
	
	//水印相关
	// 视频后缀
	private String suffix = ".flv";
	// 水印图片
	private String wmImage = "";
	// 水印文字
	private String wmText = "";
	// 水印图片区域
	/*水印图片区域目前是9个,默认值 NorthEast
	 *NorthWest  |   North   |    NorthEast
	 *___________|___________|_____________
	 *           |           |
	 *West       |   Center  |         East 
	 *___________|___________|_____________
	 *           |           |
	 * SouthWest |   South   |    SouthEast
	 */
	private String wmImageGravity = "NorthEast";
	// 文字图片区域
	private String wmTextGravity = "NorthEast";
	// 视频图片水印位置的相对横向偏移量，当值为正数时则向右偏移，反之向左。
	private Integer wmImageOffsetX = 0;
	// 视频图片水印位置的相对纵向偏移量，当值为正数时则向下偏移，反之向上。
	private Integer wmImageOffsetY = 0;
	// 图片水印透明度0-10，10=不透明
	private Integer wmImageTransparent = 9;
	// 视频文字水印位置的相对横向偏移量，当值为正数时则向右偏移，反之向左。
	private Integer wmTextOffsetX = 0;
	// 视频文字水印位置的相对纵向偏移量，当值为正数时则向下偏移，反之向上。
	private Integer wmTextOffsetY = 0;
	// 文字水印透明度0-10，默认不透明
	private Integer fontTransparent = 9;
	// base64编码过的字体,默认是黑体, 6buR5L2T="黑体"
	private String encodeFont = "6buR5L2T";
	// base64编码过的字体颜色值，目前支持:#FFFFFF,或者是红色、黑色 ，默认是白色 IzAwMDAwMA==   白色
	private String encodeFontColor = "IzAwMDAwMA==";
	// 字体大小，默认大小是16
	private Integer fontSize = 16;
	
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public byte[] getByteData() {
		return byteData;
	}
	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}
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
	public String getAudioFormat() {
		return audioFormat;
	}
	public void setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
	}
	public String getAudioBitRate() {
		return audioBitRate;
	}
	public void setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
	}
	public String getAudioQuality() {
		return audioQuality;
	}
	public void setAudioQuality(String audioQuality) {
		this.audioQuality = audioQuality;
	}
	public String getAudioSampleRate() {
		return audioSampleRate;
	}
	public void setAudioSampleRate(String audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}
	public String getVideoFormat() {
		return videoFormat;
	}
	public void setVideoFormat(String videoFormat) {
		this.videoFormat = videoFormat;
	}
	public String getVideoCodec() {
		return videoCodec;
	}
	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}
	public String getVideoBitRate() {
		return videoBitRate;
	}
	public void setVideoBitRate(String videoBitRate) {
		this.videoBitRate = videoBitRate;
	}
	public String getVideoFrameRate() {
		return videoFrameRate;
	}
	public void setVideoFrameRate(String videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getWmImage() {
		return wmImage;
	}
	public void setWmImage(String wmImage) {
		this.wmImage = wmImage;
	}
	public String getWmText() {
		return wmText;
	}
	public void setWmText(String wmText) {
		this.wmText = wmText;
	}
	public String getWmImageGravity() {
		return wmImageGravity;
	}
	public void setWmImageGravity(String wmImageGravity) {
		this.wmImageGravity = wmImageGravity;
	}
	public String getWmTextGravity() {
		return wmTextGravity;
	}
	public void setWmTextGravity(String wmTextGravity) {
		this.wmTextGravity = wmTextGravity;
	}
	public Integer getWmImageOffsetX() {
		return wmImageOffsetX;
	}
	public void setWmImageOffsetX(Integer wmImageOffsetX) {
		this.wmImageOffsetX = wmImageOffsetX;
	}
	public Integer getWmImageOffsetY() {
		return wmImageOffsetY;
	}
	public void setWmImageOffsetY(Integer wmImageOffsetY) {
		this.wmImageOffsetY = wmImageOffsetY;
	}
	public Integer getWmImageTransparent() {
		return wmImageTransparent;
	}
	public void setWmImageTransparent(Integer wmImageTransparent) {
		this.wmImageTransparent = wmImageTransparent;
	}
	public Integer getWmTextOffsetX() {
		return wmTextOffsetX;
	}
	public void setWmTextOffsetX(Integer wmTextOffsetX) {
		this.wmTextOffsetX = wmTextOffsetX;
	}
	public Integer getWmTextOffsetY() {
		return wmTextOffsetY;
	}
	public void setWmTextOffsetY(Integer wmTextOffsetY) {
		this.wmTextOffsetY = wmTextOffsetY;
	}
	public Integer getFontTransparent() {
		return fontTransparent;
	}
	public void setFontTransparent(Integer fontTransparent) {
		this.fontTransparent = fontTransparent;
	}
	public String getEncodeFont() {
		return encodeFont;
	}
	public void setEncodeFont(String encodeFont) {
		this.encodeFont = encodeFont;
	}
	public String getEncodeFontColor() {
		return encodeFontColor;
	}
	public void setEncodeFontColor(String encodeFontColor) {
		this.encodeFontColor = encodeFontColor;
	}
	public Integer getFontSize() {
		return fontSize;
	}
	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String getPathFile() {
		return pathFile;
	}
	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}
	public void setRotate(String rotate) {
		this.rotate = rotate;
	}
	public String getRotate() {
		return rotate;
	}
	
	public VideoParam() {
		super();
	}
	
	public String toString() {
		return "VideoParam [pathFile=" + pathFile + ", video=" + video + ", byteData="
				+ Arrays.toString(byteData) + ", screenshotWidth=" + screenshotWidth + ", screenshotHeight="
				+ screenshotHeight + ", screenshotImgFormat=" + screenshotImgFormat + ", screenshotOffset="
				+ screenshotOffset + ", rotate=" + rotate + ", audioFormat=" + audioFormat + ", audioBitRate="
				+ audioBitRate + ", audioQuality=" + audioQuality + ", audioSampleRate=" + audioSampleRate
				+ ", videoFormat=" + videoFormat + ", videoCodec=" + videoCodec + ", videoBitRate=" + videoBitRate
				+ ", videoFrameRate=" + videoFrameRate + ", suffix=" + suffix + ", wmImage=" + wmImage + ", wmText="
				+ wmText + ", wmImageGravity=" + wmImageGravity + ", wmTextGravity=" + wmTextGravity
				+ ", wmImageOffsetX=" + wmImageOffsetX + ", wmImageOffsetY=" + wmImageOffsetY + ", wmImageTransparent="
				+ wmImageTransparent + ", wmTextOffsetX=" + wmTextOffsetX + ", wmTextOffsetY=" + wmTextOffsetY
				+ ", fontTransparent=" + fontTransparent + ", encodeFont=" + encodeFont + ", encodeFontColor="
				+ encodeFontColor + ", fontSize=" + fontSize + "]";
	}
	
}