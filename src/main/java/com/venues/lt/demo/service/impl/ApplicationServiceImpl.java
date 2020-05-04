package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.ApplicationMapper;
import com.venues.lt.demo.model.Application;
import com.venues.lt.demo.model.Building;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.model.dto.UserDto;
import com.venues.lt.demo.service.ApplicationService;
import com.venues.lt.demo.service.BuildingService;
import com.venues.lt.demo.service.RoomService;
import com.venues.lt.demo.service.UserService;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/* 0 未提交 1部门审批中 2职能部门审批中 3 通过 4拒绝 5 已执行 6 已结束 */

@Service
@Slf4j
public class ApplicationServiceImpl  extends BaseServiceImpl<Application> implements ApplicationService {

    @Autowired
    ApplicationMapper applicationMapper;

    @Autowired
    UserService userService;

    @Autowired
    RoomService roomService;

    @Autowired
    BuildingService buildingService;

    @Override
    public Application commit(Application application) {
        application.setState(1);//进入部门审批中
        this.save(application);
        return application;
    }

    //查看我的申请 我的审核记录
    @Override
    public List<Application> getListByUserIdAndState(String userId, Integer state) {
        UserDto userDto =  userService.getUserInfo(userId);
        List<Application> list = new ArrayList<>();
        if(userDto.getRole() == 1){
            list = this.creatQuery().andEqualTo("user_id",userId)
                    .andEqualTo("state",state)
                    .list();
        }else if(userDto.getRole() == 2){ // 部门管理者
            list = this.creatQuery().andEqualTo("verify1",userId)
                    .andEqualTo("state",state)
                    .list();
        }else if(userDto.getRole() == 3){
            list = this.creatQuery().andEqualTo("verify2",userId)
                    .andEqualTo("state",state)
                    .list();
        }else if(userDto.getRole() == 4){
            list = this.creatQuery().andEqualTo("execute",userId)
                    .andEqualTo("state",state)
                    .list();
        }
        return list;
    }
    // 查看待审核 待执行
    @Override
    public List<Application> getListByUserId(String userId) {
        UserDto userDto =  userService.getUserInfo(userId);
        List<Application> list = new ArrayList<>();
       if(userDto.getRole() == 2){ // 部门管理者
           List<Application> applications = this.creatQuery()
                   .andEqualTo("state",1)
                   .list();
           if(applications != null){
               for(int i = 0; i < applications.size();i++){
                   UserDto userInfo =  userService.getUserInfo( applications.get(i).getUserId());
                   if(userDto.getDeptId() == userInfo.getDeptId()){
                       list.add(applications.get(i));
                   }
               }
           }
       }else if(userDto.getRole() == 3){
           list = this.creatQuery()
                   .andEqualTo("state",2)
                   .list();
       }else if(userDto.getRole() == 4){
           List<Application> applications = this.creatQuery()
                   .andEqualTo("state",3)
                   .list();
           if(applications != null){
               for(int i = 0; i < applications.size();i++){
                   RoomDto roomDto = roomService.selectByRoomName( applications.get(i).getRoomName());
                   String managerId = "";
                   if(roomDto.getRoomManager() != null){
                       managerId = roomDto.getRoomManager();
                   }else{
                       Building building = buildingService.queryById(roomDto.getBuildingId());
                       if(building.getBuildingManager() != null){
                           managerId = building.getBuildingManager();
                       }
                   }
                   if(userDto.getUserId().equals(managerId)){
                       list.add(applications.get(i));
                   }
               }
           }
        }
        return list;
    }

    @Override
    public Application update(Integer applicationId, String userId, Integer state, String reason, String roomName) {
        UserDto userDto =  userService.getUserInfo(userId);
        Application application = this.creatQuery().andEqualTo("application",applicationId)
                .list().get(0);
        if(userDto.getRole() == 1) {
            if(state == 1){         // 表示提交申请
                application.setState(2);
            }else if(state == 2){       //用户点击结束按钮
                application.setState(6); // 状态变为结束
            }
        }else if(userDto.getRole() == 2){ //部门管理者审批
            application.setVerify1(userId);
            if(state == 0){ // 表示驳回
                if (!StringUtils.isEmpty(reason)) {
                    application.setReason(reason);
                }
                application.setState(4);
            }else if(state == 1){
                application.setState(2); // 状态变为职能部门审批中
            }
        }else if(userDto.getRole() == 3){
            application.setVerify2(userId);
            if(state == 0){ // 表示驳回
                if (!StringUtils.isEmpty(reason)) {
                    application.setReason(reason);
                }
                application.setState(4);
            }else if(state == 1){
                application.setRoomName(roomName);//通过后设置通过的是哪个教室
                application.setState(3); // 状态变为通过
            }

        }else if(userDto.getRole() == 4){
            application.setExecute(userId);
            if(state == 1){ // 表示执行
                application.setState(5);
            }
        }
        this.updateByPrimaryKey(application);
        return application;
    }

    // 根据日期判断要申请的教室是否和已申请的冲突
    public int getStatus(String date, int start, int end, String roomName){
        List<Application> applications = applicationMapper.selectByRoomName(roomName);
        if(applications != null){
            for (int i = 0;i < applications.size();i++){
                SimpleDateFormat sy1 = new SimpleDateFormat("yyyy-MM-dd");
                String dateFormat = sy1.format( applications.get(i).getDate());
                if(dateFormat.equals(date)){
                    if(start > applications.get(i).getTimeEnd() || end > applications.get(i).getTimeBegin()){
                    }else{
                        return 1;
                    }
                }
            }
        }

        return 0;
    }
}