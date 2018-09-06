package com.syswin.temail.media.bank.utils;

import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger("async-info");
    private static RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).build();

    /***
     *
     * 校验下载地址
     */
    public static boolean checkUrl(String fileUrl){
        if(StringUtils.isBlank(fileUrl)){
            return false;
        }
        return true;
    }

    /***
     *
     * 下载文件
     */
    public static byte[] download(String fileUrl, HttpServletRequest request) {
        CloseableHttpClient client = HttpClients.createDefault();
        InputStream is = null;
        fileUrl = fileUrl.replace(" ", "%20").replace("+", "%2B");
        HttpGet httpGet = new HttpGet(fileUrl);
        httpGet.setConfig(config);
        String stoken = request.getHeader("stoken");
        if(StringUtils.isNotBlank(stoken))
            httpGet.setHeader("stoken", stoken);
        try {
            CloseableHttpResponse httpResponse = client.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == ResponseCodeConstants.SUCCESS) {
                HttpEntity entity = httpResponse.getEntity();
                if(entity != null){
                    is = entity.getContent();
                    return IOUtils.toByteArray(is);
                }
            }
        } catch (Exception e) {
            logger.error("download error, " + fileUrl, e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(client);
        }
        return null;
    }

    public static String getDomainByUrl(String interfaceName, HttpServletRequest request){
        String fullUrl = request.getRequestURL().toString();
        Pattern p = Pattern.compile("^(http[s]?:.*/)" + interfaceName + ".*");
        Matcher m = p.matcher(fullUrl);
        while (m.find()) {
            return m.group(1);
        }
        return null;
    }

}