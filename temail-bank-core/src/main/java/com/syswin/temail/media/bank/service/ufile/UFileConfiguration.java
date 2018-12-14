package com.syswin.temail.media.bank.service.ufile;

import cn.ucloud.ufile.UFileSDK;
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
@Profile("ufile")
@Configuration
@EnableConfigurationProperties(UFileProperties.class)
public class UFileConfiguration {

  @Bean
//  @ConditionalOnProperty(value = "app.mediabank.dfs", havingValue = "ufile")
  public UFileFileService fileService(UFileProperties properties) {
    log.info("------------------------------This mediabank use UFile storage!------------------------------");
    return new UFileFileService(ufileSDK(properties), properties.getTokenPrefix());
  }

  private UFileSDK ufileSDK(UFileProperties properties) {
    UFileSDK ufileSDK = new UFileSDK();
    if (properties.getCdnHost() != null && !properties.getCdnHost().isEmpty()) {
      ufileSDK.initCDN(properties.getBucket(), properties.getCdnHost(), properties.getPublicKey(),
          properties.getPrivateKey());
    } else if (properties.getUpProxySuffix() != null && !properties.getUpProxySuffix().isEmpty()) {
      ufileSDK.initGlobal(properties.getBucket(), properties.getUpProxySuffix(), properties.getDlProxySuffix(),
          properties.getPublicKey(), properties.getPrivateKey());
    } else {
      ufileSDK.init(properties.getBucket(), properties.getProxySuffix(), properties.getPublicKey(),
          properties.getPrivateKey());
    }
    return ufileSDK;
  }

}
