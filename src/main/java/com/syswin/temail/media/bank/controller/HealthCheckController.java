package com.syswin.temail.media.bank.controller;

import com.syswin.temail.media.bank.bean.disconf.common.AppInfoBean;
import com.syswin.temail.media.bank.bean.disconf.common.OfficeConvertBean;
import com.syswin.temail.media.bank.filter.AppInfoConfig;
import com.syswin.temail.media.bank.utils.fastdfs.ClientGlobal;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="/", tags="healthcheck接口")
@RestController
public class HealthCheckController {

    @Autowired
    private AppInfoConfig appInfoConfig;

    @Autowired
    private AppInfoBean appInfoBean;

    @Autowired
    OfficeConvertBean officeConvertBean;

    @GetMapping("/healthcheck")
    public ResponseEntity<String> ping(HttpServletRequest request){
        System.out.println("request url: " + request.getRequestURL());
        System.out.println("appInfo: " + appInfoBean.getAppInfoMapStr());
        System.out.println("office url: " + officeConvertBean.getOfficeConvert());
        System.out.println("connect time: " + ClientGlobal.getG_connect_timeout());
        return new ResponseEntity<>("healthcheck is ok", HttpStatus.OK);
    }

}