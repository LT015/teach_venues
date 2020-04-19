package com.venues.lt.demo.controller;

import com.venues.lt.demo.model.dto.TimeTableDto;
import com.venues.lt.demo.service.impl.TimeTableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/LBS/timetable")
public class TimeTableController {

    @Autowired
    TimeTableServiceImpl timeTableServiceImpl;

    @ResponseBody
    @GetMapping(value = "/name/{name:.+}/type/{type:.+}")
    public List<TimeTableDto> listByRoomName(@PathVariable String name, @PathVariable int type) throws UnsupportedEncodingException {
        name = URLDecoder.decode(name, "utf-8");
        return null;
    }


}
