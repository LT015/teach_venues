package com.venues.lt.demo.controller;

import com.venues.lt.demo.model.Building;
import com.venues.lt.demo.service.BuildingService;
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
@RequestMapping("/venues/building")
@Api(value = "建筑物",description = "建筑物操作api")
public class BuildingController {

    @Autowired
    BuildingService buildingService;

    @ResponseBody
    @PutMapping
    @ApiOperation(value = "添加单个建筑物", notes = "添加单个建筑物")
    public Building create(@RequestBody Building building) {
        return buildingService.create(building);
    }

    @ResponseBody
    @PutMapping(value = "/list")
    @ApiOperation(value = "添加建筑物文件", notes = "添加建筑物文件")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) {
        int result = buildingService.uploadBuilding(file);
        if(result == 1){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.LOGIN_FAIL);
        }
    }

    @DeleteMapping(value = "/{buildingId:.+}")
    @ApiOperation(value = "删除建筑物", notes = "删除建筑物")
    public void delete(@PathVariable int buildingId) {
        buildingService.delete(buildingId);
    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "获取建筑物列表", notes = "获取所有建筑物")
    public List<Building> list() {
        return buildingService.list();
    }

    @ResponseBody
    @GetMapping("/{id:.+}")
    @ApiOperation(value = "根据建筑物id获取建筑物", notes = "")
    public Building getById(@PathVariable int id) {
        return buildingService.queryById(id);
    }

    @ResponseBody
    @GetMapping("/getname/{id:.+}")
    @ApiIgnore
    public String getNameById(@PathVariable int id) {
        return buildingService.queryNameById(id);
    }

    @ResponseBody
    @PostMapping("/update/description/{id:.+}/{description:.+}")
    @ApiOperation(value = "修改建筑物描述", notes = "修改建筑物描述测试")
    public Building updateDescription(@PathVariable int id, @PathVariable String description) {
        return buildingService.updateDescription(id, description);
    }

}
