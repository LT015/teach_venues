package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.Timetable;
import java.util.List;

import com.venues.lt.demo.model.dto.TimetableDto;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TimetableMapper extends BaseMapper<Timetable> {

    int deleteByKey(@Param("courseId") String courseId, @Param("courseName") String courseName, @Param("className") String className, @Param("weekly") String weekly, @Param("section") String section, @Param("roomName") String roomName);

    Timetable selectByKey(@Param("courseId") String courseId, @Param("courseName") String courseName, @Param("className") String className, @Param("weekly") String weekly, @Param("section") String section, @Param("roomName") String roomName);

    List<Timetable> selectAll();

    int updateByPrimaryKey(Timetable record);

    List <TimetableDto> selectByName(String roomName);
}