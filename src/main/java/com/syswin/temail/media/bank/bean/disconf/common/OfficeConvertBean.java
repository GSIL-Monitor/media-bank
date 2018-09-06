package com.syswin.temail.media.bank.bean.disconf.common;

import com.systoon.integration.spring.boot.disconf.common.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:officeConvert.properties")
@RefreshScope
public class OfficeConvertBean {

    @Value("${officeConvert}")
    private String officeConvert;

    public String getOfficeConvert() {
        return officeConvert;
    }

    public void setOfficeConvert(String officeConvert) {
        this.officeConvert = officeConvert;
    }
}
