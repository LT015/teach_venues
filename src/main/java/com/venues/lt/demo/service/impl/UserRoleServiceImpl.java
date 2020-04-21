package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.UserRoleMapper;
import com.venues.lt.demo.model.UserRole;
import com.venues.lt.demo.service.UserRoleService;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService  {
    @Autowired
    private UserRoleMapper userRoleMapper;

    public List<UserRole> selectByUserId(String userId){
        return  userRoleMapper.selectByUserId(userId);
    }
}
