package com.syswin.temail.fast.emoji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.syswin.temail","com.syswin.temail.fast.emoji"})
public class FastEmojiApplication {

  public static void main(String[] args) {
    SpringApplication.run(FastEmojiApplication.class, args);
  }

}