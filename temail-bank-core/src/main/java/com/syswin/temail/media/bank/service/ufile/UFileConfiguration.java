package com.syswin.temail.media.bank.service.ufile;

import com.syswin.temail.media.bank.service.FileService;
import com.syswin.temail.media.bank.service.fastdfs.FileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author 姚华成
 * @date 2018-12-12
 */
@Slf4j
@Configuration
@Profile("ufile")
@EnableConfigurationProperties(UFileProperties.class)
public class UFileConfiguration {

  @Bean
  public FileService fileService(UFileProperties properties) {
    log.info("------------------------------This mediabank use UFile storage!------------------------------");
    if (properties.isCompatibleFastdfs()) {
      return new UFileCompatibleFastdfsService(new UFileFileService(properties), new FileServiceImpl());
    } else {
      return new UFileFileService(properties);
    }
  }

}
