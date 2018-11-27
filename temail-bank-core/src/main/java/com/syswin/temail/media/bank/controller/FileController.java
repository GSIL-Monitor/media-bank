package com.syswin.temail.media.bank.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.syswin.temail.media.bank.bean.Range;
import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.FileService;
import com.syswin.temail.media.bank.utils.HttpClientUtils;
import com.syswin.temail.media.bank.utils.HttpContentTypeUtils;
import com.syswin.temail.media.bank.utils.logs.EnumLogAction;
import com.syswin.temail.media.bank.utils.logs.EnumStateAction;
import com.syswin.temail.media.bank.utils.logs.StorageLogDto;
import com.syswin.temail.media.bank.utils.logs.StorageLogUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/", tags = "文件处理接口")
@RestController
public class FileController {

  private static final Logger logger = LoggerFactory.getLogger(FileController.class);

  @Autowired
  private FileService fileService;

  @ApiOperation(value = "文件直接上传", produces = "application/json;charset=UTF-8")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "TeMail", paramType = "header", value = "temail信息", dataType = "string"),
      @ApiImplicitParam(name = "algorithm", paramType = "header", value = "", dataType = "string"),
      @ApiImplicitParam(name = "SIGNATURE", paramType = "header", value = "", dataType = "string"),
      @ApiImplicitParam(name = "UNSIGNED_BYTES", paramType = "header", value = "", dataType = "string"),
      @ApiImplicitParam(name = "pub", paramType = "query", value = "文件公私有,公有为1,私有非1,默认公有", dataType = "int"),
      @ApiImplicitParam(name = "suffix", paramType = "query", value = "文件后缀名", dataType = "string")
  })
  @ApiResponses({
      @ApiResponse(code = 400, message = "Param Error"),
      @ApiResponse(code = 401, message = "Access Forbide"),
      @ApiResponse(code = 500, message = "Server Error")
  })
  @RequestMapping(value = "uploadFile", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
  public ResponseEntity<Map<String, Object>> uploadFile(
      @ApiParam(name = "file", value = "上传的文件", required = true) MultipartFile file
      , @RequestParam(value = "pub", required = false, defaultValue = "1") Integer pub
      , @RequestParam(value = "suffix", required = false) String suffix
      , HttpServletRequest request, HttpServletResponse response) {
    EnumStateAction state = EnumStateAction.ERROR;
    long beginTime = System.currentTimeMillis();
    long fileSize = 0;
    String fileId = null;
    try {
      checkParam(file == null || file.getSize() <= 0, "file size is error");
      fileSize = file.getSize();
      String domain = HttpClientUtils.getDomainByUrl("uploadFile", request);
      Map<String, Object> resultMap = fileService.uploadFile(file, pub, suffix, domain);
      fileId = (String) resultMap.get("fileId");
      state = EnumStateAction.NORMAL;
      return new ResponseEntity<>(resultMap, HttpStatus.OK);
    } finally {
      StorageLogUtils.logAction(new StorageLogDto(EnumLogAction.uploadFile.getCode(), beginTime,
          fileSize, fileId, state.getCode(), response.getHeader("tMark"),
          request));
    }
  }

  @ApiOperation(value = "文件分片上传", produces = "application/json;charset=UTF-8")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "TeMail", paramType = "header", value = "temail信息", dataType = "string"),
      @ApiImplicitParam(name = "algorithm", paramType = "header", value = "", dataType = "string"),
      @ApiImplicitParam(name = "SIGNATURE", paramType = "header", value = "", dataType = "string"),
      @ApiImplicitParam(name = "UNSIGNED_BYTES", paramType = "header", value = "", dataType = "string"),
      @ApiImplicitParam(name = "pub", paramType = "query", value = "文件公私有,公有为1,私有非1,默认公有", dataType = "int"),
      @ApiImplicitParam(name = "suffix", paramType = "query", value = "文件后缀名", dataType = "string"),
      @ApiImplicitParam(required = true, name = "length", paramType = "query", value = "文件总长度", dataType = "int"),
      @ApiImplicitParam(required = true, name = "uuid", paramType = "query", value = "文件句柄,第一次请求时为空", dataType = "string"),
      @ApiImplicitParam(required = true, name = "offset", paramType = "query", value = "偏移量,初始值为0, 表示已经传送完毕的文件的长度", dataType = "int"),
      @ApiImplicitParam(required = true, name = "currentSize", paramType = "query", value = "当前块大小", dataType = "int")
  })
  @ApiResponses({
      @ApiResponse(code = 400, message = "Param Error"),
      @ApiResponse(code = 401, message = "Access Forbide"),
      @ApiResponse(code = 500, message = "Server Error")
  })
  @RequestMapping(value = "continueUpload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
  public Map<String, Object> continueUpload(
      @ApiParam(name = "file", value = "上传的文件块", required = true) MultipartFile file
      , @RequestParam(value = "pub", required = false, defaultValue = "1") Integer pub
      , @RequestParam(value = "suffix", required = false) String suffix
      , @RequestParam(value = "length") Integer length
      , @RequestParam(value = "uuid") String uuid
      , @RequestParam(value = "offset") Integer offset
      , @RequestParam(value = "currentSize") Integer currentSize
      , HttpServletRequest request, HttpServletResponse response) {
    EnumStateAction state = EnumStateAction.ERROR;
    long beginTime = System.currentTimeMillis();
    Map<String, Object> continueUpload = null;
    long fileSize = 0;
    String fileId = null;
    try {
      checkParam(file == null || file.getSize() <= 0, "file size is error");
      fileSize = file.getSize();

      String domain = HttpClientUtils.getDomainByUrl("continueUpload", request);
      continueUpload = fileService
          .continueUpload(file, pub, suffix, length, uuid, offset, currentSize, domain);
      fileId = continueUpload.get("fileId") == null ? null : (String) continueUpload.get("fileId");
      state = EnumStateAction.NORMAL;
      return continueUpload;
    } finally {
      StorageLogUtils.logAction(new StorageLogDto(EnumLogAction.continueUpload.getCode(), beginTime,
          fileSize, fileId, state.getCode(), response.getHeader("tMark"),
          request));
    }
  }

  private void checkParam(boolean b, String s) throws DefineException {
    if (b) {
      throw new DefineException(ResponseCodeConstants.PARAM_ERROR, s);
    }
  }

  @ApiOperation(value = "文件直接/断点下载", produces = "/downloadFile")
  @ApiImplicitParams({
      @ApiImplicitParam(required = true, name = "fileId", paramType = "query", value = "上传文件时返回的文件id", dataType = "string"),
      @ApiImplicitParam(name = "suffix", paramType = "query", value = "文件后缀名", dataType = "string")
  })
  @ApiResponses({
      @ApiResponse(code = 400, message = "Param Error"),
      @ApiResponse(code = 401, message = "Access Forbide"),
      @ApiResponse(code = 500, message = "Server Error")
  })
  @RequestMapping(value = "downloadFile", method = RequestMethod.GET)
  public void downloadFile(@RequestParam("fileId") String fileId,
      @RequestParam(value = "suffix", required = false)
          String suffix, HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> resultMap;
    long beginTime = System.currentTimeMillis();
    int length = 0;
    EnumStateAction state = EnumStateAction.ERROR;
    try {
      resultMap = fileService.downloadFile(fileId, suffix);
      String contentType = HttpContentTypeUtils.getMineType(suffix);
      byte[] downloadFile = (byte[]) resultMap.get("file");
      length = downloadFile.length;

      response.addHeader("Accept-Ranges", "bytes");
      response.addHeader("Content-Transfer-Encoding", "binary");
      response.setContentType(contentType);
      response.setCharacterEncoding("UTF-8");

      String rangeHeader = request.getHeader("Range");
      if (StringUtils.isNotBlank(rangeHeader)) {
        Range range = new Range(rangeHeader);
        if (range.isRange()) {
          if (range.getEnd() == 0) {
            range.setEnd(length - 1);
          }
          int contentLength = range.getEnd() - range.getPos() + 1;
          response.setHeader("Content-Range",
              "bytes " + range.getPos() + "-" + (range.getEnd()) + "/" + length);
          response.setStatus(206);
          response.addHeader("Content-FileSize", "" + contentLength);
          response.setContentLength(contentLength);
          try (OutputStream outputSream = response.getOutputStream()) {
            outputSream.write(downloadFile, range.getPos(), contentLength);
          } catch (IOException e) {
            logger.error("downloadFile range exception", e);
            throw new DefineException("downloadFile range exception", e);
          }
        } else {
          response.setStatus(416);
        }
      } else {
        response.addHeader("Content-FileSize", String.valueOf(length));
        response.setContentLength(length);
        try (OutputStream outputSream = response.getOutputStream()) {
          outputSream.write(downloadFile);
        } catch (IOException e) {
          logger.error("downloadFile exception", e);
          throw new DefineException("downloadFile exception", e);
        }
      }
      state = EnumStateAction.NORMAL;
    } finally {
      StorageLogUtils.logAction(new StorageLogDto(EnumLogAction.download.getCode(), beginTime,
          length, fileId, state.getCode(), response.getHeader("tMark"), request));
    }
  }

}