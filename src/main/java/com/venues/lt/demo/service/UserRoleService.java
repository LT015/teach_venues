package com.venues.lt.demo.service;

import com.venues.lt.demo.model.UserRole;
import com.venues.lt.framework.general.service.BaseService;

import java.util.List;

public interface UserRoleService extends BaseService<UserRole> {

    List<UserRole> selectByUserId(String userId);
}
