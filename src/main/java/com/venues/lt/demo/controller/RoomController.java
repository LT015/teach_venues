package com.venues.lt.demo.controller;
import com.venues.lt.demo.model.Room;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.service.RoomService;
import com.venues.lt.demo.service.impl.RoomServiceImpl;
import com.venues.lt.demo.util.ResponseCode;
import com.venues.lt.demo.util.ResponseData;
import com.venues.lt.demo.util.ResponseMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/venues/room")
@Api(value = "场地",description = "场地操作api")
public class RoomController {

    @Autowired
    RoomService roomService;

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

    @ResponseBody
    @PutMapping
    @ApiOperation(value = "添加单个场地", notes = "添加单个场地")
    public ResponseData create(@RequestBody Room room) {
        int result =  roomService.create(room);
        if(result == 1){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.LOGIN_FAIL);
        }
    }


    @ResponseBody
    @PutMapping(value = "/list")
    @ApiOperation(value = "添加场地文件", notes = "通过文件添加场地")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) {
        int result = roomService.uploadRoom(file);
        if(result == 1){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.LOGIN_FAIL);
        }
    }

}