package com.venues.lt.demo.service;

import com.venues.lt.demo.model.Building;
import com.venues.lt.demo.model.Room;
import com.venues.lt.demo.model.dto.FloorAndRoom;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.model.dto.RoomStatus;
import com.venues.lt.framework.general.service.BaseService;
import io.swagger.models.auth.In;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface RoomService extends BaseService<Room> {

    List<RoomDto> selectByBuildingIdAndFloor(int buildingId, String floor, String capacity);

    RoomDto selectByRoomName(String roomName);

    List<Room> updateRoom( List<Room> room);

    int create(Room room);

    int uploadRoom( MultipartFile file);

    List<FloorAndRoom> getFloorAndRoom(Integer buildingId);

    List<RoomStatus> getListByTimeAndBuildingId(Integer buildingId, Integer startTime, Integer endTime, String date);

    List<Room> getByBuildingId(int building_id);

    List<Integer> confirm(String roomName1, String roomName2, String roomName3, Integer startTime, Integer endTime, String date);

    // 根据场地名得到他的buildingId
    Integer getBuildingIdByName(String name);


}
