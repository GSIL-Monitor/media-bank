package com.syswin.temail.media.bank.config;

import com.systoon.integration.spring.boot.disconf.common.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class TemailAuthVerify {

  @Value("${url.temail.auth.verify}")
  public String url;

  @Value("${stoken.auth.verify.switch}")
  public boolean verifySwitch;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean getVerifySwitch() {
    return verifySwitch;
  }

  public void setVerifySwitch(boolean verifySwitch) {
    this.verifySwitch = verifySwitch;
  }
}
