package com.venues.lt.demo.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="TimetableDto",description="通过教室名获取的课表对象")
public class TimetableDto {

    private String courseId;

    private String courseName;

    private String weekly;

    private String section;

    private String roomName;

    //周几
    @ApiModelProperty(value="表示周几 比如周六  这里就是6")
    private Integer weekday;

    //起止节数
    @ApiModelProperty(value="表示开始的节数 比如第二单元是3-4节  这里就是3")
    private Integer startNum;

    @ApiModelProperty(value="表示结束的节数")
    private Integer endNum;

    // 表示是否单双周 0是无单双 1是单 2是双
    @ApiModelProperty(value="表示是否单双周 0是无单双 1是单 2是双")
    private Integer flag;

    private String userName;

}
