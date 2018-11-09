package com.syswin.temail.media.bank.utils.media;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ParamParse {

	public enum Action {
		AUDIO_CONVERT, SCREEN_SHOT, AV_INFO
	}

	private VideoParam param;
	private String downTmpFile = "";

	public void setDownTmpFile(String downTmpFile) {
		this.downTmpFile = downTmpFile;
	}

	public ParamParse(VideoParam param) throws Exception {
		this.param = param;
	}

	// //需要转码的文件，目前只支持http
	// private String file;
	// //需要转换的目标格式
	// private String audioFromat;
	// //音频码率，单位：比特每秒（bit/s），常用码率：64k，128k，192k，256k，320k等。若指定码率大于原音频码率，则使用原音频码率进行转码。
	// private String audioBitRate;
	// //音频质量，取值范围为0-9（mp3），10-500（aac），仅支持mp3和aac，值越小越高。不能与上述码率参数共用。
	// private String audioQuality;
	public List<String> getParams(Action action) throws Exception {
		// 命令列表
		List<String> commandList = new ArrayList<String>();
		// 检测文件类型
		String inputFile = "";
		if (!downTmpFile.isEmpty()) {
			inputFile = downTmpFile;
		} else {
			inputFile = param.getVideo();
		}
		String suffix = MediaHelper.getMediaSuffix(inputFile);
		// 检测文件类型
		AudioVideoType fileType = MediaHelper.detected(suffix);
		if (fileType == AudioVideoType.UNKNOWN) {
			throw new Exception("unknown or unsupport audio or video file type");
		}
		commandList.add("-i");
		commandList.add(inputFile);

		if (action == Action.AUDIO_CONVERT) {
			if (fileType != AudioVideoType.AUDIO) {
				throw new Exception("please check your param");
			}
			String format = param.getAudioFormat();
			// 如果转码 is empty，返回异常
			if (StringUtils.isBlank(format)) {
				throw new Exception("error, please check your param");
			}
			// 检测是否是支持的转码
			MediaHelper.isSupport(format);
			// 音频参数设置
			// 获取采样率
			String audioSampleRate = param.getAudioSampleRate();
			MediaHelper.isInvalidRate(audioSampleRate);

			if (format.equals("mp3")) { // amr转换成MP3
				// 如果输入不是amr文件，咱们返回
				if (!suffix.equals(".amr")) {
					throw new Exception("invalid input file or format");
				}
			} else if (format.equals("amr")) {
				// 如果输入不是mp3文件，咱们返回
				if (!suffix.equals(".mp3")) {
					throw new Exception("invalid input file or format");
				}
				// mp3 转换成amr采样率只能是8000
				if (!audioSampleRate.equals("8k")) {
					throw new Exception("mp3 to amr sample rate must be 8k");
				}

				commandList.add("-ac");
				commandList.add("1");
			}

			commandList.add("-ar");
			commandList.add(audioSampleRate);

			// 获取比特率
			String audioBitRate = param.getAudioBitRate();
			MediaHelper.isInvalidBitrate(audioBitRate);
			if (!audioBitRate.equals("0k")) {
				// 测试比特率是否有效，如果是默认就传入0k即可
				commandList.add("-ab");
				commandList.add(audioBitRate);
			}
			// 获取音质
			String audioQuality = param.getAudioQuality();
			MediaHelper.isInvalidQuality(audioQuality);
			commandList.add("-aq");
			commandList.add(audioQuality);
			
			commandList.add("-threads");
			commandList.add("2");
			
			// 如果存在文件就覆盖
			commandList.add("-y");
			return commandList;

		} else if (action == Action.SCREEN_SHOT) {
			if (fileType != AudioVideoType.VIDEO) {
				throw new Exception("please check your param");
			}

			commandList.add("-f");
			commandList.add("image2");
			commandList.add("-ss");
			commandList.add(param.getScreenshotOffset()+"");
			commandList.add("-t");
			commandList.add("0.01");
			commandList.add("-s");
			String wh = String.format("%d*%d", param.getScreenshotWidth(),
					param.getScreenshotHeight());
			commandList.add(wh);
			String rotate = param.getRotate();
			if(!StringUtils.isBlank(rotate)){
				if(rotate.equals("90")){
					commandList.add("-vf");
					commandList.add("transpose=1");
				}else if(rotate.equals("180")){
					commandList.add("-vf");
					commandList.add("vflip");
				}else if(rotate.equals("270")){
					commandList.add("-vf");
					commandList.add("transpose=2");
				}
			}
			

			commandList.add("-threads");
			commandList.add("2");
			
			commandList.add("-y");
			return commandList;
		} else if (action == Action.AV_INFO) {
			// -v quiet -print_format json -show_format -show_streams
			//commandList.add(0, FFPROBE);
			commandList.add("-v");
			commandList.add("quiet");
			commandList.add("-print_format");
			commandList.add("json");
			commandList.add("-show_format");
			commandList.add("-show_streams");
			return commandList;
		}

		throw new Exception("error, please check your param");
	}

}