package com.syswin.temail.media.bank.utils;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;


/**
 * Created by weis on 18/4/17.
 */
public class AESEncrypt {

    private static final String ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String DEFAULTKEY = "yhTmUXaVhKea59k1";

    private AESEncrypt(){

    }
    private volatile static AESEncrypt instance;

    public static AESEncrypt getInstance(){
        if(instance==null){
            synchronized (AESEncrypt.class){
                if(instance==null){
                    instance=new AESEncrypt();
                }
            }
        }
        return instance;
    }

    private SecretKey initkey(String key) throws Exception{
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM); //实例化密钥生成器
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
        secureRandom.setSeed(key.getBytes());
        kg.init(128,secureRandom);         //初始化密钥生成器:AES要求密钥长度为128,192,256位
        SecretKey secretKey = kg.generateKey();                //生成密钥
        return secretKey;  //获取二进制密钥编码形式
    }

    public String encrypt(String source) {
        //return Base64.encodeBase64String(encryptMode(source.getBytes(),DEFAULTKEY));
        return new String(Base64.encodeBase64URLSafe(encryptMode(source.getBytes(),DEFAULTKEY)));
    }

    public String decrypt(String ciphertext) {
        try {
            return new String(decryptMode(Base64.decodeBase64(ciphertext.getBytes()),DEFAULTKEY),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encryptByKey(String source, String key) {
        return Base64.encodeBase64String(encryptMode(source.getBytes(),key));
    }

    public String decryptByKey(String ciphertext, String key) {
        return new String(decryptMode(Base64.decodeBase64(ciphertext.getBytes()),key));
    }

    private byte[] encryptMode(byte[] src,String key){
        try {
            //生成密钥
            SecretKey deskey =  initkey(key);
            //加密
            Cipher c1 = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);//在单一方面的加密或解密
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    private byte[] decryptMode(byte[] src,String key){
        try {
            //生成密钥
            SecretKey deskey =  initkey(key);
            //解密
            Cipher c1 = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(Exception e3){
            e3.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){
        AESEncrypt instance = AESEncrypt.getInstance();
        System.out.println("加密："+instance.encrypt("weisheng"));
        System.out.println("解密："+instance.decrypt(instance.encrypt("weisheng")));
        System.out.println("加密："+instance.encryptByKey("weisheng","123456"));
        System.out.println("解密："+instance.decryptByKey(instance.encryptByKey("weisheng","123456"),"123456"));
    }



}
