package com.venues.lt.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;

    private String userName;

    @ApiModelProperty(value="身份证号")
    private String idNumber;

    private String sex;

    private Integer deptId;

    private String deptName;

    private String userTitle;

    private String position;

    private String phone;

    private String email;

    private String wechat;

    @ApiModelProperty(value="默认1-在职，2-退休，3-离职，4-病休")
    private Integer status;

    @ApiModelProperty(value="1是一般教师 2是部门管理者 3是职能部门 4是场地管理者")
    private Integer role;

    // 0不是 5是管理 6是超级
    @ApiModelProperty(value="0不是 5是管理 6是超级")
    private Integer isManager;

}
