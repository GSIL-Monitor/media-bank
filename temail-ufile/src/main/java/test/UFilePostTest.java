package test;


import core.UFileRequest;
import core.UFileSDK;

/**
 * Post上传测试
 *
 * @author michael
 */
public class UFilePostTest {

  public static void main(String args[]) {
    String configPath = System.getProperty("user.dir") + "\\temail-ufile\\src\\main\\resources\\config.properties";

    String httpMethod = "POST";
    String key = "mediabank/test22.txt";
    String contentType = "multipart/form-data";
    String contentMD5 = "";
    String date = "";
    String filePath = "E:\\22.txt";

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
    ufileSDK.post(request, new PrintCallback());

    UFileSDK.shutdown();
  }

}
