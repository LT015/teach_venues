package com.venues.lt.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class WorkTime {
    private Integer worktimeId;

    private String worktimeName;

    private Date worktimeStart;

    private Date worktimeEnd;

}