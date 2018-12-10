package test;

import core.UFileRequest;
import core.UFileSDK;

/**
 * Put上传测试
 *
 * @author michael
 */
public class UFilePutTest {

  public static void main(String args[]) {
    String configPath = System.getProperty("user.dir") + "\\temail-ufile\\src\\main\\resources\\config.properties";
    //String configPath = UFilePutTest.class.getClassLoader().getResource("config.properties").toString();
    String httpMethod = "PUT";
    String key = "mediabank/test";
    String contentType = "text/plain; charset=utf-8";
    String contentMD5 = "";
    String date = "20181205";
    String filePath = "D:\\test.txt";

    UFileRequest request = new UFileRequest();
    request.setHttpMethod(httpMethod);
    request.setKey(key);
    request.setContentType(contentType);
    request.setContentMD5(contentMD5);
    request.setDate(date);
    request.setFilePath(filePath);

    UFileSDK ufileSDK = new UFileSDK();
    ufileSDK.loadConfig(configPath);

    System.out.println("[Request]\n");
    ufileSDK.put(request, new PrintCallback());

    UFileSDK.shutdown();
  }

}
