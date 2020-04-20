package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.Room;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    int deleteByKey(String roomName);

    int insert(Room record);

    Room selectByKey(String roomName);

    List<Room> selectAll();

    int updateByPrimaryKey(Room record);
}