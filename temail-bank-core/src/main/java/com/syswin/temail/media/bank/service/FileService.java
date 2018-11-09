package com.syswin.temail.media.bank.service;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  Map<String, Object> uploadFile(MultipartFile file, Integer pub, String suffix, String domain);

  Map<String, Object> continueUpload(MultipartFile file, Integer pub, String suffix,
      Integer length, String uuid, Integer offset, Integer currentSize, String domain);

  Map<String, Object> downloadFile(String fileId, String suffix);

  int getPubByFileId(String fileId);

  int getAppIdByFileId(String fileId);
}
