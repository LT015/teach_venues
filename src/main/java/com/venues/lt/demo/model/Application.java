package com.venues.lt.demo.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Application {
    @Id
    private Integer application;

    private String roomName;

    private Integer year;

    private Integer term;

    private Date date;

    private Integer timeBegin;

    private Integer timeEnd;

    private String roomTarget1;

    private String roomTarget2;

    private String roomTarget3;

    private String activity;

    private Integer state;

    private String userId;

    private String deptId;

    private String verify1;

    private String verify2;

    private String execute;

    private String reason;

}