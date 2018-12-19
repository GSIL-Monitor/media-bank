package com.syswin.temail.media.bank.configuration;

import java.io.File;
import java.io.IOException;
import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

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
    return factory.createMultipartConfig();
  }

  @Bean
  public MultipartResolver multipartResolver() throws IOException {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    String shmPath = "/dev/shm";
    File shm = new File(shmPath);
    if (shm.exists()) {
      multipartResolver.setUploadTempDir(new FileSystemResource(shmPath + "/uploadtmp"));
    }
    return multipartResolver;
  }

}
