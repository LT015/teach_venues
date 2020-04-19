package com.venues.lt.demo.service;

import com.venues.lt.demo.model.Room;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.framework.general.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface RoomService extends BaseService<Room> {

    List<RoomDto> selectByBuildingIdAndFloor(String buildingId, int floor);

    RoomDto selectByIdRoomName(String roomName);

    void updateRoom();

    int uploadRoom( MultipartFile file);

}
