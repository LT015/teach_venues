package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.Application;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {
    int deleteByKey(Integer application);

    Application selectByKey(Integer application);

    List<Application> selectAll();

    int updateByPrimaryKey(Application record);
}