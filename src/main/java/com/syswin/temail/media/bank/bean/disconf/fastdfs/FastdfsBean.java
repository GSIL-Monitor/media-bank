package com.syswin.temail.media.bank.bean.disconf.fastdfs;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.syswin.temail.media.bank.utils.fastdfs.ClientGlobal;
import org.springframework.stereotype.Component;

@Component
@DisconfUpdateService(confFileKeys = { "fastdfs-client.properties" })
public class FastdfsBean implements IDisconfUpdate {

    @Override
    public void reload() throws Exception {
        ClientGlobal.initByProperties("fastdfs-client.properties");
    }

}