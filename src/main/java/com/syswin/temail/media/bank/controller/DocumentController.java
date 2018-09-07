package com.syswin.temail.media.bank.controller;

import com.systoon.integration.spring.boot.disconf.common.annotation.RefreshScope;
import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.DocumentService;
import com.syswin.temail.media.bank.utils.HttpClientUtils;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value="/", tags="文档处理接口")
@RestController
@RefreshScope
public class DocumentController {

    @Value("${url.office.convert}")
    private String officeConvertUrl;

    @Autowired
    private DocumentService documentService;

    @ApiOperation(value = "文档预览", produces = "application/x-zip-compressed")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "fileUrl", paramType = "query", value = "文件下载地址", dataType = "string"),
            @ApiImplicitParam(name = "suffix", paramType = "query", value = "文件后缀，如.docx", dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "Param Error"),
            @ApiResponse(code = 401, message = "Access Forbide"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @GetMapping("/previewByUrl")
    public ResponseEntity<byte[]> previewByUrl(@RequestParam String fileUrl, @RequestParam(required=false) String suffix,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception{
        long beginTime = System.currentTimeMillis();
        EnumStateAction state = EnumStateAction.ERROR;
        try {
            if(!HttpClientUtils.checkUrl(fileUrl)){
                throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "fileUrl error");
            }
            if(StringUtils.isBlank(suffix)){
                suffix = fileUrl.contains(".")?fileUrl.substring(fileUrl.lastIndexOf(".")):"";
            }
            byte[] previewFile = documentService.preview(officeConvertUrl, fileUrl, suffix, request);
            state = EnumStateAction.NORMAL;
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept-Ranges", "bytes");
            headers.add("Content-Transfer-Encoding", "binary");
            return ResponseEntity.ok().headers(headers).contentLength(previewFile.length).
                    contentType(MediaType.parseMediaType("application/x-zip-compressed")).body(previewFile);
        } finally {
            StorageLogUtils.logAction(new StorageLogDto(EnumLogAction.previewByUrl.getCode(), beginTime,
                    state.getCode(), response.getHeader("tMark"), request));
        }
    }

}
