package com.venues.lt.demo.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class WorkTime {
    @Id
    private Integer worktimeId;

    private String worktimeName;

    private Date worktimeStart;

    private Date worktimeEnd;

}