package com.syswin.temail.media.bank.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger("async-info");
    private static RequestConfig config = RequestConfig.custom().setConnectTimeout(3000).build();

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

    /**
     * form表单的post请求
     * */
    public static boolean httpPostWithForm(String url, JSONObject params) throws Exception {
        // 创建默认的HttpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 定义一个get请求方法
            HttpPost httppost = new HttpPost(url);
            httppost.setConfig(config);
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            httppost.setHeader("Accept", "application/json,text/plain,*/*");
            List<BasicNameValuePair> list = new ArrayList<>();
            for (String key : params.keySet()) {
                Object value = params.get(key);
                list.add(new BasicNameValuePair(key, value.toString()));
            }
            httppost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
            // 执行post请求，返回response服务器响应对象, 其中包含了状态信息和服务器返回的数据
            CloseableHttpResponse httpResponse = httpclient.execute(httppost);
            // 使用响应对象, 获得状态码, 处理内容
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            // 使用响应对象获取响应实体
            HttpEntity entity = httpResponse.getEntity();
            // 将响应实体转为字符串
            String result = null;
            if(entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            if (statusCode == ResponseCodeConstants.SUCCESS) {
                JSONObject jsonObject = JSON.parseObject(result);
                if(jsonObject.getInteger("code") == ResponseCodeConstants.SUCCESS){
                    return true;
                }else{
                    throw new DefineException(ResponseCodeConstants.AUTO_ERROR, jsonObject.getString("message"));
                }
            } else {
                logger.error("check signature error, response code:" + statusCode + ", reason:"+ result);
                throw new DefineException(statusCode, result);
            }
        } finally {
            IOUtils.closeQuietly(httpclient);
        }
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