package com.renyibang.orderapi.util;

import com.alibaba.fastjson2.JSONObject;

public class ResponseUtil {
  private static JSONObject response(boolean ok, String message, Object data) {
    JSONObject json = new JSONObject();
    json.put("ok", ok);
    json.put("message", message);
    json.put("data", data);
    return json;
  }

  public static JSONObject success(String message, Object data) {
    return response(true, message, data);
  }

  public static JSONObject success(String message) {
    return response(true, message, new JSONObject());
  }

  public static JSONObject success(Object data) {
    return response(true, "", data);
  }

  public static JSONObject success() {
    return response(true, "", new JSONObject());
  }

  public static JSONObject error(String message) {
    return response(false, message, new JSONObject());
  }

  public static JSONObject error() {
    return response(false, "", new JSONObject());
  }
}
