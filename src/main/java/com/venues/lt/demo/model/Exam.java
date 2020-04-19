package com.venues.lt.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class Exam {
    private String examId;

    private Date startTime;

    private Date endTime;

    private String examName;

    private String courseName;

    private Integer year;

    private Integer term;

    private Date occupationDate;

    private String roomName;

    private String className;

    private Integer activityNum;

    private String userId;

}