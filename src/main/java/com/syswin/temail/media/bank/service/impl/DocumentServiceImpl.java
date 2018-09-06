package com.syswin.temail.media.bank.service.impl;

import com.syswin.temail.media.bank.bean.disconf.common.OfficeConvertBean;
import com.syswin.temail.media.bank.service.DocumentService;
import com.syswin.temail.media.bank.utils.HttpClientUtils;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    OfficeConvertBean officeConvertBean;

    @Override
    public byte[] preview(String fileUrl, String suffix, HttpServletRequest request) throws Exception {
        //拼接请求参数
        String previewUrl = officeConvertBean.getOfficeConvert() + "?download_url=" + URLEncoder.encode(fileUrl, "UTF-8") + "&suffix=" + suffix;
        byte[] previewFile = HttpClientUtils.download(previewUrl, request);
        if(previewFile == null){
            throw new Exception("convert error");
        }
        return previewFile;
    }
}