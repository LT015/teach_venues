package com.venues.lt.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class Application {
    private Integer application;

    private String roomName;

    private Integer year;

    private Integer term;

    private Date date;

    private Date timeBegin;

    private Date timeEnd;

    private String roomTarget1;

    private String roomTarget2;

    private String roomTarget3;

    private String activity;

    private Integer noticeState;

    private String applicationUser;

    private String applicationDept;

}