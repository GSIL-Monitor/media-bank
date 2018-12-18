package com.syswin.temail.media.bank.controller;

import com.syswin.temail.media.bank.constants.MediaConstants;
import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.MediaService;
import com.syswin.temail.media.bank.utils.HttpClientUtils;
import com.syswin.temail.media.bank.utils.images.ImageHelper;
import com.syswin.temail.media.bank.utils.logs.EnumLogAction;
import com.syswin.temail.media.bank.utils.logs.EnumStateAction;
import com.syswin.temail.media.bank.utils.logs.StorageLogDto;
import com.syswin.temail.media.bank.utils.logs.StorageLogUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value="/", tags="音视频处理接口")
@RestController
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @ApiOperation(value = "视频截图", produces = "image/*")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "fileUrl", paramType = "query", value = "文件下载地址", dataType = "string"),
            @ApiImplicitParam(required = true, name = "format", paramType = "query", value = "截取图片格式，例如：jpg", dataType = "string"),
            @ApiImplicitParam(required = true, name = "offset", paramType = "query", value = "视频截图时间", dataType = "int"),
            @ApiImplicitParam(required = true, name = "width", paramType = "query", value = "截取图片宽度", dataType = "int"),
            @ApiImplicitParam(required = true, name = "height", paramType = "query", value = "截取图片高度", dataType = "int"),
            @ApiImplicitParam(name = "suffix", paramType = "query", value = "文件后缀，如.mp4", dataType = "string"),

    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Param Error"),
            @ApiResponse(code = 401, message = "Access Forbide"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @GetMapping("/vframeByUrl")
    public ResponseEntity<byte[]> vframeByUrl(@RequestParam String fileUrl, @RequestParam(required=false) String suffix,
                                      @RequestParam String format, @RequestParam int offset,
                                      @RequestParam int width,  @RequestParam int height,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception{
        long beginTime = System.currentTimeMillis();
        EnumStateAction state = EnumStateAction.ERROR;
        try {
            if(HttpClientUtils.isInvalidUrl(fileUrl)){
                throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "fileUrl error");
            }
            if(StringUtils.isBlank(suffix)){
                suffix = fileUrl.contains(".")?fileUrl.substring(fileUrl.lastIndexOf(".")) : ".mp4";
            }
            if (StringUtils.isBlank(format) || (!ImageHelper.screentFormatSupport(format))) {
                throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "unsupport image format");
            }
            if (width <= 0 || height <= 0 || offset < 0) {
                throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "width or heigth invalid");
            }
            if (width > MediaConstants.MAX_VIDEO_VFRAME_WIDTH) {
                width = 4096;
            }
            if (height > MediaConstants.MAX_VIDEO_VFRAME_HEIGHT) {
                height = 4096;
            }
            Future<byte[]> future = mediaService.vframe(fileUrl, suffix, width, height, offset, format, request);
            byte[] img = future.get(MediaConstants.VFRAME_TIMEOUT, TimeUnit.SECONDS);
            state = EnumStateAction.NORMAL;
            return ResponseEntity.ok().contentLength(img.length).contentType(MediaType.parseMediaType("image/" + format)).body(img);
        } finally {
            StorageLogUtils.logAction(new StorageLogDto(EnumLogAction.vframeByUrl.getCode(), beginTime,
                    state.getCode(), response.getHeader("tMark"), request));
        }
    }

}