package com.syswin.temail.media.bank.service;

import java.util.concurrent.Future;
import javax.servlet.http.HttpServletRequest;

public interface ImageService {
    Future thumbnail(String fileUrl, int width, int height, double quality, int type, String format,
        HttpServletRequest request) throws Exception;

    Future crop(String fileUrl, int x, int y, int x1, int y1, int quality, String format,
        HttpServletRequest request) throws Exception;
}
