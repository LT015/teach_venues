package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.AppFlow;
import com.venues.lt.demo.model.Building;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BuildingMapper extends BaseMapper<Building> {
    int deleteByPrimaryKey(Integer buildingId);

    int insert(Building record);

    Building selectByPrimaryKey(Integer buildingId);

    List<Building> selectAll();

    int updateByPrimaryKey(Building record);
}