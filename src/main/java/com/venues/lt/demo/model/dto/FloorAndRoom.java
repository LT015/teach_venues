package com.venues.lt.demo.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="楼层和教室",description="楼层和该楼层包含的教室名字列表")
public class FloorAndRoom {
    private Integer floor;

    private List<String> roomName;
}
