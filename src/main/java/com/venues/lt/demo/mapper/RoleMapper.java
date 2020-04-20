package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.Role;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    int deleteByKey(Integer roleId);

    Role selectByKey(Integer roleId);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);
}