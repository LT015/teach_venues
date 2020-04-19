package com.venues.lt.framework.general.mapper;

import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public abstract class BaseMapperImpl<T> implements BaseMapper<T> {

}
