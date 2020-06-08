package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.OccupationMapper;
import com.venues.lt.demo.mapper.RoomMapper;
import com.venues.lt.demo.model.Application;
import com.venues.lt.demo.model.Occupation;
import com.venues.lt.demo.model.Room;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.model.dto.RoomStatus;
import com.venues.lt.demo.service.OccupationService;
import com.venues.lt.demo.service.RoomService;
import com.venues.lt.demo.util.DateUtil;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OccupationServiceImpl extends BaseServiceImpl<Occupation> implements OccupationService {

    @Autowired
    OccupationMapper occupationMapper;

    @Autowired
    RoomService roomService;

    @Override
    public int commitByRoom(List<Occupation> occupations) {
        for(int i = 0; i< occupations.size();i++){
            occupations.get(i).setYear(DateUtil.YEAR);
            occupations.get(i).setTerm(DateUtil.TERM);
            occupations.get(i).setType(2);
            this.save(occupations.get(i));
        }

        return 1;
    }

    @Override
    public int commitByBuilding(List<Occupation> occupations) {
        for(int i = 0; i < occupations.size();i++){
            occupations.get(i).setYear(DateUtil.YEAR);
            occupations.get(i).setTerm(DateUtil.TERM);
            occupations.get(i).setType(1);
            this.save( occupations.get(i));
        }
        return 0;
    }

    @Override
    public List<RoomStatus> getList(Integer buildingId, String date) {
        List<RoomStatus> list = new ArrayList<>();
        List<RoomDto> roomDtoList = roomService.selectByBuildingIdAndFloor(buildingId,null,null);
        if(roomDtoList != null){
            for(int i = 0;i < roomDtoList.size();i++){
                RoomStatus roomStatus = new RoomStatus();
                RoomDto roomDto = roomDtoList.get(i);
                roomStatus.setBuildingName(roomDto.getBuildingName());
                roomStatus.setCapacity(roomDto.getCapacity());
                roomStatus.setFloor(roomDto.getFloor());
                roomStatus.setArea(roomDto.getArea());
                if(roomStatus.getDeptName() != null){
                    roomStatus.setDeptName(roomDto.getDeptName());
                }
                roomStatus.setRoomName(roomDto.getRoomName());
                roomStatus.setStatus("未使用");
                List<Occupation> occupations = occupationMapper.selectByRoomName(roomDto.getRoomName());
                if(occupations != null){
                    for (int j = 0;j < occupations.size();j++){
                        SimpleDateFormat sy1 = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = occupations.get(j).getOccupationDate();
                        String dateFormat = sy1.format(date1);
                        if(dateFormat.equals(date)){
                            roomStatus.setStatus("已占用");
                            roomStatus.setOccupation(occupations.get(j).getOccupationId());
                        }
                    }
                }
                list.add(roomStatus);

            }

        }
        return list;
    }

    @Override
    public int cancel(List<Integer> occupationIdList) {
        for (int i = 0;i< occupationIdList.size();i++){
            occupationMapper.deleteByKey(occupationIdList.get(i));
        }
        return 1;
    }
    public int getStatus(String date, String roomName){
        List<Occupation> occupations = new ArrayList<>();
        occupations.addAll(occupationMapper.selectByRoomName(roomName));
        Integer buildingId = roomService.getBuildingIdByName(roomName);
        occupations.addAll(this.creatQuery().andEqualTo("buildingId",buildingId).andEqualTo("type",1).list());
        if(occupations != null){
            for (int j = 0;j < occupations.size();j++){
                SimpleDateFormat sy1 = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = occupations.get(j).getOccupationDate();
                String dateFormat = sy1.format(date1);
                if(dateFormat.equals(date)){
                   return 1;
                }
            }
        }
        return 0;

    }
}
