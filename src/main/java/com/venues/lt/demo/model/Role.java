package com.venues.lt.demo.model;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Role {
    @Id
    private Integer roleId;

    private String roleName;

    private String description;

}