package com.venues.lt.demo.model;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Building {
    @Id
    private Integer buildingId;

    private String buildingName;

    private Double longitude;

    private Double latitude;

    private String buildingDescribe;

    private String buildingImage;

    private String campus;

    private Integer roomNum;

    private Integer capacity;

    private String buildingManager;

    private String status;

}