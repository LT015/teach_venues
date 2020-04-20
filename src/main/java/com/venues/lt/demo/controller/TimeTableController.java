package com.venues.lt.demo.controller;

import com.venues.lt.demo.model.Timetable;
import com.venues.lt.demo.model.dto.TimeTableDto;
import com.venues.lt.demo.service.TimeTableService;
import com.venues.lt.demo.service.impl.TimeTableServiceImpl;
import com.venues.lt.demo.util.ResponseCode;
import com.venues.lt.demo.util.ResponseData;
import com.venues.lt.demo.util.ResponseMsg;
import com.venues.lt.framework.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/venues/timetable")
@Api(value = "课表",description = "课表操作API")
public class TimeTableController {

    @Autowired
    TimeTableService timeTableService;

    @ResponseBody
    @GetMapping(value = "/roomname/{name:.+}")
    @ApiOperation(value = "获取课表", notes = "根据用户id获教室名称获取课表")
    public ResponseData listByRoomName(@PathVariable String roomname) throws UnsupportedEncodingException {
        roomname = URLDecoder.decode(roomname, "utf-8");
        List<TimeTableDto> list = timeTableService.list(roomname);
        return ResponseData.success(list);
    }

    @ResponseBody
    @PutMapping
    @ApiOperation(value = "添加课表", notes = "添加课表")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) {
        int result = timeTableService.uploadTimetable(file);
        if(result == 1){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.LOGIN_FAIL);
        }
    }


}
