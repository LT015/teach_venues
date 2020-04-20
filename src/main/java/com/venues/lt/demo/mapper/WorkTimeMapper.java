package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.WorkTime;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkTimeMapper extends BaseMapper<WorkTime> {
    int deleteByKey(Integer worktimeId);

    WorkTime selectByKey(Integer worktimeId);

    List<WorkTime> selectAll();

    int updateByPrimaryKey(WorkTime record);
}