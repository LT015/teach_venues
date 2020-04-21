package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.TimetableMapper;
import com.venues.lt.demo.model.Timetable;
import com.venues.lt.demo.model.dto.TimetableDto;
import com.venues.lt.demo.service.TimeTableService;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import com.venues.lt.framework.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeTableServiceImpl  extends BaseServiceImpl<Timetable> implements TimeTableService {

    @Autowired
    TimetableMapper timetableMapper;

    public List<TimetableDto> list(String roomName) {
        List<TimetableDto> list = timetableMapper.selectByName(roomName);
//        List<Timetable> list = this.creatQuery().andEqualTo("roomName", roomName)
//                .andEqualTo("year",2019)
//                .andEqualTo("term",2)
//                .setOrderByClause("section desc")
//                .setDistinct(true)
//                .list();
        return list.stream().map(this::handleTimeTableDto).collect(Collectors.toList());
    }

    public int uploadTimetable( MultipartFile file) {
        String name = file.getOriginalFilename();
        if(name.length() < 6 || !name.substring(name.length()-5).equals(".xls")){
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
        parseTimetable(list);
        return 1;
    }

        private TimetableDto handleTimeTableDto(TimetableDto timetableDto) {
        String section = timetableDto.getSection();
        section = section.replace(" ", "");//例如：二[1-2节]单
        String[] strings = section.split("\\["); //得到 0：二 1：1-2节]单
        int day = 0;
        if(strings[0].equals("一")) {
            day = 1;
        }else if(strings[0].equals("二")){
            day = 2;
        }else if(strings[0].equals("三")){
            day = 3;
        }else if(strings[0].equals("四")){
            day = 4;
        }else if(strings[0].equals("五")){
            day = 5;
        }else if(strings[0].equals("六")){
            day = 6;
        }
        else if(strings[0].equals("七")){
            day = 7;
        }
        timetableDto.setWeekday(day);
        String[] startString = strings[1].split("-");//继续上面的例子   0是“1”  1是“2节]单”
        int startNum = Integer.valueOf(startString[0]);
        String[] endString = startString[1].split("节]");//继续上面的例子   0是“2”  1是“单”
        int endNum = Integer.valueOf(endString[0]);
        timetableDto.setStartNum(startNum);
        timetableDto.setEndNum(endNum);
        if (endString.length > 1){
            if(endString[1].equals("单")){
                timetableDto.setFlag(1);
            }else if(endString[1].equals("双")){
                timetableDto.setFlag(2);
            }
        }else{
            timetableDto.setFlag(0);
        }
        return timetableDto;
    }
    public void parseTimetable(List<List<String>> list){
        List<Integer> indexList = new ArrayList<>();
        for(int i = 0; i < 14 ;i++){
            indexList.add(-1);
        }
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
            if(timetable.getUserName().equals("网络课教师"));
            timetable.setUserId("0000");
            timetable.setUserTitle(stringList.get(indexList.get(11)));

            timetable.setYear(2019);
            timetable.setTerm(2);
            String weekly = timetable.getWeekly();
            String[] strings = weekly.split("-");
            timetable.setWeekBegin(Integer.valueOf(strings[0]));
            timetable.setWeekEnd(Integer.valueOf(strings[1]));

            timetableList.add(timetable);
        }
        saveTimetable(timetableList);
//        this.insertList(timetableList);
//        for (int i = 0; i < timetableList.size(); i++){
//            this.save(timetableList.get(i));
//        }

    }

    public void saveTimetable(List<Timetable> list){
        this.insertList(list);
    }

}
