package com.venues.lt.demo.service;

import com.venues.lt.demo.model.Occupation;
import com.venues.lt.demo.model.dto.RoomStatus;

import java.util.List;

public interface OccupationService {

    int commitByRoom(List<Occupation> occupation);

    int commitByBuilding(List<Occupation> occupations);

    List<RoomStatus> getList(Integer buildingId, String date);

    int cancel(List<Integer> occupationId);

    int getStatus(String date, String roomName);
}
