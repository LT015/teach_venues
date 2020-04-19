package com.venues.lt.demo.model;

import lombok.Data;

@Data
public class Timetable {
    private String courseId;

    private String courseName;

    private String className;

    private String weekly;

    private String section;

    private String roomName;

    private Integer year;

    private Integer term;

    private Integer studentNum;

    private String userId;

    private String userName;

    private String userTitle;

    private Integer weekBegin;

    private Integer weekEnd;

}