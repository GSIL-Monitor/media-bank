package com.syswin.temail.media.bank.service;

import javax.servlet.http.HttpServletRequest;

public interface DocumentService {
    byte[] preview(String fileUrl, String suffix, HttpServletRequest request) throws Exception;
}
