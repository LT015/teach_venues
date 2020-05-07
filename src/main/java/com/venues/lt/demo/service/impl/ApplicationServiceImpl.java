package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.ApplicationMapper;
import com.venues.lt.demo.model.Application;
import com.venues.lt.demo.model.Building;
import com.venues.lt.demo.model.Department;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.model.dto.UserDto;
import com.venues.lt.demo.service.*;
import com.venues.lt.demo.util.WordUtil;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.text.SimpleDateFormat;

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

    @Autowired
    DepartmentService departmentService;

    @Override
    public Application commit(Application application) {
        application.setState(1);//进入部门审批中
        UserDto userDto = userService.getUserInfo(application.getUserId());
        application.setDeptId(String.valueOf(userDto.getDeptId()));
        Date date = new Date();
        application.setDate(date);
        this.save(application);
        return application;
    }

    //查看我的申请 我的审核记录
    @Override
    public List<Application> getListByUserIdAndState(String userId, Integer state) {
        UserDto userDto =  userService.getUserInfo(userId);
        List<Application> list = new ArrayList<>();
        if(userDto.getRole() == 1){
            list = this.creatQuery().andEqualTo("userId",userId)
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
                    if(start > applications.get(i).getTimeEnd() || end < applications.get(i).getTimeBegin()){
                    }else{
                        return 1;
                    }
                }
            }
        }

        return 0;
    }

    public String createWord(HttpServletResponse response, int applicationId) {
        Application application = this.selectByPrimaryKey(applicationId);
//        Application application = new Application();
//        application.setDate(new Date());
//        application.setApplyDate(new Date());
//        application.setDeptId("2");
//        application.setNumber(60);
//        application.setUserId("1003");
//        application.setTimeBegin(1);
//        application.setTimeEnd(1);
//        application.setActivity("活动内容");
//        application.setMedia(1);

        /** 用于组装word页面需要的数据 */
        Map<String, Object> dataMap = new HashMap<String, Object>();

        /** 申请单位 */
        if(application.getDeptId() != null){
            Department department = departmentService.queryById(Integer.valueOf(application.getDeptId()));
            dataMap.put("application", department.getDeptName());
        }
        // 活动人数
        dataMap.put("num", application.getNumber());
        // 活动日期
        String s = "";
        if(application.getTimeBegin() == 1){
            s = "第一单元";
        }else if(application.getTimeBegin() == 2){
            s = "第二单元";
        }else if(application.getTimeBegin() == 3){
            s = "第三单元";
        }else if(application.getTimeBegin() == 4){
            s = "第四单元";
        }else if(application.getTimeBegin() == 5){
            s = "第五单元";
        }else if(application.getTimeBegin() == 6){
            s = "第六单元";
        }
        if(application.getTimeBegin() != application.getTimeEnd()){
            if(application.getTimeBegin() == 1){
                dataMap.put("time", s + "-第一单元");
            }else if(application.getTimeBegin() == 2){
                dataMap.put("time", s + "-第二单元");
            }else if(application.getTimeBegin() == 3){
                dataMap.put("time", s + "-第三单元");
            }else if(application.getTimeBegin() == 4){
                dataMap.put("time", s + "-第四单元");
            }else if(application.getTimeBegin() == 5){
                dataMap.put("time", s + "-第五单元");
            }else if(application.getTimeBegin() == 6){
                dataMap.put("time", s + "-第六单元");
            }
        }else{
            dataMap.put("time", s);
        }
        UserDto userDto = userService.getUserInfo(application.getUserId());
        dataMap.put("user",  userDto.getUserName());
        dataMap.put("phone", userDto.getPhone());
        dataMap.put("roomName", application.getRoomName());
        dataMap.put("activity", application.getActivity());
        dataMap.put("equipment", application.getMedia() == 1 ? "使用" : "不使用");

        // 申请时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        dataMap.put("applicationTime", sdf.format(application.getApplyDate()));
        // 活动日期
        dataMap.put("date", sdf.format(application.getDate()));

        String filePath; //文件路径
        String fileName; //文件名称
        String fileOnlyName; //文件唯一名称

        //文件路径
        filePath = "c:/venues/doc/";
        //文件名称
        fileOnlyName = userDto.getUserName() + "-申请文档.doc";

        /** 生成word */
        WordUtil.createWord(dataMap, "application.ftl", filePath, fileOnlyName);

        try {

            /** 返回最终生成的word文件流  */
            File file = new File(filePath + File.separator + fileOnlyName);
            InputStream is =  new BufferedInputStream(new FileInputStream(filePath + File.separator + fileOnlyName));
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileOnlyName.getBytes("UTF-8"), "ISO-8859-1"));
            response.addHeader("Content-Length", "" +file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            return "下载成功";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}