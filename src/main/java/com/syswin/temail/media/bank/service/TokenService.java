package com.syswin.temail.media.bank.service;

public interface TokenService {

  String generate(String content);

  boolean verify(String token);
}
