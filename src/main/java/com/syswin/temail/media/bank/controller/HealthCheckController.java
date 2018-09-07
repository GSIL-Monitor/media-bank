package com.syswin.temail.media.bank.controller;

import com.systoon.integration.spring.boot.disconf.common.annotation.RefreshScope;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="/", tags="healthcheck接口")
@RestController
@RefreshScope
public class HealthCheckController {

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthcheck(){
        return new ResponseEntity<>("healthcheck is ok", HttpStatus.OK);
    }

}