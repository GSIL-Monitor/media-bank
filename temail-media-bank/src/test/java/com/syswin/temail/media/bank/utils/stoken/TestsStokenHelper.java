package com.syswin.temail.media.bank.utils.stoken;

import org.junit.Assert;
import org.junit.Test;

public class TestsStokenHelper {

  @Test
  public void StokenHelper() {
    String stoken = "syswin-oss-1-1-0:c3lzd2luLW9zcy0xLTEtMAoxMDAxCjE1NDE0NzUwNzM3MDQKdwo:chJBnzr9qwTreNru46KxcFo900c";
    String x = StokenHelper.defaultStoken();
    System.out.println(x);
  }
}
