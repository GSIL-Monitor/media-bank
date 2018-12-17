package com.syswin.temail.media.bank.service.ufile;

import static com.syswin.temail.media.bank.constants.ResponseCodeConstants.FORBID_ACCESS_ERROR;
import static com.syswin.temail.media.bank.constants.ResponseCodeConstants.NOT_FOUND_ERROR;
import static com.syswin.temail.media.bank.constants.ResponseCodeConstants.SUCCESS;

import cn.ucloud.ufile.UFileRequest;
import cn.ucloud.ufile.UFileSDK;
import com.google.gson.Gson;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.FileService;
import com.syswin.temail.media.bank.utils.AESEncrypt;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 姚华成
 * @date 2018-12-12
 */
@Slf4j
public class UFileFileService implements FileService {

  private static final String HTTP_METHOD_PUT = "PUT";
  private static final String HTTP_METHOD_GET = "GET";
  private static final String DOWNLOAD_FILE = "downloadFile";
  private static final String UPLOAD_FILE = "uploadFile";
  private UFileSDK ufileSDK;
  private static final Gson GSON = new Gson();
  private UFileProperties properties;
  private int timeoutInSeconds = 11; // 由于okhttp默认读写超时时间是10秒，UFileSDK使用了默认的okhttp，因此同步等待时间设置为11秒

  public UFileFileService(UFileProperties properties) {
    this.properties = properties;
    this.ufileSDK = ufileSDK(properties);
  }

  private UFileSDK ufileSDK(UFileProperties properties) {
    UFileSDK ufileSDK = new UFileSDK();
    if (properties.getCdnHost() != null && !properties.getCdnHost().isEmpty()) {
      ufileSDK.initCDN(properties.getBucket(), properties.getCdnHost(), properties.getPublicKey(),
          properties.getPrivateKey());
    } else if (properties.getUpProxySuffix() != null && !properties.getUpProxySuffix().isEmpty()) {
      ufileSDK.initGlobal(properties.getBucket(), properties.getUpProxySuffix(), properties.getDlProxySuffix(),
          properties.getPublicKey(), properties.getPrivateKey());
    } else {
      ufileSDK.init(properties.getBucket(), properties.getProxySuffix(), properties.getPublicKey(),
          properties.getPrivateKey());
    }
    return ufileSDK;
  }

  @Override
  public Map<String, Object> uploadFile(MultipartFile file, Integer pub, String suffix, String domain) {
    try {
      String fileName = file.getOriginalFilename();
      if (StringUtils.isBlank(suffix)) {
        suffix = (fileName != null && fileName.contains(".")) ? fileName.substring(fileName.lastIndexOf(".")) : "";
      }

      String fileId = genFileId();
      UFileRequest request = new UFileRequest();
      request.setHttpMethod(HTTP_METHOD_PUT);
      request.setKey(fileId);
      request.setDate(getNowString());
      request.setContentMD5("");
      request.setContentType(file.getContentType());
      request.setFilePath(fileName);
      AtomicReference<Exception> error = new AtomicReference<>();
      AtomicReference<Response> ufileResponse = new AtomicReference<>();
      CountDownLatch latch = new CountDownLatch(1);
      ufileSDK.putStream(request, new SyncCallback(latch, ufileResponse, error), file.getInputStream());
      latch.await(timeoutInSeconds, TimeUnit.SECONDS);
      Response response = handleResponseError(ufileResponse, error, UPLOAD_FILE);
      Map<String, Object> resultMap = new HashMap<>();
      fileId = AESEncrypt.getInstance().encrypt(fileId);
      resultMap.put("fileId", fileId);
      resultMap.put("pubUrl", domain + fileId + suffix);
      resultMap.put("ETag", response.header("ETag"));
      log.debug("上传返回结果：{}", resultMap);
      return resultMap;
    } catch (DefineException e) {
      throw e;
    } catch (Exception e) {
      throw new DefineException("uploadFile error:" + e.getMessage(), e);
    }
  }

  @Override
  public Map<String, Object> downloadFile(String fileId, String suffix) {
    try {
      if (fileId.contains(".")) {
        String[] split = fileId.split("\\.");
        fileId = split[0];
      }
      fileId = AESEncrypt.getInstance().decrypt(fileId);

      UFileRequest request = new UFileRequest();
      request.setHttpMethod(HTTP_METHOD_GET);
      request.setKey(fileId);
      CountDownLatch latch = new CountDownLatch(1);
      AtomicReference<Response> ufileResponse = new AtomicReference<>();
      AtomicReference<Exception> error = new AtomicReference<>();
      ufileSDK.get(request, new SyncCallback(latch, ufileResponse, error));
      latch.await(timeoutInSeconds, TimeUnit.SECONDS);
      Response response = handleResponseError(ufileResponse, error, DOWNLOAD_FILE);
      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("file", (response.body() != null) ? response.body().bytes() : new byte[0]);
      resultMap.put("userId", null);
      resultMap.put("length", response.header("Content-Length"));
      resultMap.put("contentType", response.header("Content-Type"));
      resultMap.put("ETag", response.header("ETag"));
      return resultMap;
    } catch (DefineException e) {
      throw e;
    } catch (Exception e) {
      throw new DefineException("downloadFile error:" + e.getMessage(), e);
    }
  }


  private Response handleResponseError(AtomicReference<Response> ufileResponse, AtomicReference<Exception> error,
      String methodName) throws IOException {
    if (error.get() != null) {
      throw new DefineException(methodName + " error", error.get());
    }
    Response response = ufileResponse.get();
    if (response == null) {
      throw new DefineException(methodName + " error，timeout:" + timeoutInSeconds + "s");
    }

    if (response.code() == SUCCESS) {
      return response;
    }

    String sessionId = response.header("X-SessionId");
    ResponseBody body = response.body();
    int errorCode = -1;
    String errMsg = "unkown error";
    if (body != null) {
      UFileErrorResponse errorResponse = GSON.fromJson(body.string(), UFileErrorResponse.class);
      errorCode = errorResponse.getRetCode();
      errMsg = errorResponse.getErrMsg();
    }
    String msg = methodName + " error，X-SessionId=" + sessionId + "，RetCode=" + errorCode + ", ErrMsg=" + errMsg;
    if (DOWNLOAD_FILE.equals(methodName) &&
        (response.code() == FORBID_ACCESS_ERROR || response.code() == NOT_FOUND_ERROR)) {
      throw new DefineException(response.code(), msg);
    } else {
      throw new DefineException(msg);
    }
  }

  private String getNowString() {
    return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  @Override
  public int getPubByFileId(String fileId) {
    return 1;
  }

  @Override
  public int getAppIdByFileId(String fileId) {
    return 0;
  }

  private String genFileId() {
    return properties.getTokenPrefix() + "/" + UUID.randomUUID().toString();
  }

  @Data
  private static class UFileErrorResponse {

    private int RetCode;
    private String ErrMsg;
  }

  private static class SyncCallback implements Callback {

    private CountDownLatch latch;
    private AtomicReference<Response> ufileResponse;
    private AtomicReference<Exception> error;

    private SyncCallback(CountDownLatch latch,
        AtomicReference<Response> ufileResponse, AtomicReference<Exception> error) {
      this.latch = latch;
      this.ufileResponse = ufileResponse;
      this.error = error;
    }

    @Override
    public void onFailure(Call call, IOException e) {
      error.set(e);
      latch.countDown();
    }

    @Override
    public void onResponse(Call call, Response response) {
      ufileResponse.set(response);
      latch.countDown();
    }
  }

  @Override
  public Map<String, Object> continueUpload(MultipartFile file, Integer pub, String suffix, Integer length, String uuid,
      Integer offset, Integer currentSize, String domain) {
    throw new UnsupportedOperationException("暂不支持分片续传服务！");
//    try {
//      String fileName = file.getOriginalFilename();
//      if (StringUtils.isBlank(suffix) && fileName != null) {
//        suffix = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
//      }
//      offset = currentSize + offset;
//      if (offset > length) {
//        throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "file length error");
//      }
//      String fileId;
//      String uploadId;
//      int partNumber;
//      if (uuid == null || uuid.equals("") || uuid.equals("null")) {
//        UFileInitMultiResponse initMultiResponse;
//        fileId = genFileId();
//        UFileRequest request = new UFileRequest();
//        request.setKey(fileId);
//        request.setHttpMethod(HTTP_METHOD_POST);
//        request.setDate(getNowString());
//        Response response = ufileSDK.initMulti(request);
//        if (response == null) {
//          throw new DefineException("init multi file error: unkown");
//        }
//        if (response.code() == 200) {
//          if (response.body() != null) {
//            initMultiResponse = GSON
//                .fromJson(response.body().string(), UFileInitMultiResponse.class);
//          } else {
//            throw new DefineException("init multi file error: response body is null");
//          }
//        } else {
//          if (response.body() != null) {
//            UFileErrorResponse errorResponse = GSON
//                .fromJson(response.body().string(), UFileErrorResponse.class);
//            throw new DefineException(
//                "init multi file error，X-SessionId=" + response.header("") + "errorCode=" + errorResponse.getErrorCode()
//                    + ", 错误信息：" + errorResponse.getErrMsg());
//          } else {
//            throw new DefineException("init multi file error: response body is null");
//          }
//        }
//        String uploadId = initMultiResponse.getUploadId();
//      }
//
//      {
//
//        UFileRequest request = new UFileRequest();
//        request.setKey(fileId);
//
//        Response response = ufileSDK.uploadPart(request, uploadId, partNumber, offset, length);

//      }
//
//      // 根据uuid解析出时间戳+加密fileId
//      uuid = AESEncrypt.getInstance().decrypt(uuid);
//      String currentTime = uuid.substring(0, 13);
//      long sysCurrentTime = System.currentTimeMillis();
//      if (Long.parseLong(currentTime) > sysCurrentTime) {
//        throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "file upload time error");
//      }
//      // 获取加密fileId，并进行解密
//      fileId = uuid.substring(13);
//      uploadMulti();
//      if (offset.equals(length)) {
//        UFileFinishMultiResponse finishMultiResponse = finishMulti();
//      }
//      uuid = sysCurrentTime + fileId;
//      PartNumber
//      return null;
//    } catch (Exception e) {
//      throw new DefineException("continueUpload exception:" + e.getMessage(), e);
//    }
  }

//
//  @Data
//  private static class UFileInitMultiResponse {
//
//    private String UploadId;
//    private int BlkSize;
//    private String Bucket;
//    private String Key;
//  }
//
//  @Data
//  private static class UFileFinishMultiResponse {
//
//    private String Bucket;
//    private String Key;
//    private int FileSize;
//  }

}
