package com.syswin.temail.media.bank.bean.disconf.common;

import com.systoon.integration.spring.boot.disconf.common.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:appInfo.properties")
@RefreshScope
public class AppInfoBean {

    @Value("${appInfoMapStr}")
    private String appInfoMapStr;

    public String getAppInfoMapStr() {
        return appInfoMapStr;
    }

    public void setAppInfoMapStr(String appInfoMapStr) {
        this.appInfoMapStr = appInfoMapStr;
    }
}