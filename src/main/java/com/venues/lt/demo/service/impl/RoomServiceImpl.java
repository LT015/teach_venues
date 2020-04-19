package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.BuildingMapper;
import com.venues.lt.demo.mapper.DepartmentMapper;
import com.venues.lt.demo.mapper.RoomMapper;
import com.venues.lt.demo.mapper.TimetableMapper;
import com.venues.lt.demo.model.Building;
import com.venues.lt.demo.model.Department;
import com.venues.lt.demo.model.Room;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.service.*;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import com.venues.lt.framework.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl  extends BaseServiceImpl<Room> implements RoomService {

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

    public List<RoomDto> selectByBuildingIdAndFloor(String buildingId, int floor) {
        List<Room> list = new ArrayList<>();
        if(floor != 0){
            list.addAll(this.creatQuery()
                    .andEqualTo("building_id",buildingId)
                    .andEqualTo("floor",floor)
                    .list());
        }else{
            list.addAll(this.creatQuery()
                    .andEqualTo("building_id",buildingId)
                    .list());
        }
        return  list.stream().map(this::handleRoom).collect(Collectors.toList());
    }

    public RoomDto selectByIdRoomName(String roomName){
        Room room = this.selectByPrimaryKey(roomName);
        return handleRoom(room);
    }

    public void updateRoom(){

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

        Department department = departmentService.selectByPrimaryKey(room.getDeptId());
        roomDto.setDeptName(department.getDeptName());
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
                        .andEqualTo("dept_name",stringList.get(indexList.get(6)))
                        .list().get(0);
                room.setDeptId(department.getDeptId());
            }
            if(!stringList.get(indexList.get(7)).equals("")){
                Building building =  buildingService.creatQuery()
                        .andEqualTo("dept_name",stringList.get(indexList.get(7)))
                        .list().get(0);
                room.setBuildingId(building.getBuildingId());
            }
            if(!stringList.get(indexList.get(8)).equals("")){
                room.setRoomManager(stringList.get(indexList.get(8)));
            }
            room.setRoomType(stringList.get(indexList.get(9)));
            roomList.add(room);
        }
        saveRoom(roomList);

    }

    @Async
    public void saveRoom(List<Room> list){
        for (int i = 0; i < list.size(); i++){
            this.save(list.get(i));
        }
    }

}