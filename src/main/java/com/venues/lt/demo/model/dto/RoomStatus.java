package com.venues.lt.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoomStatus {
    private String roomName;

    private Integer floor;

    private Integer capacity;

    private String status;

    private Integer area;

    private String deptName;

    private String buildingName;

    @ApiModelProperty(value="如果被管理员占用 会有占用id 便于取消")
    private Integer Occupation;

}
