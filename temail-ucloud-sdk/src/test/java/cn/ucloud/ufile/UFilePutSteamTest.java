package cn.ucloud.ufile;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;

/**
 * Put上传测试
 *
 * @author michael
 */
public class UFilePutSteamTest {

  public static void main(String args[]) throws Exception {
    String configPath = ClassLoader.getSystemResource("config.properties").getFile();
    byte[] fileContent = "This is file content!2".getBytes();
    UFileSDK ufileSDK = new UFileSDK();
    ufileSDK.loadConfig(configPath);
    String httpMethod = "PUT";
    String key = "mediabank/test";
    String contentType = "text/plain";
    String contentMD5 = HexUtil.encodeHex(MessageDigest.getInstance("MD5").digest(fileContent));
    String date = "20181213";

    UFileRequest request = new UFileRequest();
    request.setHttpMethod(httpMethod);
    request.setKey(key);
    request.setContentType(contentType);
    request.setContentMD5(contentMD5);
    request.setDate(date);

    System.out.println("[Request]\n");
    ufileSDK.putStream(request, new PrintCallback(), new ByteArrayInputStream(fileContent));

    UFileSDK.shutdown();
  }

}
