package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.UserRole;
import java.util.List;

import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    int deleteByKey(@Param("userId") String userId, @Param("roleId") Integer roleId);

    List<UserRole> selectAll();
}