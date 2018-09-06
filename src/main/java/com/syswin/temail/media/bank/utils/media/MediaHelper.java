package com.syswin.temail.media.bank.utils.media;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class MediaHelper {
	private final static String[]  audioSuffixArr = new String[]{
		".mp3",".amr"
	};
	private final static String[]  videoSuffixArr= new String[]{
		".mp4",".avi",".mkv",".rmvb","flv","wmv","mov","3gp"
	};
	public static AudioVideoType detected(String suffix){
		suffix = suffix.toLowerCase();
		List<String> videoArrList=Arrays.asList(videoSuffixArr);
		if(videoArrList.contains(suffix)){
			return AudioVideoType.VIDEO;
		}
		List<String> audioarList = Arrays.asList(audioSuffixArr);
		if(audioarList.contains(suffix)){
			return AudioVideoType.AUDIO;
		}
		return AudioVideoType.UNKNOWN;
	}

	public static final void isSupport(String format)
			throws Exception{
		//忽略大小写
		format = format.toLowerCase();
		List<String> list = Arrays.asList(audioSuffixArr);
		List<String> list2 = Arrays.asList(videoSuffixArr);
		if(!list.contains(format) && !list2.contains(format)){
			throw new Exception("unsupport audio or video format");
		}
	}

	public static final void isInvalidRate(String rate)
			throws Exception{
		if(StringUtils.isBlank(rate)){
			throw new Exception("audio sample rate is invalid");
		}
		if(!rate.endsWith("k")){
			throw new Exception("audio sample rate is invalid");
		}
	}

	public static final void isInvalidBitrate(String bitrate)
			throws Exception{
		if(StringUtils.isBlank(bitrate)){
			throw new Exception("bitrate is invalid");
		}
		if(!bitrate.endsWith("k")){
			throw new Exception("bitrate is invalid");
		}
	}

	public static void isInvalidQuality(String quality)
			throws Exception{
		if(StringUtils.isBlank(quality)){
			throw  new Exception("audio quality is no set");
		}
		if(Integer.parseInt(quality)<0){
			throw  new Exception("audio quality is invalid");
		}
	}

	//获取文件后缀
	public static String getMediaSuffix(String file)
			throws Exception{

		if(StringUtils.isBlank(file)){
			throw new Exception("input file is invalid");
		}
		int index = file.lastIndexOf(".");
		if(index == -1){
			throw new Exception("input file is invalid");
		}
		return file.substring(index);
	}

	//从绝对路径中获取文件
	public static String getMediaFile(String file) throws Exception{
		if(StringUtils.isBlank(file)){
			throw new Exception("file is invalid");
		}
		int index = StringUtils.lastIndexOf(file, "/");
		if(index==-1){
			throw new Exception("input http file is invalid");
		}
		//最后不能是/
		if(index+1>=file.length()){
			throw new Exception("input http file is invalid");
		}
		return file.substring(index+1);
	}
}