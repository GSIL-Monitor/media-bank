package com.syswin.temail.media.bank.service.fastdfs;

import com.syswin.temail.media.bank.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author 姚华成
 * @date 2018-12-14
 */
@Slf4j
@Configuration
@Profile("fastdfs")
public class FastdfsConfiguration {

  @Bean
//  @ConditionalOnProperty(value = "app.mediabank.dfs", havingValue = "fastdfs", matchIfMissing = true)
  public FileService fileService() {
    log.info("------------------------------This mediabank use fastdfs storage!------------------------------");
    return new FileServiceImpl();
  }
}
