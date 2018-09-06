package com.syswin.temail.media.bank.service.impl;

import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.MediaService;
import com.syswin.temail.media.bank.utils.HttpClientUtils;
import com.syswin.temail.media.bank.utils.media.MediaUtils;
import java.util.concurrent.Future;
import javax.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class MediaServiceImpl implements MediaService {

    @Override
    @Async("videoExecutor")
    public Future vframe(String fileUrl, String suffix, int width, int height, int offset, String format, HttpServletRequest request) throws Exception {
        //下载文件
        byte[] media = HttpClientUtils.download(fileUrl, request);
        //视频处理
        if(media == null){
            throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "download error");
        }
        byte[] vframeImage = MediaUtils.vframe(media, suffix ,width, height, offset, format);
        if(vframeImage == null){
            throw new DefineException(ResponseCodeConstants.SERVER_ERROR, "vframe error");
        }
        return new AsyncResult<>(vframeImage);
    }
}
