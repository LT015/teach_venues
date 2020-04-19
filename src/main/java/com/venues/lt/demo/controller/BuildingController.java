package com.venues.lt.demo.controller;

import com.venues.lt.demo.model.Building;
import com.venues.lt.demo.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/LBS/building")
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

    @GetMapping("/name/{name:.+}")
    public List<Building> getBuildingByType(@PathVariable String name) throws UnsupportedEncodingException {
        name = URLDecoder.decode(name, "utf-8");
        return buildingService.getBuildingByName(name);
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Building> list() {
        return buildingService.list();
    }

    @ResponseBody
    @GetMapping("/{id:.+}")
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
