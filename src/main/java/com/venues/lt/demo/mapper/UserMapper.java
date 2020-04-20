package com.venues.lt.demo.mapper;

import com.venues.lt.demo.model.User;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    int deleteByKey(String userId);

    int insert(User record);

    User selectByKey(String userId);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}