package com.venues.lt.demo.controller;
import com.venues.lt.demo.model.Room;
import com.venues.lt.demo.model.dto.FloorAndRoom;
import com.venues.lt.demo.model.dto.RoomDto;
import com.venues.lt.demo.model.dto.RoomStatus;
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
import springfox.documentation.annotations.ApiIgnore;

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
    @ApiIgnore
    public List<RoomDto> list(@PathVariable String id, @PathVariable int type, @PathVariable int classtime) {
        return null;
    }

    @ResponseBody
    @GetMapping(value = "/userId/{userId:.+}/roomName/{room:.+}/status/{status:.+}")
    @ApiIgnore
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
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.ADD_FAIL);
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
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }
    }

    @ResponseBody
    @GetMapping("/buildingId/{buildingId:.+}")
    @ApiOperation(value = "根据建筑物id获取建筑物包含的场地名称", notes = "根据建筑物id获取建筑物拥有的楼层和每个楼层的场地信息")
    public ResponseData list(@PathVariable Integer buildingId) {
        List<FloorAndRoom> list = roomService.getFloorAndRoom(buildingId);
        if(list!=null){
            return ResponseData.success(list);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }

    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "更新场地信息", notes = "更新场地信息")
    public ResponseData updateBuilding(@RequestBody Room room) {
        if(roomService.updateRoom(room) != null){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }

    }

    @ResponseBody
    @GetMapping("/status")
    @ApiOperation(value = "根据时间获取场地状态", notes = "根据日期和单元获得场地状态 在申请时挑选场地用")
    public ResponseData getListByTimeAndBuildingId(@RequestParam("buildingId") int buildingId,
                                                   @RequestParam("startTime") int startTime,
                                                   @RequestParam("endTime") int endTime,
                                                   @RequestParam("date") String date) {
        List<RoomStatus> list = roomService.getListByTimeAndBuildingId(buildingId,startTime,endTime,date);
        if(list!=null){
            return ResponseData.success(list);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }

    }
    @ResponseBody
    @GetMapping("/confirm")
    @ApiOperation(value = "在审批的最终阶段判断场地是否冲突", notes = "根据传递的三个场地名判断场地是否冲突")
    public ResponseData getListByTimeAndBuildingId(@RequestParam("roomName1") String roomName1,
                                                   @RequestParam(value = "roomName2", required = false) String roomName2,
                                                   @RequestParam(value = "roomName3", required = false) String roomName3,
                                                   @RequestParam("startTime") int startTime,
                                                   @RequestParam("endTime") int endTime,
                                                   @RequestParam("date") String date) {
        List<Integer> list = roomService.confirm(roomName1, roomName2, roomName3,startTime,endTime,date);
        if(list!=null){
            return ResponseData.success(list);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }

    }
}