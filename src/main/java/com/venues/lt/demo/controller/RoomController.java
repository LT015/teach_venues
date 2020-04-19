package com.venues.lt.demo.controller;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.service.impl.RoomServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/LBS/room")
@Api(value = "场地",description = "场地操作api")
public class RoomController {

    @Autowired
    RoomServiceImpl roomServiceImpl;

    @ResponseBody
    @GetMapping(value = "/id/{id:.+}/type/{type:.+}/classtime/{classtime:.+}")
    public List<RoomDto> list(@PathVariable String id, @PathVariable int type, @PathVariable int classtime) {
        return null;
    }

    @ResponseBody
    @GetMapping(value = "/userId/{userId:.+}/roomName/{room:.+}/status/{status:.+}")
    public int collect(@PathVariable String userId,@PathVariable String room,@PathVariable int status) throws UnsupportedEncodingException {
        room = URLDecoder.decode(room, "utf-8");
        return 0;
    }


}