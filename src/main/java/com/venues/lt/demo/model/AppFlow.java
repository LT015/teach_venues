package com.venues.lt.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class AppFlow {
    private Integer applicationId;

    private Integer flowId;

    private Date appFlowTime;

}