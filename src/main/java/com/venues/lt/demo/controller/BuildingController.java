package com.venues.lt.demo.controller;

import com.venues.lt.demo.model.Building;
import com.venues.lt.demo.service.BuildingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/LBS/building")
@Api(value = "建筑物",description = "建筑物操作api")
public class BuildingController {

    @Autowired
    BuildingService buildingService;

    @ResponseBody
    @PutMapping
    public Building create(@RequestBody Building building) {
        return buildingService.create(building);
    }

    @DeleteMapping(value = "/{buildingId:.+}")
    public void delete(@PathVariable int buildingId) {
        buildingService.delete(buildingId);
    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "获取建筑物列表", notes = "获取建筑物列表")
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
    public String getNameById(@PathVariable int id) {
        return buildingService.queryNameById(id);
    }

    @ResponseBody
    @PostMapping("/update/description/{id:.+}/{description:.+}")
    public Building updateDescription(@PathVariable int id, @PathVariable String description) {
        return buildingService.updateDescription(id, description);
    }

}
