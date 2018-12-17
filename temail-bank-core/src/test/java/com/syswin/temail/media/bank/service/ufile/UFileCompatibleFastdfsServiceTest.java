package com.syswin.temail.media.bank.service.ufile;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.syswin.temail.media.bank.service.fastdfs.FileServiceImpl;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.apache.http.entity.ContentType;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 姚华成
 * @date 2018-12-17
 */
public class UFileCompatibleFastdfsServiceTest {

  private static final String DOMAIN = "http://www.systoon.com/";
  private UFileCompatibleFastdfsService fileService;

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
    String tokenPrefix = "mediabank";
    fileProperties.setTokenPrefix(tokenPrefix);
    fileProperties.setCompatibleFastdfs(true);
    fileService = new UFileCompatibleFastdfsService(new UFileFileService(fileProperties), new FileServiceImpl());
  }

  @Test
  public void testCompatibleFastdfs() throws IOException, MyException {
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
