package com.syswin.temail.media.bank.service.ufile;

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
//  @ConditionalOnProperty(value = "app.mediabank.dfs", havingValue = "ufile")
  public UFileFileService fileService(UFileProperties properties) {
    log.info("------------------------------This mediabank use UFile storage!------------------------------");
    return new UFileFileService(properties);
  }

}
