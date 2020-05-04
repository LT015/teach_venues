package com.venues.lt.demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

@Data
public class Room {
    @Id
    private String roomName;

    private Integer floor;

    private String doorNum;

    private Integer capacity;

    private String status;

    private Integer area;

    private Integer deptId;

    private Integer buildingId;

    private String roomManager;

    private String roomType;

    //
    @ApiModelProperty(value="是否是常用教室 0否 1是")
    private Integer use;

}