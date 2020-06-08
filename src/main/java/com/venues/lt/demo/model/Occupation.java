package com.venues.lt.demo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Occupation {
    @Id
    private Integer occupationId;

    private Integer worktimeId;

    private String occupationName;

    private Integer year;

    private Integer term;

    private Date occupationDate;

    private String roomName;

    private Integer activityNum;

    private String userId;

    @ApiModelProperty(value="按建筑物占用时需要填这个")
    private Integer buildingId;

    //
    @ApiModelProperty(value="1是建筑物占用 2是场地占用")
    private Integer type;

}