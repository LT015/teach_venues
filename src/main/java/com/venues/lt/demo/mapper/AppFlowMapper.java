package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import java.util.List;

import com.venues.lt.demo.model.Timetable;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppFlowMapper extends BaseMapper<AppFlow> {

    int deleteByKey(@Param("applicationId") Integer applicationId, @Param("flowId") Integer flowId);

    AppFlow selectByKey(@Param("applicationId") Integer applicationId, @Param("flowId") Integer flowId);

    List<AppFlow> selectAll();

    int updateByPrimaryKey(AppFlow record);

}