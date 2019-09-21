package com.mohsinkerai.sample.helpers;

public class CustomException extends RuntimeException {

  private final int code;
  private final String message;

  public CustomException(int errorCode, String message) {
    super(message);
    this.message = message;
    this.code = errorCode;
  }
}
