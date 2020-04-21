package com.venues.lt.demo.service;

import com.venues.lt.demo.model.Timetable;
import com.venues.lt.demo.model.dto.TimetableDto;
import com.venues.lt.framework.general.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TimeTableService extends BaseService<Timetable> {

    List<TimetableDto> list(String roomName);

    int uploadTimetable( MultipartFile file);
}
