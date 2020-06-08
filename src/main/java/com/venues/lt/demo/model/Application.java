package com.venues.lt.demo.model;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value="活动日期")
    private Date date;

    private Integer timeBegin;

    private Integer timeEnd;

    private String roomTarget1;

    private String roomTarget2;

    private String roomTarget3;

    @ApiModelProperty(value="人数")
    private Integer number;

    private String activity;

    @ApiModelProperty(value=" 0 未提交 1部门审批中 2职能部门审批中 3 通过 4拒绝 5 已执行 6 已结束 ")
    private Integer state;

    @ApiModelProperty(value="表示是否使用多媒体 1是 0否")
    private Integer media;

    @ApiModelProperty(value="申请者职工号")
    private String userId;

    private Integer deptId;

    @ApiModelProperty(value="申请日期")
    private Date applyDate;

    private String verify1;

    private String verify2;

    private String execute;

    @ApiModelProperty(value="驳回理由")
    private String reason;

}