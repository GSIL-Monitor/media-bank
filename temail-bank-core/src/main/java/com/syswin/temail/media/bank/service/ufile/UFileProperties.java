package com.syswin.temail.media.bank.service.ufile;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 姚华成
 * @date 2018-12-12
 */
@Data
@ConfigurationProperties("app.mediabank.ufile")
public class UFileProperties {

  private String bucket;
  private String publicKey;
  private String privateKey;
  private String proxySuffix;
  private String dlProxySuffix;
  private String upProxySuffix;
  private String cdnHost;

  private String tokenPrefix;
  private boolean compatibleFastdfs = false;
}
