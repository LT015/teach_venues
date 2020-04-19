package com.venues.lt.demo.service;

import com.venues.lt.demo.model.Building;
import com.venues.lt.framework.general.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BuildingService extends BaseService<Building> {

    Building create(Building building);

    void delete(int buildingId);

    List<Building> list();

    Building queryById(int buildingId) ;

    String queryNameById(int buildingId) ;

    Building updateDescription(int buildingId, String description);

    List<Building> getBuildingByName(String name);

    // 0表示上传失败 1表示成功
    int uploadBuilding( MultipartFile file);

}
