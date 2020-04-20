package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.BuildingMapper;
import com.venues.lt.demo.model.Building;
import com.venues.lt.demo.model.User;
import com.venues.lt.demo.service.BuildingService;
import com.venues.lt.demo.service.UserService;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import com.venues.lt.framework.utils.ExcelUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BuildingServiceImpl  extends BaseServiceImpl<Building> implements BuildingService {

    @Autowired
    BuildingMapper buildingMapper;

    public Building create(Building building) {
        // todo: check validity

        return null;
    }


    public void delete(int buildingId) {
        this.deleteByPrimaryKey(Integer.valueOf(buildingId));
    }

    public List<Building> list() {
        return this.selectAll().list();
    }

    public Building queryById(int buildingId) {
        Building building= buildingMapper.selectByKey(Integer.valueOf(buildingId));
        return building;
    }

    public String queryNameById(int buildingId) {
        Building building= buildingMapper.selectByKey(Integer.valueOf(buildingId));
        return  building.getBuildingName();
    }

    public Building updateDescription(int buildingId, String description) {
        Building building= buildingMapper.selectByKey(Integer.valueOf(buildingId));
        building.setBuildingDescribe(description);
        this.updateByPrimaryKey(building);
        return building;
    }

    public List<Building> getBuildingByName(String name) {
        List<Building> list = this.creatQuery().andLike("building_name", name).list();
        return list;
    }

    public int uploadBuilding( MultipartFile file) {
        String name = file.getOriginalFilename();
        if(name.length() < 6 || !name.substring(name.length()-5).equals(".xlsx")){
            List<Object> li = new ArrayList<>();
            li.add("文件格式错误");
            return 0;
        }
        List<List<String>> list = null;
        try {
            list = ExcelUtil.excelToObjectList(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list != null){
            parseBuilding(list);
        }

        return 1;
    }

    public void parseBuilding(List<List<String>> list){
        List<Integer> indexList = new ArrayList<>(14);
        List<String> titleList = list.get(0);//标题栏
        titleList.forEach(title ->{
            if(title.equals("建筑物id")){
                indexList.set(0,titleList.indexOf(title));
            }else if(title.equals("建筑物名称")){
                indexList.set(1,titleList.indexOf(title));
            }else if(title.equals("经度")){
                indexList.set(2,titleList.indexOf(title));
            }else if(title.equals("纬度")){
                indexList.set(3,titleList.indexOf(title));
            }else if(title.equals("描述")){
                indexList.set(4,titleList.indexOf(title));
            }else if(title.equals("图片")){
                indexList.set(5,titleList.indexOf(title));
            }else if(title.equals("校区")){
                indexList.set(6,titleList.indexOf(title));
            }else if(title.equals("教室个数")){
                indexList.set(7,titleList.indexOf(title));
            }else if(title.equals("容量")){
                indexList.set(8,titleList.indexOf(title));
            }else if(title.equals("管理者")){
                indexList.set(9,titleList.indexOf(title));
            }else if(title.equals("状态")){
                indexList.set(10,titleList.indexOf(title));
            }
        });
        List<Building> buildingList = new ArrayList<>();
        for(int i = 1;i < list.size();i++){
            Building building = new Building();
            List<String> stringList = list.get(i);
            building.setBuildingId(Integer.valueOf(stringList.get(indexList.get(0))));
            building.setBuildingName(stringList.get(indexList.get(1)));
            if(!stringList.get(indexList.get(2)).equals("")){
                building.setLongitude(Double.valueOf(stringList.get(indexList.get(2))));
            }
            if(!stringList.get(indexList.get(3)).equals("")){
                building.setLatitude(Double.valueOf(stringList.get(indexList.get(3))));
            }
            building.setBuildingDescribe(stringList.get(indexList.get(4)));
            building.setBuildingImage(stringList.get(indexList.get(5)));
            building.setCampus(stringList.get(indexList.get(6)));
            if(!stringList.get(indexList.get(7)).equals("")){
                building.setRoomNum(Integer.valueOf(stringList.get(indexList.get(7))));
            }
            if(!stringList.get(indexList.get(8)).equals("")){
                building.setCapacity(Integer.valueOf(stringList.get(indexList.get(8)) ));
            }
            building.setBuildingManager(stringList.get(indexList.get(9)));
            building.setStatus(stringList.get(indexList.get(10)));
            buildingList.add(building);
        }
        saveBuilding(buildingList);

    }

    @Async
    public void saveBuilding(List<Building> list){
        this.insertList(list);
    }
}
