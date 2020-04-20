package com.venues.lt.demo.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class AppFlow {

    @Id
    private Integer applicationId;

    @Id
    private Integer flowId;

    private Date appFlowTime;

}