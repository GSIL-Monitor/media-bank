package com.syswin.temail.media.bank.bean.disconf.common;

import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileUploadLimite {

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

}
