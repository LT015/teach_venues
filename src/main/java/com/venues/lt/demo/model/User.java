package com.venues.lt.demo.model;

import lombok.Data;

import javax.persistence.Id;

@Data
public class User {
    @Id
    private String userId;

    private String userName;

    private String password;

    private String idNumber;

    private String sex;

    private Integer deptId;

    private String userTitle;

    private String position;

    private String phone;

    private String email;

    private String wechat;

    private Integer status;

}