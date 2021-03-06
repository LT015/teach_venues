package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.Department;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    int deleteByKey(Integer deptId);

    Department selectByKey(Integer deptId);

    List<Department> selectAll();

    int updateByPrimaryKey(Department record);
}