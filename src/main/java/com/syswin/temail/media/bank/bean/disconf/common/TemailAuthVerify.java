package com.syswin.temail.media.bank.bean.disconf.common;

import com.systoon.integration.spring.boot.disconf.common.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class TemailAuthVerify {

  @Value("${url.temail.auth.verify}")
  public String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

}
