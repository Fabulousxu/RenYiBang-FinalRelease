package com.renyibang.global.util;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class Response {
  private boolean ok;
  private String message;
  private Object data;

  public static Response success(String message, Object data) {
    Response response = new Response();
    response.setOk(true);
    response.setMessage("");
    response.setData(data);
    return response;
  }

  public static Response success(String message) {
    return success(message, new JSONObject());
  }

  public static Response success(Object data) {
    return success("", data);
  }

  public static Response success() {
    return success("", new JSONObject());
  }

  public static Response error(String message) {
    Response response = new Response();
    response.setOk(false);
    response.setMessage(message);
    response.setData(new JSONObject());
    return response;
  }

  public static Response error() {
    return error("");
  }
}
