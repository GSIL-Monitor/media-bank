package com.syswin.temail.media.bank.utils.images;

import java.util.Arrays;
import java.util.List;

public class ImageHelper {

    private final static String[] imageFormatArr = new String[] { "jpg", "png",
            "gif", "webp", "bmp", "tiff" };
    private final static String[] videoFormatArr = new String[] { "jpg", "png",
            "bmp" };

    public static final boolean isFormatSupport(String format) throws Exception {
        // 忽略大小写
        format = format.toLowerCase();
        List<String> list = Arrays.asList(imageFormatArr);
        if (list.contains(format)) {
            return true;
        }
        return false;
    }

    public static final boolean screentFormatSupport(String format)
            throws Exception {
        // 忽略大小写
        format = format.toLowerCase();
        List<String> list = Arrays.asList(videoFormatArr);
        if (list.contains(format)) {
            return true;
        }
        return false;
    }
}
