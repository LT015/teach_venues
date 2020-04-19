package com.venues.lt.demo.service;


public interface LoginService {
    /**
     * 返回登录是否成功
     *
     * @param stuId    学号
     * @param password 密码
     * @return 登录是否成功
     */
    boolean checkUser(String stuId, String password);
}
