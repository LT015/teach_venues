package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.NonworkDays;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface NonworkDaysMapper extends BaseMapper<NonworkDays> {
    int deleteByPrimaryKey(Date offDay);

    int insert(NonworkDays record);

    List<NonworkDays> selectAll();
}