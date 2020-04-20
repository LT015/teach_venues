package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.Exam;
import java.util.Date;
import java.util.List;

import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExamMapper extends BaseMapper<Exam> {
    int deleteByKey(@Param("examId") String examId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Exam selectByKey(@Param("examId") String examId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<Exam> selectAll();

    int updateByPrimaryKey(Exam record);
}