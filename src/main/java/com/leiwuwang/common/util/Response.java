package com.leiwuwang.common.util;


import java.io.Serializable;

public class Response
  implements Serializable
{
  private int code;
  private String message;
  private Object returnValue;

  public int getCode()
  {
    return this.code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getReturnValue() {
    return this.returnValue;
  }

  public void setReturnValue(Object returnValue) {
    this.returnValue = returnValue;
  }
}
