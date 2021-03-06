package com.syswin.temail.media.bank.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.syswin.temail.media.bank.constants.ResponseCodeConstants;
import com.syswin.temail.media.bank.exception.DefineException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

  private static final String CLAIMS_KEY = "token";

  private final String key;

  public TokenService(@Value("${app.mediabank.jwt.secretkey:syswin}") String key){
    this.key = key;
  }

  public String generate(String content) {
    return JWT.create().withClaim(CLAIMS_KEY, content)
        .withIssuedAt(new Date())
        .sign(Algorithm.HMAC256(key));
  }

  public boolean verify(String token) {
    try {
      JWTVerifier verifier = JWT.require(Algorithm.HMAC256(key)).build();
      DecodedJWT jwt = verifier.verify(token);
      String content = jwt.getClaim(CLAIMS_KEY).asString();
      // TODO: 2018/11/7 后面需要对比content
      return true;
    } catch (Exception e) {
      logger.error("verify token failed");
      throw new DefineException(ResponseCodeConstants.FORBID_ACCESS_ERROR,
          "The token is a forgery!");
    }
  }
}
