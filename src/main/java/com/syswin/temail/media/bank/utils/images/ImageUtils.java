package com.syswin.temail.media.bank.utils.images;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syswin.temail.media.bank.bean.ImageInfo;
import com.syswin.temail.media.bank.constants.ImageConstants;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.summercool.gif.GifDecoder;

public class ImageUtils {

    private static final Logger logger = LoggerFactory.getLogger("async-info");

    private final static String[] imageQuality = new String[] { "10", "40",
            "70", "90" };

    public static byte[] scaleImageByGm(int width, int height, double quality, int type, byte[] src, String format) throws Exception {
        ByteArrayInputStream inStream = null;
        try {
            if (src == null || src.length < 13) {
                throw new Exception("Invalid Image");
            }
            String imageType = format;
            String sourceType = getType(src);
            if (StringUtils.isBlank(sourceType)) {
                throw new Exception("Invalid Image");
            }
            if (StringUtils.isBlank(imageType)) {
                imageType = sourceType;
            }
            int picQuality = (int) quality;
            if (width <= 0 || width == Integer.MAX_VALUE) {
                width = 100;
            }
            if (height <= 0 || height == Integer.MAX_VALUE) {
                height = 100;
            }
            ImageInfo info = getImageInfo(src);
            if (info == null) {
                throw new Exception("getImageInfo error");
            }
            if (info.getHeight() * info.getWidth() > ImageConstants.MAX_SOURCE_IMAGE_PIX) {
                throw new Exception("the image is too large");
            }
            if (width * height > ImageConstants.MAX_IMAGE_PIX) {
                //如果和目标格式是一样的，直接就返回
                if (format.equalsIgnoreCase(sourceType)) {
                    return src;
                }
                byte[] arr = null;
                //如果是gif，先抽出第一张图片
                if (sourceType.equalsIgnoreCase("gif")) {
                    arr = cutGif(src);
                    if (arr == null || arr.length == 0) {
                        return src;
                    }
                    src = arr;
                }
                //转换成目标格式
                arr = convertImageByGm(src, format);
                if (arr == null || arr.length == 0) {
                    return src;
                }
                return arr;
            }

            //gif check
            byte[] arr = null;
            if (sourceType.equalsIgnoreCase("gif")) {
                //如果是gif转gif，我们就用gifImage来转
                if (format.equalsIgnoreCase("gif") && type == 0) {
                    inStream = new ByteArrayInputStream(src);
                    arr = GmImage.scaleGif(inStream, format, width, height, picQuality);
                    //gm 转换失败，就使用gifimage
                    if (arr == null || arr.length == 0) {
                        arr = GifImage.scale(src, width, height);
                    }
                    if (arr == null || arr.length == 0) {
                        return src;
                    }
                    return arr;
                } else {
                    //gif转其他格式的
                    arr = cutGifAndScale(src, width, height, format);
                }
            }
            if (arr == null || arr.length == 0) {
                inStream = new ByteArrayInputStream(src);
            } else {
                inStream = new ByteArrayInputStream(arr);
            }
            // 按比例缩放
            if (0 == type) {
                return GmImage.scaleImage(inStream, imageType, width, height, picQuality, false);
            } else if (2 == type) {
                // 按尺寸裁剪
                return GmImage.resizeImage(inStream, imageType, width, height, picQuality);
            }
            int orgWidth = info.getWidth();
            int orgHeight = info.getHeight();

            inStream.reset();
            Integer scaleWidth = null;
            Integer scaleHeight = null;
            boolean isCrop = true;
            // 只压缩质量和缩放
            if (orgWidth <= width && orgHeight <= height && src.length <= ImageConstants.MIN_IMAGE_LENGTH) {
                return src;
            } else if ((orgWidth <= width && orgHeight <= height)
                    || ((orgHeight * 1.000) / orgWidth == (height * 1.000) / width)) {
                scaleWidth = width;
                scaleHeight = height;
                isCrop = false;
            } else if (orgWidth > width && orgHeight > height) {
                scaleWidth = width;
                scaleHeight = height;
            } else if (orgWidth <= width) {
                // 切除高的大小
                scaleWidth = orgWidth;
                scaleHeight = height;
            } else {
                // 切除宽的大小
                scaleWidth = width;
                scaleHeight = orgHeight;
            }
            return GmImage.scaleImage(inStream, imageType, scaleWidth, scaleHeight, picQuality, isCrop);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
    }

    public static byte[] cutGif(byte[] src) {
        ByteArrayInputStream inStream = null;
        try {
            inStream = new ByteArrayInputStream(src);
            //切出一帧gif图片
            byte[] arr = GifImage.cut(src);
            if (arr == null || arr.length == 0) {
                return null;
            }
            return arr;
        } catch (Exception e) {
            logger.error("cutGif", e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
        return null;
    }

    public static byte[] cutGifAndScale(byte[] src, int width, int height, String format) {
        ByteArrayInputStream inStream = null;
        ByteArrayInputStream inStreamConvert = null;
        try {
            inStream = new ByteArrayInputStream(src);
            //切出一帧gif图片
            byte[] arr = GifImage.cut(src);
            if (arr == null || arr.length == 0) {
                return null;
            }
            return arr;
        } catch (Exception e) {
            logger.error("cutGifAndScaleByGm", e);
        } finally {
            IOUtils.closeQuietly(inStream);
            IOUtils.closeQuietly(inStreamConvert);
        }
        return null;
    }

    public static byte[] convertImageByGm(byte[] src, String format) {
        ByteArrayInputStream inStream = null;
        try {
            inStream = new ByteArrayInputStream(src);
            return GmImage.convertImage(inStream, format);
        } catch (Exception e) {
            logger.error("convertImageByGm", e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
        return null;
    }

    public static byte[] cropImageByGm(byte[] in, int x, int y, int x1, int y1, int quality, String format)
            throws Exception {
        // 起始坐标大于结束坐标,原图返回
        if (x >= x1 || y >= y1) {
            return in;
        }
        // 查看图片信息
        ImageInfo info = getImageInfo(in);
        if (info == null) {
            return null;
        }
        int srcHeight = info.getHeight();
        int srcWidth = info.getWidth();
        if (x > srcWidth || y > srcHeight) {
            return in;
        }
        if (srcHeight * srcWidth > ImageConstants.MAX_SOURCE_IMAGE_PIX) {
            throw new Exception("the image is too large");
        }
        x1 = x1 > srcWidth ? srcWidth : x1;
        y1 = y1 > srcHeight ? srcHeight : y1;
        if(StringUtils.isBlank(format)){
            format = getType(in);
        }
        byte[] ret = GmImage.cropImage(in, format, x, y, x1, y1, quality);
        return ret;
    }

    public static byte[] scaleVframeImage(int width, int height, double quality, byte[] src) throws Exception {
        String imageType = getType(src);
        int picQuality = (int) (quality * 100.0f);
        if (picQuality > 0 && picQuality <= 100) {
            // 最高设置为85
            if (picQuality > 85) {
                picQuality = 85;
            }
        } else {
            picQuality = 70;
        }
        if (width <= 0 || width == Integer.MAX_VALUE) {
            width = 100;
        }
        if (height <= 0 || height == Integer.MAX_VALUE) {
            height = 100;
        }
        ByteArrayInputStream inStream = null;
        try {
            inStream = new ByteArrayInputStream(src);
            // 按比例缩放
            return GmImage.scaleImage(inStream, imageType, width, height, picQuality, false);
        } catch (Exception e) {
            logger.error("scaleImageByGM", e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }
        return null;
    }

    public static ImageInfo getImageInfo(byte[] src) {
        ByteArrayInputStream inStream = null;
        FileOutputStream fos = null;
        BufferedImage img = null;
        int orgWidth = 0;
        int orgHeight = 0;
        File file = null;
        try {
            ImageInfo imageInfo = new ImageInfo();
            try {
                String imageType = getType(src);
                if (StringUtils.isBlank(imageType) || imageType.equals("Unknown")) {
                    return null;
                }
                inStream = new ByteArrayInputStream(src);
                if (imageType.equals("GIF")) {
                    GifDecoder gd = new GifDecoder();
                    int status = gd.read(inStream);
                    if (status == GifDecoder.STATUS_OK) {
                        img = gd.getImage();
                    }
                    inStream.reset();
                }
                if (img == null) {
                    img = ImageIO.read(inStream);
                }
            } catch (Exception e) {
                logger.error("getImageInfo", e);
            }
            if (img != null) {
                orgWidth = img.getWidth();
                orgHeight = img.getHeight();
            } else {
                file = new File(FileUtils.getTempDirectoryPath() + "/" + UUID.randomUUID() + "imageinfo");
                fos = new FileOutputStream(file.getPath());
                IOUtils.write(src, fos);
                String jsonStr = GmImage.showImageInfo(file.getPath());
                if (StringUtils.isBlank(jsonStr)) {
                    return null;
                }
                JSONObject info = JSON.parseObject(jsonStr);
                orgWidth = info.getIntValue("width");
                orgHeight = info.getIntValue("height");
            }
            imageInfo.setWidth(orgWidth);
            imageInfo.setHeight(orgHeight);
            return imageInfo;
        } catch (Exception e) {
            logger.error("getImageInfo", e);
        } finally {
            IOUtils.closeQuietly(inStream);
            IOUtils.closeQuietly(fos);
            FileUtils.deleteQuietly(file);
            if (img != null) {
                img = null;
            }
        }
        return null;
    }

    public static String getType(byte[] src) {
        if(src == null || src.length < 12){
            return null;
        }
        String type = null;
        byte b0 = src[0];
        byte b1 = src[1];
        byte b2 = src[2];
        byte b3 = src[3];
        byte b6 = src[6];
        byte b7 = src[7];
        byte b8 = src[8];
        byte b9 = src[9];
        byte b10 = src[10];
        byte b11 = src[11];
        //GIF
        if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F') {
            type = "GIF";
        }
        // PNG
        else if (b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G') {
            type = "PNG";
        }
        // JPG
        else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I' && b9 == (byte) 'F') {
            type = "JPG";
        } else if (b6 == (byte) 'E' && b7 == (byte) 'x' && b8 == (byte) 'i' && b9 == (byte) 'f') {
            type = "JPG";
        } else if (b8 == (byte) 'W' && b9 == (byte) 'E' && b10 == (byte) 'B' && b11 == (byte) 'P') {
            type = "WEBP";
        } else if (b0 == (byte) 'B' && b1 == (byte) 'M') {
            type = "bmp";
        } else if (b0 == (byte) 'I' && b1 == (byte) 'I') {
            type = "tif";
        } else if (b0 == -1 && b1 == -40) {
            byte last1 = src[src.length - 2];
            byte last2 = src[src.length - 1];
            if (last1 == -1 && last2 == -39) {
                type = "JPG";
            }
        }
        return type;
    }

    /**
     * 根据质量等级获取图片质量
     *
     */
    public static final int level2Quality(int level, String format)
            throws Exception {
        int len = imageQuality.length;
        // 其他参数, 返回默认质量
        if (level < 0 || level > len - 1) {
            return Integer.parseInt(imageQuality[2]);
        }
        if ("PNG".equalsIgnoreCase(format) || "TIFF".equalsIgnoreCase(format)) {
            return Integer.parseInt(imageQuality[len - level - 1]);
        } else {
            return Integer.parseInt(imageQuality[level]);
        }
    }
}
