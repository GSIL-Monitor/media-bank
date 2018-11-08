package com.syswin.temail.media.bank.utils.stoken;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by 136249 on 2015/2/27.
 */
public class HmacSha1Utils {

    private static final String HMAC_SHA1 = "HmacSHA1";

    public static byte[]  hmacSha1(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        return rawHmac;
    }
 

    public static void main(String[] fwe)throws Exception{
       System.out.println(hmacSha1("qwdew".getBytes(),"qwew".getBytes()));
    }

}
