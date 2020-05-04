package com.venues.lt.demo.service;

import com.venues.lt.demo.model.Application;

import java.util.List;

public interface ApplicationService {

    Application commit(Application application);

    List<Application> getListByUserIdAndState(String userId, Integer status);

    List<Application> getListByUserId(String userId);

    Application update(Integer applicationId, String userId, Integer state, String reason, String roomName);

    int getStatus(String date, int start, int end, String roomName);
}
