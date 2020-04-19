package com.venues.lt.demo.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 工具类
 *
 */
public class PubUtil {
    /**
     * 从request中获取sessionId
     *
     * @param request 用于获取sessionId
     * @return sessionId
     */
    public static String getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if ("TJUFE-SESSION-ID".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    /**
     * 响应未登录
     *
     * @param response 用于写回响应体
     */
    public static void responseUnAuthorizedError(HttpServletResponse response) {
        Map<String, Object> errorMap = new HashMap<>(4);
        errorMap.put("code", 401);
        errorMap.put("message", "未登录");
        JSONObject jsonObject = new JSONObject(errorMap);
        try {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应访问过于频繁
     *
     * @param response 用于写回响应体
     */
    public static void responseAccessTooOftenError(HttpServletResponse response) {
        Map<String, Object> errorMap = new HashMap<>(4);
        errorMap.put("code", 503);
        errorMap.put("message", "接口访问过于频繁");
        JSONObject jsonObject = new JSONObject(errorMap);
        try {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /**
     * 响应文件上传失败
     *
     * @param response 用于写回响应体
     */
    public static void responseFileUploadFailedError(HttpServletResponse response) {
        Map<String, Object> errorMap = new HashMap<>(4);
        errorMap.put("code", 500);
        errorMap.put("message", "文件上传失败");
        JSONObject jsonObject = new JSONObject(errorMap);
        try {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }













}
