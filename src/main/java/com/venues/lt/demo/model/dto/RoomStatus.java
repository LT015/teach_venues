package com.venues.lt.demo.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="RoomStatus",description="获取教室状态")
public class RoomStatus {
    private String roomName;

    private Integer floor;

    private Integer capacity;

    @ApiModelProperty(value="包括未占用和已占用")
    private String status;

    private Integer area;

    private String deptName;

    private String buildingName;

    @ApiModelProperty(value="如果被管理员占用 会有占用id 便于取消")
    private Integer Occupation;

}
