package com.syswin.temail.media.bank.service.ufile;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import cn.ucloud.ufile.UFileSDK;
import com.syswin.temail.media.bank.exception.DefineException;
import com.syswin.temail.media.bank.service.fastdfs.FileServiceImpl;
import com.syswin.temail.media.bank.utils.AESEncrypt;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 姚华成
 * @date 2018-12-13
 */
@Slf4j
public class UFileFileServiceTest {

  private static final String DOMAIN = "http://www.systoon.com/";
  private String tokenPrefix = "mediabank";
  private UFileFileService fileService;
  private HttpClient httpClient;

  @Before
  public void init() throws IOException {
    Properties properties = new Properties();
    properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));
    UFileProperties fileProperties = new UFileProperties();
    fileProperties.setBucket(properties.getProperty("Bucket"));
    fileProperties.setPublicKey(properties.getProperty("UCloudPublicKey"));
    fileProperties.setPrivateKey(properties.getProperty("UCloudPrivateKey"));
    fileProperties.setProxySuffix(properties.getProperty("ProxySuffix"));
    fileProperties.setDlProxySuffix(properties.getProperty("GlobalDownloadProxySuffix"));
    fileProperties.setUpProxySuffix(properties.getProperty("GlobalUploadProxySuffix"));
    fileProperties.setCdnHost(properties.getProperty("CDNHost"));
    fileProperties.setTokenPrefix(tokenPrefix);
    fileService = new UFileFileService(fileProperties);
    if (httpClient == null) {
      httpClient = HttpClients.createDefault();
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

  @Test
  public void testUploadAndPubDownloadSuccess() throws IOException {
    byte[] fileContent = "This is file content!".getBytes();
    String contentType = ContentType.TEXT_PLAIN.getMimeType();
    String filename = "/test/data.txt";
    MultipartFile file = new MockMultipartFile("testUpload", filename,
        contentType, fileContent);
    String suffix = ".txt";
    Map<String, Object> uploadMap = fileService.uploadFile(file, 0, suffix, DOMAIN);
    String pubUrl = String.valueOf(uploadMap.get("pubUrl"));
    String uploadETag = String.valueOf(uploadMap.get("ETag"));
    assertNotNull("pubUrl is null", pubUrl);
    assertNotNull("eTag is null", uploadETag);

    HttpGet request = new HttpGet(pubUrl);
    HttpResponse response = httpClient.execute(request);
    assertEquals(200, response.getStatusLine().getStatusCode());
    byte[] downloadData = IOUtils.toByteArray(response.getEntity().getContent());
    long downloadLength =response.getEntity().getContentLength();
    assertEquals(fileContent.length, downloadLength);
    assertArrayEquals(fileContent, downloadData);
  }

  @Test(expected = DefineException.class)
  public void testInvalidFileId() {
    String suffix = "";
    String fileId = tokenPrefix + "/" + UUID.randomUUID().toString();
    fileId = AESEncrypt.getInstance().encrypt(fileId);
    fileService.downloadFile(fileId, suffix);
  }

  @Test(expected = DefineException.class)
  public void testInvalidProfix() {
    String suffix = "";
    String fileId = UUID.randomUUID().toString() + "/" + UUID.randomUUID().toString();
    fileId = AESEncrypt.getInstance().encrypt(fileId);
    fileService.downloadFile(fileId, suffix);
  }

  @Test(expected = DefineException.class)
  public void testUploadInvalidPrefix() throws IOException {
    tokenPrefix = UUID.randomUUID().toString();
    init();
    byte[] fileContent = "This is file content!".getBytes();
    String contentType = ContentType.TEXT_PLAIN.getMimeType();
    String filename = "/test/data.txt";
    MultipartFile file = new MockMultipartFile("testUpload", filename,
        contentType, fileContent);
    String suffix = ".txt";
    fileService.uploadFile(file, 0, suffix, DOMAIN);
  }

  @Test(expected = DefineException.class)
  public void testIncompatibleFastdfs() throws IOException, MyException {
    init();
    ClientGlobal.initByProperties("fastdfs-client.properties");

    // 先向fastdfs上传一个文件
    FileServiceImpl fileService1 = new FileServiceImpl();
    byte[] fileContent = "This is file content!".getBytes();
    String contentType = ContentType.TEXT_PLAIN.getMimeType();
    String filename = "/test/data.txt";
    MultipartFile file = new MockMultipartFile("testUpload", filename,
        contentType, fileContent);
    String suffix = ".txt";
    Map<String, Object> uploadMap = fileService1.uploadFile(file, 0, suffix, DOMAIN);
    String fileId = String.valueOf(uploadMap.get("fileId"));

    Map<String, Object> downloadMap = fileService.downloadFile(fileId, suffix);
    byte[] downloadData = (byte[]) downloadMap.get("file");
    int downloadLength = Integer.parseInt(String.valueOf(downloadMap.get("length")));
    assertEquals(fileContent.length, downloadLength);
    assertArrayEquals(fileContent, downloadData);
  }

}