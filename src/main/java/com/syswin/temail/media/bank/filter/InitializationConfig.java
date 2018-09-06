package com.syswin.temail.media.bank.filter;

import com.syswin.temail.media.bank.utils.fastdfs.ClientGlobal;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class InitializationConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        ClientGlobal.initByProperties("fastdfs-client.properties");
    }

}