package com.syswin.temail.media.bank.utils.media;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syswin.temail.media.bank.bean.VideoFrame;
import com.syswin.temail.media.bank.constants.MediaConstants;
import com.syswin.temail.media.bank.utils.images.ImageUtils;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class MediaUtils {

    public static byte[] vframe(byte[] media, String suffix, int width, int height, int offset, String format) throws Exception {
        if(media.length > MediaConstants.MAX_VIDEO_VFRAME_SIZE){
            throw new Exception("media source too large");
        }
        //视频处理参数
        VideoFrame videoFrame = new VideoFrame();
        videoFrame.setScreenshotImgFormat(format);
        videoFrame.setScreenshotOffset(offset);
        videoFrame.setScreenshotWidth(width);
        videoFrame.setScreenshotHeight(height);
        videoFrame.setSuffix(suffix);

        //将文件写入临时路径
        String tempPath = FileUtils.getTempDirectoryPath() + "/" + UUID.randomUUID().toString() + System.currentTimeMillis() + suffix;
        File outFile = new File(tempPath);
        FileUtils.writeByteArrayToFile(outFile, media);
        try {
            videoFrame.setFilePath(tempPath);
            //获取视频的基本信息
            VideoParam param = new VideoParam();
            param.setVideo(tempPath);
            param.setSuffix(suffix);
            Object result = avInfo(param, ParamParse.Action.AV_INFO);
            String tmp = JSON.toJSONString(result);
            Pattern p = Pattern.compile("\"rotate\":\"([0-9]+)\"");
            Matcher m = p.matcher(tmp);
            while (m.find()) {
                videoFrame.setChanageRotate(m.group(1));
                break;
            }
            JSONObject videoInfo = JSON.parseObject(tmp);
            if (videoInfo != null && videoInfo.containsKey("format")) {
                int shotoffset = videoFrame.getScreenshotOffset();
                int duration = 0;
                JSONObject formatJson = videoInfo.getJSONObject("format");
                duration = (int)formatJson.getDoubleValue("duration");
                if(duration < shotoffset){
                    videoFrame.setScreenshotOffset(duration);
                }
            } else {
                videoFrame.setScreenshotOffset(0);
            }
        } catch (Exception e) {
            //忽略错误
        }
        try{
            // 获取参数列表
            List<String> commandList = videoFrame.getParams();
            // 处理文件
            byte[] outBuffer = FFmpeg.process(commandList, videoFrame.getSafeFileName());
            if (outBuffer == null || outBuffer.length == 0) {
                throw new Exception("vframe error");
            }
            byte[] b = ImageUtils.scaleVframeImage(videoFrame.getScreenshotWidth(), videoFrame.getScreenshotHeight(), 70, outBuffer);
            if (b == null || b.length <= 0) {
                throw new Exception("scale image error");
            }
            return b;
        } finally {
            FileUtils.deleteQuietly(new File(tempPath));
        }

    }

    public static Object avInfo(VideoParam param, ParamParse.Action action) throws Exception {
        // 预处理参数
        ParamParse paramParse = new ParamParse(param);
        String file = param.getVideo();
        if (StringUtils.isBlank(file)) {
            throw new Exception("input file is invalid");
        }
        // 如果没有后缀
        paramParse.setDownTmpFile(file);

        // 获取参数列表
        List<String> commandList = paramParse.getParams(action);
        // 处理文件
        String outBuffer = FFprobe.process(commandList);
        if (StringUtils.isBlank(outBuffer)) {
            throw new Exception("outBuffer is empty");
        }
        outBuffer = outBuffer.replace("\r", "");
        outBuffer = outBuffer.replace("\n", "");
        outBuffer = outBuffer.replace(" ", "");

        Pattern p = Pattern.compile("\"filename\":\"([0-9a-zA-Z+-_.]+?)\"");
        Matcher m = p.matcher(outBuffer);
        while (m.find()) {
            String fileName = MediaHelper.getMediaFile(m.group(1));
            outBuffer=outBuffer.replaceAll("\"filename\":\"([0-9a-zA-Z+-_.]+?)\"",
                    "\"filename\":\""+fileName+"\"");
            break;
        }
        // 移除临时目录
        //removeTempPath(object);
        return JSON.parse(outBuffer);
    }
}
