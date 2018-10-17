package com.syswin.temail.media.bank.utils.stoken;

import com.syswin.temail.media.bank.exception.DefineException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StokenHelper {

  private static final Logger logger = LoggerFactory.getLogger(StokenHelper.class);

  public static String getStoken(int appId, String appSecret)
      throws InvalidKeyException, UnsupportedEncodingException,
      NoSuchAlgorithmException {
    SecurityToken securityToken = new SecurityToken();
    String auth = "w";
    String stoid = "";
    long ts = System.currentTimeMillis();
    int independence = 0;
    String version = "1";
    String product = "oss";
    String company = "syswin";
    securityToken.initObjectSecurityToken(company, product, version,
        independence, appId, ts, auth, stoid, appSecret);
    String stoken = securityToken.getSecurityToken();
    return stoken;
  }

  public static String defaultStoken() {
    try {
      return getStoken(1001, "123456");
    } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
      logger.error("defaultStoken exception", e);
      throw new DefineException("defaultStoken exception", e);
    }
  }

}