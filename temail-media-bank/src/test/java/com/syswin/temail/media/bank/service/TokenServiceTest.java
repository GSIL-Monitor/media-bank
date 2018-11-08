package com.syswin.temail.media.bank.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.syswin.temail.media.bank.service.impl.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

public class TokenServiceTest {

  private static final String key = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

  private TokenService tokenService;

  @Before
  public void setUp() throws Exception {
    tokenService = new TokenService(key);
  }

  @Test
  public void generateToken() throws Exception {
    String token = tokenService.generate("syswin");
    System.out.println("token is :" + token);
  }

  @Test
  public void shouldVerifyToken() throws Exception {
    String token = tokenService.generate("syswin");
    System.out.println(token);
    boolean result = tokenService.verify(token);
    assertThat(result).isEqualTo(true);
  }
}
