package com.syswin.temail.media.bank.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.systoon.integration.spring.boot.disconf.common.annotation.RefreshScope;
import com.syswin.temail.media.bank.bean.AppInfo;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:appInfo.properties")
@RefreshScope
public class AppInfoBean {

    @Value("${app.mediabank.appInfoMapStr}")
    private String appInfoMapStr;

    public String getAppInfoMapStr() {
        return appInfoMapStr;
    }

    public void setAppInfoMapStr(String appInfoMapStr) {
        this.appInfoMapStr = appInfoMapStr;
    }

    public HashMap<String,AppInfo> getAppInfo() {
        Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return  GSON.fromJson(getAppInfoMapStr(), new TypeToken<HashMap<String,AppInfo>>(){}.getType());
    }

}