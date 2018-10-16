package com.syswin.temail.media.bank.configuration;

import org.csource.fastdfs.ClientGlobal;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class FastdfsConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        ClientGlobal.initByProperties("fastdfs-client.properties");
    }

}