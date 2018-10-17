package com.syswin.temail.media.bank.exception;

public class DefineException extends RuntimeException {

  private int code;
  private String msg;

  public DefineException() {
    super();
  }

  public DefineException(String message) {
    super(message);
  }

  public DefineException(String message, Throwable cause) {
    super(message, cause);
  }

  public DefineException(Throwable cause) {
    super(cause);
  }

  protected DefineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public DefineException(int code, String msg) {
    super(msg);
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}