package com.venues.lt.demo.controller;

import com.venues.lt.demo.model.Occupation;
import com.venues.lt.demo.service.OccupationService;
import com.venues.lt.demo.util.ResponseCode;
import com.venues.lt.demo.util.ResponseData;
import com.venues.lt.demo.util.ResponseMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues/occupation")
@Api(value = "场地占用",description = "场地占用api")
public class OccupationController {

    @Autowired
    OccupationService occupationService;

    @ResponseBody
    @PostMapping("/commit/building")
    @ApiOperation(value = "建筑物占用", notes = "建筑物占用")
    public ResponseData commitByBuilding(@RequestBody Occupation occupation) {
        if(occupationService.commitByBuilding(occupation) == 1){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }

    }

    @ResponseBody
    @PostMapping("/commit/room")
    @ApiOperation(value = "场地占用", notes = "场地占用")
    public ResponseData commitByRoom(@RequestBody List<Occupation> occupation) {
        if(occupationService.commitByRoom(occupation) == 1){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }

    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "获取占用记录", notes = "获取某个日期的场地占用情况 占用时不分时段  整天占用")
    public ResponseData getList(@RequestParam("buildingId") int buildingId, @RequestParam("date") String date) {
        if(occupationService.getList(buildingId,date) != null){
            return ResponseData.success(occupationService.getList(buildingId,date));
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }
    }
    @ResponseBody
    @PostMapping("/cancel")
    @ApiOperation(value = "取消场地占用", notes = "取消场地的占用 ")
    public ResponseData cancel(@RequestParam("occupationId") int occupationId) {
        if(occupationService.cancel(occupationId) == 1){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }
    }
}
