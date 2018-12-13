package com.syswin.temail.media.bank.service.ufile;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import cn.ucloud.ufile.UFileSDK;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.utils.AESEncrypt;
import java.util.Map;
import java.util.UUID;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 姚华成
 * @date 2018-12-13
 */

public class UFileFileServiceTest {

  private static final String DOMAIN = "http://www.systoon.com/";
  private static final String TOKEN_PREFIX = "mediabank";
  private UFileFileService fileService;

  @Before
  public void init() {
    if (fileService == null) {
      UFileSDK ufileSDK = new UFileSDK();
      String configPath = ClassLoader.getSystemResource("config.properties").getFile();
      ufileSDK.loadConfig(configPath);
      fileService = new UFileFileService(ufileSDK, TOKEN_PREFIX);
    }
  }

  public void detroy() {
    UFileSDK.shutdown();
  }

  /**
   * 文件成功上传下载
   */
  @Test
  public void testUploadAndDownloadSuccess() {
    byte[] fileContent = "This is file content!".getBytes();
    String contentType = ContentType.TEXT_PLAIN.getMimeType();
    String filename = "/test/data.txt";
    MultipartFile file = new MockMultipartFile("testUpload", filename,
        contentType, fileContent);
    String suffix = ".txt";
    Map<String, Object> uploadMap = fileService.uploadFile(file, 0, suffix, DOMAIN);
    String fileId = String.valueOf(uploadMap.get("fileId"));
    String pubUrl = String.valueOf(uploadMap.get("pubUrl"));
    String uploadETag = String.valueOf(uploadMap.get("ETag"));
    assertNotNull("pubUrl is null", pubUrl);
    assertNotNull("eTag is null", uploadETag);

    Map<String, Object> downloadMap = fileService.downloadFile(fileId, suffix);
    byte[] downloadData = (byte[]) downloadMap.get("file");
    int downloadLength = Integer.parseInt(String.valueOf(downloadMap.get("length")));
    String downloadETag = String.valueOf(uploadMap.get("ETag"));
    assertEquals(fileContent.length, downloadLength);
    assertArrayEquals(fileContent, downloadData);
    assertEquals(uploadETag, downloadETag);
  }

  @Test(expected = DefineException.class)
  public void testInvalidFileId() {
    String suffix = "";
    String fileId = "mediabank/" + UUID.randomUUID().toString();
    fileId = AESEncrypt.getInstance().encrypt(fileId);
    fileService.downloadFile(fileId, suffix);
  }

  @Test(expected = DefineException.class)
  public void testUploadInvalidPrefix() {
    UFileSDK ufileSDK = new UFileSDK();
    String configPath = ClassLoader.getSystemResource("config.properties").getFile();
    ufileSDK.loadConfig(configPath);
    UFileFileService localFileService = new UFileFileService(ufileSDK, UUID.randomUUID().toString());
    byte[] fileContent = "This is file content!".getBytes();
    String contentType = ContentType.TEXT_PLAIN.getMimeType();
    String filename = "/test/data.txt";
    MultipartFile file = new MockMultipartFile("testUpload", filename,
        contentType, fileContent);
    String suffix = ".txt";
    localFileService.uploadFile(file, 0, suffix, DOMAIN);
  }

  @Test(expected = DefineException.class)
  public void testUploadLargeFile() {
    String suffix = "";
    String fileId = "mediabank/" + UUID.randomUUID().toString();
    fileId = AESEncrypt.getInstance().encrypt(fileId);
    fileService.downloadFile(fileId, suffix);
  }
}