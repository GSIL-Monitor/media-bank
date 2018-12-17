package com.syswin.temail.media.bank.service.ufile;

import static com.syswin.temail.media.bank.constants.ResponseCodeConstants.FORBID_ACCESS_ERROR;
import static com.syswin.temail.media.bank.constants.ResponseCodeConstants.NOT_FOUND_ERROR;

import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.FileService;
import com.syswin.temail.media.bank.service.fastdfs.FileServiceImpl;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 姚华成
 * @date 2018-12-17
 */
@Slf4j
public class UFileCompatibleFastdfsService implements FileService {

  private UFileFileService ufileService;
  private FileServiceImpl fastdfsService;

  public UFileCompatibleFastdfsService(UFileFileService ufileService, FileServiceImpl fastdfsService) {
    this.ufileService = ufileService;
    this.fastdfsService = fastdfsService;
  }

  @Override
  public Map<String, Object> uploadFile(MultipartFile file, Integer pub, String suffix, String domain) {
    return ufileService.uploadFile(file, pub, suffix, domain);
  }

  @Override
  public Map<String, Object> continueUpload(MultipartFile file, Integer pub, String suffix, Integer length, String uuid,
      Integer offset, Integer currentSize, String domain) {
    return ufileService.continueUpload(file, pub, suffix, length, uuid, offset, currentSize, domain);
  }

  @Override
  public Map<String, Object> downloadFile(String fileId, String suffix) {
    Map<String, Object> resultMap;
    try {
      resultMap = ufileService.downloadFile(fileId, suffix);
    } catch (DefineException e) {
      if (e.getCode() == FORBID_ACCESS_ERROR || e.getCode() == NOT_FOUND_ERROR) {
        resultMap = fastdfsService.downloadFile(fileId, suffix);
        log.info("Got file from fastdfs，fileId={}, sufiix={}", fileId, suffix);
        return resultMap;
      }
      throw e;
    }
    return resultMap;
  }

  @Override
  public int getPubByFileId(String fileId) {
    return ufileService.getPubByFileId(fileId);
  }

  @Override
  public int getAppIdByFileId(String fileId) {
    return ufileService.getAppIdByFileId(fileId);
  }
}
