package com.syswin.temail.media.bank.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtHelperTest {

  @Autowired
  private TokenService tokenService;

  @Test
  public void generateToken() throws Exception {
    String token = tokenService.generate("syswin");
    System.out.println("token is : " + token);
  }

  @Test
  public void shouldVerifyToken() throws Exception {
    String token = tokenService.generate("syswin");
    boolean result = tokenService.verify(token);
    assertThat(result).isEqualTo(true);
  }
}
