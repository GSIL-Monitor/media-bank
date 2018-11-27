package com.syswin.temail.media.bank.service.impl;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.syswin.temail.media.bank.service.DocumentService;
import com.syswin.temail.media.bank.utils.HttpClientUtils;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Override
    public byte[] preview(String officeConvertUrl, String fileUrl, String suffix, HttpServletRequest request) throws Exception {
        //拼接请求参数
        String previewUrl = officeConvertUrl + "?download_url=" + URLEncoder.encode(fileUrl, "UTF-8") + "&suffix=" + suffix;
        byte[] previewFile = HttpClientUtils.download(previewUrl, request);
        if(previewFile == null){
            throw new Exception("convert error");
        }
        return previewFile;
    }
}