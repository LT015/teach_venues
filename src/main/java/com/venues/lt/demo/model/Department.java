package com.venues.lt.demo.model;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Department {
    @Id
    private Integer deptId;

    private String deptName;

    private Integer buildingId;

    private Integer higherDeptId;

    private String leaderId;

    private String superManager;

    private String deptPicture;

    private String deptDescribe;

}