package com.syswin.temail.media.bank.service.impl;

import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.ImageService;
import com.syswin.temail.media.bank.utils.HttpClientUtils;
import com.syswin.temail.media.bank.utils.images.ImageUtils;
import java.util.concurrent.Future;
import javax.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    @Async("imageExecutor")
    public Future<byte[]> thumbnail(String fileUrl, int width, int height, double quality, int type, String format,
            HttpServletRequest request) throws Exception {
        //下载文件
        byte[] image = HttpClientUtils.download(fileUrl, request);
        //图片处理
        if(image == null){
            throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "download error");
        }
        byte[] thumbnailImage = ImageUtils.scaleImageByGm(width, height, quality, type, image, format);
        if(thumbnailImage == null){
            throw new DefineException(ResponseCodeConstants.SERVER_ERROR, "thumbnail error");
        }
        return new AsyncResult<>(thumbnailImage);
    }

    @Override
    @Async("imageExecutor")
    public Future crop(String fileUrl, int x, int y, int x1, int y1, int quality, String format, HttpServletRequest request) throws Exception {
        //下载文件
        byte[] image = HttpClientUtils.download(fileUrl, request);
        //图片处理
        if(image == null){
            throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "download error");
        }
        byte[] cropImage = ImageUtils.cropImageByGm(image, x, y, x1, y1, quality, format);
        if(cropImage == null){
            throw new DefineException(ResponseCodeConstants.SERVER_ERROR, "crop error");
        }
        return new AsyncResult<>(cropImage);
    }
}
