package com.syswin.temail.media.bank.configuration;

import java.io.File;
import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUploadLimiteConfiguration {

  @Value("${spring.servlet.multipart.max-file-size}")
  private String maxFileSize;

  @Value("${spring.servlet.multipart.max-request-size}")
  private String maxRequestSize;

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    //  单个数据大小
    factory.setMaxFileSize(maxFileSize);
    /// 总上传数据大小
    factory.setMaxRequestSize(maxRequestSize);
    String shmPath = "/dev/shm";
    File shm = new File(shmPath);
    if (shm.exists()) {
      factory.setLocation(shmPath + "/uploadtmp");
    }
    return factory.createMultipartConfig();
  }

}
