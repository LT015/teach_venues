package com.venues.lt.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoomDto {
    private String roomName;

    private Integer floor;

    private String doorNum;

    private Integer capacity;

    private String status;

    private Integer area;

    private Integer deptId;

    private String deptName;

    private Integer buildingId;

    private String buildingName;

    private String roomManager;

    @ApiModelProperty(value="是否常用")
    private Integer use;

}
