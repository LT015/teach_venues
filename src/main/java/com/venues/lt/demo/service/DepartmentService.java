package com.venues.lt.demo.service;

import com.venues.lt.demo.model.Department;
import com.venues.lt.framework.general.service.BaseService;

import java.util.List;

public interface DepartmentService extends BaseService<Department> {

    Department create(Department department);

    /**
     * todo: foreign key or delete all dependencies
     * @param departmentId
     */
    void delete(int departmentId);

    List<Department> list();

    Department queryById(int id);


    Department updateDescription(int id, String description);

    Department updateName(int id, String name);

    Department updateHigherDepartment(int id, int higher);

    Department updateBuildingId(int id, int buildingId);
}
