package com.syswin.temail.media.bank.service;

import java.util.concurrent.Future;
import javax.servlet.http.HttpServletRequest;

public interface MediaService {

    Future vframe(String fileUrl, String suffix, int width, int height, int offset, String format,
        HttpServletRequest request) throws Exception;
}
