//package com.syswin.temail.media.bank.filter;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
//import com.syswin.temail.media.bank.bean.AppInfo;
//import com.syswin.temail.media.bank.config.AppInfoBean;
//import java.util.HashMap;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AppInfoConfig {
//
//	@Autowired
//	private AppInfoBean appInfoBean;
//
//	public HashMap<String,AppInfo> getAppInfo() {
//		Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//		return  GSON.fromJson(appInfoBean.getAppInfoMapStr(), new TypeToken<HashMap<String,AppInfo>>(){}.getType());
//	}
//
//}