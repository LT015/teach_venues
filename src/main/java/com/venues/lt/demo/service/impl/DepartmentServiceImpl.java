package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.DepartmentMapper;
import com.venues.lt.demo.model.Department;
import com.venues.lt.demo.service.DepartmentService;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DepartmentServiceImpl  extends BaseServiceImpl<Department> implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    public Department create(Department department) {
        // todo: check validity
        this.save(department);
        return department;
    }

    /**
     * todo: foreign key or delete all dependencies
     * @param departmentId
     */
    public void delete(int departmentId) {
        Department department = this.selectByPrimaryKey(departmentId);
        this.delete(department);
    }

    public List<Department> list() {
        return this.list();
    }

    public Department queryById(int id) {
        return this.selectByPrimaryKey(id);
    }


    public Department updateDescription(int id, String description) {
        Department department = this.selectByPrimaryKey(id);

        if (department != null) {
            department.setDeptDescribe(description);
            // todo: check validity
            this.updateByPrimaryKey(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

    public Department updateName(int id, String name) {
        Department department = this.selectByPrimaryKey(id);

        if (department != null) {
            department.setDeptName(name);
            // todo: check validity
            this.updateByPrimaryKey(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

    public Department updateHigherDepartment(int id, int higher) {
        Department department = this.selectByPrimaryKey(id);

        if (department != null) {
            department.setHigherDeptId(higher);
            // todo: check validity
            this.updateByPrimaryKey(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

    public Department updateBuildingId(int id, int buildingId) {
        Department department = this.selectByPrimaryKey(id);

        if (department != null) {
            department.setBuildingId(buildingId);
            // todo: check validity
            this.updateByPrimaryKey(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

}
