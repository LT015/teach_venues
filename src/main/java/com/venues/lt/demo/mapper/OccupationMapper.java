package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.Occupation;
import java.util.List;

import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OccupationMapper extends BaseMapper<Occupation> {
    int deleteByKey(@Param("occupationId") Integer occupationId);

    Occupation selectByKey(@Param("occupationId") Integer occupationId);

    List<Occupation> selectAll();

    int updateByPrimaryKey(Occupation record);

    List<Occupation> selectByRoomName(String roomName);
}