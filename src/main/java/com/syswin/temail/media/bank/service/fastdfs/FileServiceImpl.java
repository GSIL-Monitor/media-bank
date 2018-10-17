package com.syswin.temail.media.bank.service.fastdfs;

import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.FileService;
import com.syswin.temail.media.bank.utils.AESEncrypt;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

  private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

  @Override
  public Map<String, Object> uploadFile(MultipartFile file, Integer pub, String suffix, int userId,
      String domain) {
    Map<String, Object> resultMap = new HashMap<>();
    String fileName = file.getOriginalFilename();
    if (StringUtils.isBlank(suffix)) {
      suffix = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }
    TrackerServer trackerServer = null;
    String fileId = null;
    try {
      TrackerClient tracker = new TrackerClient();
      trackerServer = tracker.getConnection();
      StorageClient1 client1 = new StorageClient1(trackerServer, null);
      NameValuePair[] metaList = new NameValuePair[5];
      metaList[0] = new NameValuePair("fileName", fileName);
      metaList[1] = new NameValuePair("pub", pub.toString());
      metaList[2] = new NameValuePair("suffix", suffix);
      metaList[3] = new NameValuePair("userId", userId + "");
      metaList[4] = new NameValuePair("length", file.getSize() + "");
      fileId = client1.upload_file1(file.getBytes(), null, metaList);
      //fileId = client1.upload_file1(file.getBytes(), null, metaList);
      if (fileId == null || fileId.length() == 0) {
        throw new DefineException(ResponseCodeConstants.SERVER_ERROR,
            "file upload error,error code: " + client1.getErrorCode());
      }
    } catch (Exception e) {
      logger.error("uploadFile error fileName={}", fileName, e);
      throw new DefineException("uploadFile exception", e);
    } finally {
      if (trackerServer != null) {
        try {
          trackerServer.close();
        } catch (IOException e) {
          logger.warn("trackerServer.close warn", e);
        }
      }
    }
    fileId = AESEncrypt.getInstance().encrypt(fileId);
    resultMap.put("fileId", fileId);
    resultMap.put("pubUrl", domain + fileId + suffix);
    return resultMap;
  }

  @Override
  public Map<String, Object> continueUpload(MultipartFile file, Integer pub, String suffix,
      Integer length,
      String uuid, Integer offset, Integer currentSize, int userId, String domain) {
    String fileId = null;
    String puburl = null;
    String fileName = file.getOriginalFilename();
    if (StringUtils.isBlank(suffix)) {
      suffix = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }
    Map<String, Object> resultMap = new HashMap<>();
    offset = currentSize + offset;
    long sysCurrentTime = System.currentTimeMillis();
    if (offset > length) {
      throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "file length error");
    }
    TrackerServer trackerServer = null;
    try {
      TrackerClient tracker = new TrackerClient();
      trackerServer = tracker.getConnection();
      StorageClient1 client1 = new StorageClient1(trackerServer, null);
      NameValuePair[] metaList = new NameValuePair[6];
      metaList[0] = new NameValuePair("fileName", fileName);
      metaList[1] = new NameValuePair("pub", pub.toString());
      metaList[2] = new NameValuePair("suffix", suffix);
      metaList[3] = new NameValuePair("length", length.toString());
      metaList[4] = new NameValuePair("offset", offset.toString());
      metaList[5] = new NameValuePair("userId", userId + "");

      if (StringUtils.isNotBlank(uuid) && !uuid.equals("null")) {
        // 根据uuid解析出时间戳+加密fileId
        uuid = AESEncrypt.getInstance().decrypt(uuid);
        String currentTime = uuid.substring(0, 13);
        if (Long.parseLong(currentTime) > sysCurrentTime) {
          throw new DefineException(ResponseCodeConstants.PARAM_ERROR, "file upload time error");
        }
        // 获取加密fileId，并进行解密
        fileId = uuid.substring(13);
        int errno = client1.set_metadata1(fileId, metaList, ProtoCommon.STORAGE_SET_METADATA_FLAG_OVERWRITE);
        if (errno != 0) {
          throw new DefineException(ResponseCodeConstants.SERVER_ERROR, "overwrite metadata error");
        }
        int appendErrno = client1.append_file1(fileId, file.getBytes());
        if (appendErrno != 0) {
          throw new DefineException(ResponseCodeConstants.SERVER_ERROR, "append file error");
        }
      } else {
        fileId = client1.upload_appender_file1(file.getBytes(), null, metaList);
      }
      if (fileId == null) {
        throw new DefineException(ResponseCodeConstants.SERVER_ERROR, "file upload error");
      }
      uuid = sysCurrentTime + fileId;
      fileId = AESEncrypt.getInstance().encrypt(fileId);
      // 是否传完
      if (offset.equals(length)) {
        resultMap.put("fileId", fileId);
        puburl = domain + fileId + suffix;
      } else {
        resultMap.put("fileId", null);
      }
      resultMap.put("offset", offset);
      resultMap.put("uuid", AESEncrypt.getInstance().encrypt(uuid));
      resultMap.put("pubUrl", puburl);

      return resultMap;
    } catch (Exception e) {
      logger.error("continueUpload error fileName={}", fileName, e);
      throw new DefineException("continueUpload exception", e);
    } finally {
      if (trackerServer != null) {
        try {
          trackerServer.close();
        } catch (IOException e) {
          logger.warn("trackerServer.close warn", e);
        }
      }
    }
  }

  @Override
  public Map<String, Object> downloadFile(String fileId, String suffix) {
    Map<String, Object> resultMap = new HashMap<>(4);
    if (fileId.contains(".")) {
      String[] split = fileId.split("\\.");
      fileId = split[0];
    }
    fileId = AESEncrypt.getInstance().decrypt(fileId);
    byte[] result = null;
    TrackerServer trackerServer = null;
    try {
      TrackerClient tracker = new TrackerClient();
      trackerServer = tracker.getConnection();
      StorageClient1 client1 = new StorageClient1(trackerServer, null);
      NameValuePair[] metadataList = client1.get_metadata1(fileId);
      Integer userId = null;
      Long length = null;
      String contentType = null;
      for (NameValuePair nameValuePair : metadataList) {
        if (nameValuePair.getName().equals("userId")) {
          userId = Integer.parseInt(nameValuePair.getValue());
          continue;
        }
        if (nameValuePair.getName().equals("length")) {
          length = Long.parseLong(nameValuePair.getValue());
          continue;
        }
        if (nameValuePair.getName().equals("suffix")) {
          contentType = nameValuePair.getValue();
          continue;
        }
      }
      int i = 0;
      result = client1.download_file1(fileId);
      if (result == null || result.length == 0) {
        throw new DefineException(ResponseCodeConstants.SERVER_ERROR,
            "file download error,error code: " + client1.getErrorCode());
      }
      resultMap.put("file", result);
      resultMap.put("userId", userId);
      resultMap.put("length", length);
      resultMap.put("contentType", contentType);
      return resultMap;
    } catch (Exception e) {
      logger.error("downloadFile error fileName={}", fileId, e);
      throw new DefineException("downloadFile exception", e);
    } finally {
      if (trackerServer != null) {
        try {
          trackerServer.close();
        } catch (IOException e) {
          logger.warn("trackerServer.close warn", e);
        }
      }
    }
  }

  @Override
  public int getPubByFileId(String fileId) {
    TrackerServer trackerServer = null;
    try {
      String pub = "1";
      TrackerClient tracker = new TrackerClient();
      trackerServer = tracker.getConnection();
      StorageClient1 client1 = new StorageClient1(trackerServer, null);
      fileId = AESEncrypt.getInstance().decrypt(fileId);
      NameValuePair[] nameValuePairs = client1.get_metadata1(fileId);
      if (nameValuePairs != null && nameValuePairs.length > 0) {
        for (NameValuePair nameValuePair : nameValuePairs) {
          if ("pub".equals(nameValuePair.getName())) {
            pub = nameValuePair.getValue();
            break;
          }
        }
      }
      return Integer.parseInt(pub);
    } catch (Exception e) {
      logger.error("getPubByFileId error fileName={}", fileId, e);
      throw new DefineException("getPubByFileId exception", e);
    } finally {
      if (trackerServer != null) {
        try {
          trackerServer.close();
        } catch (IOException e) {
          logger.warn("trackerServer.close warn", e);
        }
      }
    }
  }

  @Override
  public int getAppIdByFileId(String fileId) {
    TrackerServer trackerServer = null;
    try {
      String appId = "0";
      TrackerClient tracker = new TrackerClient();
      trackerServer = tracker.getConnection();
      StorageClient1 client1 = new StorageClient1(trackerServer, null);
      fileId = AESEncrypt.getInstance().decrypt(fileId);
      NameValuePair[] nameValuePairs = client1.get_metadata1(fileId);
      if (nameValuePairs != null && nameValuePairs.length > 0) {
        for (NameValuePair nameValuePair : nameValuePairs) {
          if ("userId".equals(nameValuePair.getName())) {
            appId = nameValuePair.getValue();
            break;
          }
        }
      }
      return Integer.parseInt(appId);
    } catch (Exception e) {
      logger.error("getAppIdByFileId error fileName={}", fileId, e);
      throw new DefineException("getAppIdByFileId exception", e);
    } finally {
      if (trackerServer != null) {
        try {
          trackerServer.close();
        } catch (IOException e) {
          logger.warn("trackerServer.close warn", e);
        }
      }
    }
  }

}