package com.venues.lt.demo.controller;

import com.venues.lt.demo.model.Department;
import com.venues.lt.demo.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/venues/department")
@Api(value = "部门",description = "部门操作api")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @ResponseBody
    @PutMapping
    public Department create(@RequestBody Department department) {
        return departmentService.create(department);
    }

    @DeleteMapping(value = "/{departmentId:.+}")
    public void delete(@PathVariable int departmentId) {
        departmentService.delete(departmentId);
    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    public List<Department> list() {
        return departmentService.list();
    }

    @ResponseBody
    @GetMapping("/{id:.+}")
    public Department getById(@PathVariable int id) {
        return departmentService.queryById(id);
    }

    @ResponseBody
    @PostMapping("/update/description/{id:.+}")
    public Department updateDescription(@PathVariable int id,@RequestParam("description") String description) {
        return departmentService.updateDescription(id, description);
    }

    @ResponseBody
    @PostMapping("/update/name/{id:.+}")
    public Department updateName(@PathVariable int id, @RequestParam("name") String name) {
        return departmentService.updateName(id, name);
    }

    @ResponseBody
    @PostMapping("/update/buildingId/{id:.+}/{buildingId:.+}")
    public Department updateBuildingId(@PathVariable int id, @PathVariable int buildingId) {
        return departmentService.updateBuildingId(id, buildingId);
    }

    @ResponseBody
    @PostMapping("/update/higherDept/{id:.+}/{higher:.+}")
    public Department updateHigherDept(@PathVariable int id, @PathVariable int higher) {
        return departmentService.updateHigherDepartment(id, higher);
    }

}
