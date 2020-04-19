package com.venues.lt.demo.model.dto;

import lombok.Data;

@Data
public class TimeTableDto {

    private String courseId;

    private String courseName;

    private String className;

    private String weekly;

    private String section;

    private String roomName;

    //周几
    private Integer weekday;

    //起止节数
    private Integer startNum;

    private Integer endNum;

    private String userName;

}
