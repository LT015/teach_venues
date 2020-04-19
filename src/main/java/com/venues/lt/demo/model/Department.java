package com.venues.lt.demo.model;

import lombok.Data;

@Data
public class Department {
    private Integer deptId;

    private String deptName;

    private Integer buildingId;

    private Integer higherDeptId;

    private String leaderId;

    private String superManager;

    private String deptPicture;

    private String deptDescribe;

}