package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.Room;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    int deleteByKey(String roomName);

    Room selectByKey(String roomName);

    List<Room> selectAll();

    int updateByPrimaryKey(Room record);

    //根据建筑物id得到楼层
    List<Integer> getFloorByBuildingId(Integer buildingId);

    //根据建筑物id和楼层得到教室名
    List<String> getRoomNameByFloorAndBuildingId(Integer floor,Integer buildingId);
}