package com.syswin.temail.media.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.syswin.temail")
public class MediaBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(MediaBankApplication.class, args);
  }

}