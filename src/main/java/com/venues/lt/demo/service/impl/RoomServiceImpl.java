package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.BuildingMapper;
import com.venues.lt.demo.mapper.DepartmentMapper;
import com.venues.lt.demo.mapper.RoomMapper;
import com.venues.lt.demo.mapper.TimetableMapper;
import com.venues.lt.demo.model.*;
import com.venues.lt.demo.model.dto.FloorAndRoom;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.model.dto.RoomStatus;
import com.venues.lt.demo.model.dto.TimetableDto;
import com.venues.lt.demo.service.*;
import com.venues.lt.demo.util.DateUtil;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import com.venues.lt.framework.general.service.QueryBuilder;
import com.venues.lt.framework.utils.ExcelUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl extends BaseServiceImpl<Room> implements RoomService {

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    TimeTableService timeTableService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    BuildingService buildingService;

    @Autowired
    UserService userService;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    OccupationService occupationService;

    public List<RoomDto> selectByBuildingIdAndFloor(int buildingId, String floor, String capacity) {
        List<Room> list  = roomMapper.getByBuildingId(buildingId);
        if (!StringUtils.isEmpty(floor)) {
            list = list.stream().filter(p -> p.getFloor() == Integer.valueOf(floor)).collect(Collectors.toList());
        }
        if (!StringUtils.isEmpty(capacity)) {
            list = list.stream().filter(p -> p.getCapacity() >= Integer.valueOf(capacity)).collect(Collectors.toList());
        }
        return  list.stream().map(this::handleRoom).collect(Collectors.toList());
    }

    public RoomDto selectByRoomName(String roomName){
        Room room = roomMapper.selectByKey(roomName);
        return handleRoom(room);
    }

    public int create(Room room){
        this.save(room);
        return 1;
    }

    public int uploadRoom( MultipartFile file) {
        String name = file.getOriginalFilename();
        if(name.length() < 6 || !name.substring(name.length()-5).equals(".xlsx")){
            List<Object> li = new ArrayList<>();
            li.add("文件格式错误");
            return 0;
        }
        List<List<String>> list = null;
        try {
            list = ExcelUtil.excelToObjectList(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list != null){
            parseRoom(list);
        }

        return 1;
    }

    public List<FloorAndRoom> getFloorAndRoom(Integer buildingId){
        List<FloorAndRoom> list = new ArrayList<>();
        List<Integer> floorList = roomMapper.getFloorByBuildingId(buildingId);
        if(floorList == null || floorList.size() == 0){
            return null;
        }
        for(int i =0; i < floorList.size(); i++){
            FloorAndRoom floorAndRoom = new FloorAndRoom();
            List<String> roomNameList = roomMapper.getRoomNameByFloorAndBuildingId(floorList.get(i),buildingId);
            floorAndRoom.setFloor(floorList.get(i));
            floorAndRoom.setRoomName(roomNameList);
            list.add(floorAndRoom);
        }
        return list;
    }

    public List<Room> updateRoom( List<Room> roomList) {
        for(int i = 0;i< roomList.size();i++){
            this.updateByPrimaryKey(roomList.get(i));
        }
        return roomList;
    }

    public List<RoomStatus> getListByTimeAndBuildingId(Integer buildingId, Integer start, Integer end, String date){
        List<RoomStatus> list = new ArrayList<>();
        int startTime = start * 2 - 1;//划分成小节 因为有些课程可能是一个半单元
        int endTime = end * 2;
        List<Room> roomList = roomMapper.getByBuildingId(buildingId);
        if(roomList != null){
            for(int i = 0;i < roomList.size();i++){
                RoomStatus roomStatus = new RoomStatus();
                RoomDto roomDto = handleRoom(roomList.get(i));
                roomStatus.setBuildingName(roomDto.getBuildingName());
                roomStatus.setCapacity(roomDto.getCapacity());
                roomStatus.setFloor(roomDto.getFloor());
                roomStatus.setArea(roomDto.getArea());
                if(roomStatus.getDeptName() != null){
                    roomStatus.setDeptName(roomDto.getDeptName());
                }
                roomStatus.setRoomName(roomDto.getRoomName());
                roomStatus.setStatus("未使用");
                if(roomDto.getStatus().equals("不可用")){
                    roomStatus.setStatus(roomDto.getStatus());
                }else{
                    int weekNum = DateUtil.getWeekNum(date);
                    int dayOfWeek = DateUtil.getDayOfWeek(date);
                    List<TimetableDto> timetableDtoList = timeTableService.list(roomDto.getRoomName());
                    timetableDtoList.forEach(timetableDto -> {
                        String[] strings = timetableDto.getWeekly().split("-");
                        if(weekNum >= Integer.valueOf(strings[0]) && weekNum <= Integer.valueOf(strings[1])){
                            if(timetableDto.getWeekday() == dayOfWeek){
                                if(startTime > timetableDto.getEndNum() || endTime < timetableDto.getStartNum()){

                                }else{
                                    roomStatus.setStatus("已使用");
                                }
                            }
                        }
                    });
                    if(roomStatus.getStatus().equals("未使用")){ // 去申请表里查
                        int flag = applicationService.getStatus(date, start, end, roomDto.getRoomName());
                        if(flag == 1){
                            roomStatus.setStatus("已使用");
                        }
                    }
                    if(roomStatus.getStatus().equals("未使用")){ // 去占用表里查
                        int flag = occupationService.getStatus(date, roomDto.getRoomName());
                        if(flag == 1){
                            roomStatus.setStatus("已占用");
                        }
                    }
                }
                list.add(roomStatus);

            }

        }
        return list;
    }

    public List<Room> getByBuildingId(int buildingId){
        List<Room> roomList = roomMapper.getByBuildingId(buildingId);
        return roomList;
    }

    public List<Integer> confirm(String roomName1, String roomName2, String roomName3, Integer start, Integer end, String date){
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        list.add(0);
        int flag1 = applicationService.getStatus(date, start, end, roomName1);
        if(applicationService.getStatus(date, start, end, roomName1) == 1){
            list.set(0,1);
        }
        if(occupationService.getStatus(date, roomName1) == 1){
            list.set(0,1);
        }
        if (!StringUtils.isEmpty(roomName2)) {
            if(applicationService.getStatus(date, start, end, roomName2) == 1){
                list.set(1,1);
            }
            if(occupationService.getStatus(date, roomName2) == 1){
                list.set(1,1);
            }
        }
        if (!StringUtils.isEmpty(roomName3)) {
            if(applicationService.getStatus(date, start, end, roomName3) == 1){
                list.set(2,1);
            }
            if(occupationService.getStatus(date, roomName3) == 1){
                list.set(2,1);
            }
        }

        return list;
    }
    public Integer getBuildingIdByName(String name){
        return roomMapper.selectByKey(name).getBuildingId();
    }

    private RoomDto handleRoom(Room room) {
        RoomDto roomDto  = new RoomDto();
        roomDto.setRoomName(room.getRoomName());
        roomDto.setFloor(room.getFloor());
        roomDto.setDoorNum(room.getDoorNum());
        roomDto.setCapacity(room.getCapacity());
        roomDto.setStatus(room.getStatus());
        roomDto.setArea(room.getArea());
        roomDto.setDeptId(room.getDeptId());
        roomDto.setBuildingId(room.getBuildingId());
        roomDto.setRoomManager(room.getRoomManager());
        if(room.getDeptId() != null){
            Department department = departmentService.selectByPrimaryKey(room.getDeptId());
            roomDto.setDeptName(department.getDeptName());
        }
        Building building = buildingService.selectByPrimaryKey(room.getBuildingId());
        roomDto.setBuildingName(building.getBuildingName());
        return roomDto;
    }

    private int getWeekday(String str){
        if(str.equals("星期一")){
            return 1;
        }else if(str.equals("星期二")){
            return 2;
        }else if(str.equals("星期三")){
            return 3;
        }else if(str.equals("星期四")){
            return 4;
        }else if(str.equals("星期五")){
            return 5;
        }else if(str.equals("星期六")){
            return 6;
        }else{
            return 7;
        }
    }

    public void parseRoom(List<List<String>> list){
        List<Integer> indexList = new ArrayList<>(14);
        List<String> titleList = list.get(0);//标题栏
        titleList.forEach(title ->{
            if(title.equals("名称")){
                indexList.set(0,titleList.indexOf(title));
            }else if(title.equals("楼层")){
                indexList.set(1,titleList.indexOf(title));
            }else if(title.equals("门牌号")){
                indexList.set(2,titleList.indexOf(title));
            }else if(title.equals("容量")){
                indexList.set(3,titleList.indexOf(title));
            }else if(title.equals("状态")){
                indexList.set(4,titleList.indexOf(title));
            }else if(title.equals("使用面积")){
                indexList.set(5,titleList.indexOf(title));
            }else if(title.equals("使用单位")){
                indexList.set(6,titleList.indexOf(title));
            }else if(title.equals("所属建筑物")){
                indexList.set(7,titleList.indexOf(title));
            }else if(title.equals("管理者职工号")){
                indexList.set(8,titleList.indexOf(title));
            }else if(title.equals("类型")){
                indexList.set(9,titleList.indexOf(title));
            }
        });
        List<Room> roomList = new ArrayList<>();
        for(int i = 1;i < list.size();i++){
            Room room = new Room();
            List<String> stringList = list.get(i);
            room.setRoomName(stringList.get(indexList.get(0)));
            room.setFloor(Integer.valueOf(stringList.get(indexList.get(1))));
            room.setDoorNum(stringList.get(indexList.get(2)));
            if(!stringList.get(indexList.get(3)).equals("")){
                room.setCapacity(Integer.valueOf(stringList.get(indexList.get(3))));
            }
            room.setStatus(stringList.get(indexList.get(4)));
            if(!stringList.get(indexList.get(5)).equals("")){
                room.setArea(Integer.valueOf(stringList.get(indexList.get(5))));
            }
            if(!stringList.get(indexList.get(6)).equals("")){
                Department department =  departmentService.creatQuery()
                        .andEqualTo("deptName",stringList.get(indexList.get(6)))
                        .list().get(0);
                room.setDeptId(department.getDeptId());
            }
            if(!stringList.get(indexList.get(7)).equals("")){
                Building building =  buildingService.creatQuery()
                        .andEqualTo("deptName",stringList.get(indexList.get(7)))
                        .list().get(0);
                room.setBuildingId(building.getBuildingId());
            }
            if(!stringList.get(indexList.get(8)).equals("")){
                room.setRoomManager(stringList.get(indexList.get(8)));
            }
            room.setRoomType(stringList.get(indexList.get(9)));
            room.setUse(0);
            roomList.add(room);
        }
        saveRoom(roomList);

    }

    @Async
    public void saveRoom(List<Room> list){
        this.insertList(list);
    }

}