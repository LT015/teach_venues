package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.TimetableMapper;
import com.venues.lt.demo.model.Timetable;
import com.venues.lt.demo.model.dto.TimeTableDto;
import com.venues.lt.demo.service.TimeTableService;
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
public class TimeTableServiceImpl  extends BaseServiceImpl<Timetable> implements TimeTableService {

    @Autowired
    TimetableMapper timetableMapper;

    public List<TimeTableDto> list(String roomName) {
        List<Timetable> list = this.creatQuery().andEqualTo("roomName", roomName)
                .andEqualTo("year",2019)
                .andEqualTo("term",2)
                .setOrderByClause("section desc")
                .setDistinct(true)
                .list();
        return list.stream().map(this::handleTimeTable).collect(Collectors.toList());
    }

    public int uploadTimetable( MultipartFile file) {
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
            parseTimetable(list);
        }

        return 1;
    }

    public TimeTableDto handleTimeTable(Timetable timetable) {
        TimeTableDto timeTableDto = new TimeTableDto();
        timeTableDto.setCourseId(timetable.getCourseId());
        timeTableDto.setCourseName(timetable.getCourseName());
        timeTableDto.setClassName(timetable.getClassName());
        timeTableDto.setWeekly(timetable.getWeekly());
        timeTableDto.setSection(timetable.getSection());
        timeTableDto.setRoomName(timetable.getRoomName());
        timeTableDto.setUserName(timetable.getUserName());
        String section = timetable.getSection();
        section = section.replace(" ", "");
        int day = 0;
        if(section.charAt(0) == '一') {
            day = 1;
        }else if(section.charAt(0) == '二'){
            day = 2;
        }else if(section.charAt(0) == '三'){
            day = 3;
        }else if(section.charAt(0) == '四'){
            day = 4;
        }else if(section.charAt(0) == '五'){
            day = 5;
        }else if(section.charAt(0) == '六'){
            day = 6;
        }
        timeTableDto.setWeekday(day);
        int startNum = Integer.valueOf(section.charAt(2));
        int endNum = Integer.valueOf(section.charAt(4));
        timeTableDto.setStartNum(startNum);
        timeTableDto.setEndNum(endNum);
        return timeTableDto;
    }

    public void parseTimetable(List<List<String>> list){
        List<Integer> indexList = new ArrayList<>(14);
        List<String> titleList = list.get(0);//标题栏
        titleList.forEach(title ->{
            if(title.equals("课程代码")){
                indexList.set(0,titleList.indexOf(title));
            }else if(title.equals("课程名称")){
                indexList.set(1,titleList.indexOf(title));
            }else if(title.equals("行政班级")){
                indexList.set(2,titleList.indexOf(title));
            }else if(title.equals("周次")){
                indexList.set(3,titleList.indexOf(title));
            }else if(title.equals("节次")){
                indexList.set(4,titleList.indexOf(title));
            }else if(title.equals("地点")){
                indexList.set(5,titleList.indexOf(title));
            }else if(title.equals("学生人数")){
                indexList.set(8,titleList.indexOf(title));
            }else if(title.equals("教师一工号")){
                indexList.set(9,titleList.indexOf(title));
            }else if(title.equals("教师一姓名")){
                indexList.set(10,titleList.indexOf(title));
            }else if(title.equals("教师一职称")){
                indexList.set(11,titleList.indexOf(title));
            }

            else if(title.equals("学年")){
                indexList.set(6,titleList.indexOf(title));
            }else if(title.equals("学期")){
                indexList.set(7,titleList.indexOf(title));
            } else if(title.equals("任务开始周次")){
                indexList.set(12,titleList.indexOf(title));
            }else if(title.equals("任务结束周次")){
                indexList.set(13,titleList.indexOf(title));
            }
        });
        List<Timetable> timetableList = new ArrayList<>();
        for(int i = 1;i < list.size();i++){
            Timetable timetable = new Timetable();
            List<String> stringList = list.get(i);
            timetable.setCourseId(stringList.get(indexList.get(0)));
            timetable.setCourseName(stringList.get(indexList.get(1)));
            timetable.setClassName(stringList.get(indexList.get(2)));
            timetable.setWeekly(stringList.get(indexList.get(3)));
            timetable.setSection(stringList.get(indexList.get(4)));
            timetable.setRoomName(stringList.get(indexList.get(5)));
            timetable.setStudentNum(Integer.valueOf(stringList.get(indexList.get(8))));
            timetable.setUserId(stringList.get(indexList.get(9)));
            timetable.setUserName(stringList.get(indexList.get(10)));
            timetable.setUserTitle(stringList.get(indexList.get(11)));

            timetable.setYear(2019);
            timetable.setTerm(2);
            String weekly = timetable.getWeekly();
            weekly = weekly.replace(" ", "");
            timetable.setWeekBegin(Integer.valueOf(weekly.charAt(0)));
            timetable.setWeekEnd(Integer.valueOf(weekly.charAt(2)));

            timetableList.add(timetable);
        }
        saveTimetable(timetableList);

    }

    @Async
    public void saveTimetable(List<Timetable> list){
        for (int i = 0; i < list.size(); i++){
            this.save(list.get(i));
        }
    }

}
