package com.syswin.temail.media.bank.controller;

import com.syswin.temail.media.bank.constants.ImageConstants;
import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.ImageService;
import com.syswin.temail.media.bank.utils.HttpClientUtils;
import com.syswin.temail.media.bank.utils.images.ImageUtils;
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
import java.net.URLDecoder;
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

@Api(value="/", tags="图片处理接口")
@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @ApiOperation(value = "图片缩略图", produces = "image/*")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "fileUrl", paramType = "query", value = "文件下载地址", dataType = "string"),
            @ApiImplicitParam(required = true, name = "mode", paramType = "query", value = "缩略模式，0：等比缩放，不裁剪；1：等比缩放，居中裁剪；2：强制缩放，不裁剪",
                    defaultValue = "0", dataType = "int"),
            @ApiImplicitParam(required = true, name = "width", paramType = "query", value = "图片宽度，缩略模式为0图片长边最大值；缩略模式为1：图片宽度最小值；缩略模式为2：图片宽度", dataType = "int"),
            @ApiImplicitParam(required = true, name = "height", paramType = "query", value = "图片高度,缩略模式为0：图片短边最大值；缩略模式为1：图片高度最小值；缩略模式为2：图片高度", dataType = "int"),
            @ApiImplicitParam(required = true, name = "format", paramType = "query", value = "图片转换类型", dataType = "string"),
            @ApiImplicitParam(name = "suffix", paramType = "query", value = "文件后缀，如.jpg", dataType = "string"),
            @ApiImplicitParam(name = "quality", paramType = "query", value = "图片质量（0：差（10%）；1：一般（40%）；2：标清（默认）（70%）；3:高清（90%）。 质量等级分0到3，等级越高，图片越大。该参数不支持gif格式，jpg、webp格式为有损压缩，png、tiff格式为无损压缩）",
                    defaultValue = "2", dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Param Error"),
            @ApiResponse(code = 401, message = "Access Forbide"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @GetMapping("/thumbnailByUrl")
    public ResponseEntity<byte[]> thumbnailByUrl(@RequestParam String fileUrl, @RequestParam(defaultValue = "0") int mode,
                                         @RequestParam int width, @RequestParam int height,
                                         @RequestParam String format, @RequestParam(required=false) String suffix,
                                         @RequestParam(required=false, defaultValue = "2") int quality,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception{
        long beginTime = System.currentTimeMillis();
        EnumStateAction state = EnumStateAction.ERROR;
        try {
            if(!HttpClientUtils.checkUrl(fileUrl)){
                throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "fileUrl error");
            }
            if (mode < 0 || mode > 2) {
                mode = 0;
            }
            if (width < 0 || height<0) {
                throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "width or height error");
            }
            if(StringUtils.isBlank(suffix)){
                suffix = fileUrl.contains(".")?fileUrl.substring(fileUrl.lastIndexOf(".")):"";
            }
            if(StringUtils.isBlank(format)){
                format = suffix.startsWith(".")?suffix.substring(1):suffix;
            }
            if (quality < 0 || quality > 3) {
                quality = 2;
            }
            Future<byte[]> future = imageService.thumbnail(fileUrl,width,height,ImageUtils.level2Quality(quality, format),mode,format,request);
            byte[] img = future.get(ImageConstants.THUMBNAIL_TIMEOUT, TimeUnit.SECONDS);
            state = EnumStateAction.NORMAL;
            return ResponseEntity.ok().contentLength(img.length).contentType(MediaType.parseMediaType("image/" + format)).body(img);
        } finally {
            StorageLogUtils.logAction(new StorageLogDto(EnumLogAction.thumbnailByUrl.getCode(), beginTime,
                    state.getCode(), response.getHeader("tMark"), request));
        }
    }

    @ApiOperation(value = "图片裁切图", produces = "image/*")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "fileUrl", paramType = "query", value = "文件下载地址", dataType = "string"),
            @ApiImplicitParam(required = true, name = "x", paramType = "query", value = "裁剪的起始X轴坐标", dataType = "int"),
            @ApiImplicitParam(required = true, name = "y", paramType = "query", value = "裁剪的起始Y轴坐标", dataType = "int"),
            @ApiImplicitParam(required = true, name = "x1", paramType = "query", value = "裁剪的结束X轴坐标", dataType = "int"),
            @ApiImplicitParam(required = true, name = "y1", paramType = "query", value = "裁剪的结束Y轴坐标", dataType = "int"),
            @ApiImplicitParam(name = "suffix", paramType = "query", value = "文件后缀，如.jpg", dataType = "string"),
            @ApiImplicitParam(name = "quality", paramType = "query", value = "图片质量（0：差（10%）；1：一般（40%）；2：标清（默认）（70%）；3:高清（90%）。 质量等级分0到3，等级越高，图片越大。该参数不支持gif格式，jpg、webp格式为有损压缩，png、tiff格式为无损压缩）",
                    defaultValue = "2", dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Param Error"),
            @ApiResponse(code = 401, message = "Access Forbide"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @GetMapping("/cropByUrl")
    public ResponseEntity<byte[]> cropByUrl(@RequestParam String fileUrl, @RequestParam(defaultValue = "0") int x,
                                            @RequestParam int y, @RequestParam int x1,
                                            @RequestParam int y1, @RequestParam(required=false) String suffix,
                                            @RequestParam(required=false, defaultValue = "2") int quality,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        long beginTime = System.currentTimeMillis();
        EnumStateAction state = EnumStateAction.ERROR;
        try {
            if(!HttpClientUtils.checkUrl(fileUrl)){
                throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "fileUrl error");
            }
            fileUrl = URLDecoder.decode(fileUrl, "UTF-8");
            if(StringUtils.isBlank(suffix)){
                suffix = fileUrl.contains(".")?fileUrl.substring(fileUrl.lastIndexOf(".")):"";
            }
            if (quality < 0 || quality > 3) {
                quality = 2;
            }
            String format = suffix.startsWith(".")?suffix.substring(1):suffix;
            if(StringUtils.isBlank(format)){
                throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "no suffix found");
            }
            Future<byte[]> future = imageService.crop(fileUrl, x, y, x1, y1, ImageUtils.level2Quality(quality, format), format, request);
            byte[] img = future.get(ImageConstants.THUMBNAIL_TIMEOUT, TimeUnit.SECONDS);
            state = EnumStateAction.NORMAL;
            return ResponseEntity.ok().contentLength(img.length).contentType(MediaType.parseMediaType("image/" + format)).body(img);
        } finally {
            StorageLogUtils.logAction(new StorageLogDto(EnumLogAction.cropByUrl.getCode(), beginTime,
                    state.getCode(), response.getHeader("tMark"), request));
        }
    }
}
