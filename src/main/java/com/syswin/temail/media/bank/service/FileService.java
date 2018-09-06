package com.syswin.temail.media.bank.service;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	Map<String,Object> uploadFile(MultipartFile file, Integer pub, String suffix, int userId,
      String domain) throws Exception;

	Map<String,Object> continueUpload(MultipartFile file, Integer pub, String suffix,
      Integer length, String uuid, Integer offset, Integer currentSize, int userId, String domain) throws Exception;

	Map<String,Object> downloadFile(String fileId, String suffix) throws Exception;

	int getPubByFileId(String fileId) throws Exception;

	int getAppIdByFileId(String fileId) throws Exception;
}
