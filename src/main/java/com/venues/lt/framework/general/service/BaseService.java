package com.venues.lt.framework.general.service;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BaseService<T> {

	/**
	 * 如果为空字段需要保存，不使用数据库默认值。需在本方法执行前执行setSelective（）
	 */
	T add(T t);

	/**
	 * 是否更新为空字段，更新直接使用本方法，否则需在本方法执行前执行setSelective（）
	 */
	Integer updateByPrimaryKey(T t);

	/**
	 * 根据实体属性作为条件进行删除，查询条件使用等号
	 */
	Integer delete(T t);

	/**
	 * 根据主键删除对象
	 */
	Integer deleteByPrimaryKey(Object key);

	/**
	 * 是否有选择性的，需在执行方法前执行
	 */
	BaseService<T> setSelective(boolean selective);

	/**
	 * 根据主键字段进行查询
	 */
	T selectByPrimaryKey(Object key);

	/**
	 * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号 可适用于联合逐渐查询
	 */
	T selectOne(T t);

	/**
	 * 根据实体中的属性值进行查询，查询条件使用等号
	 */
	BaseServiceImpl<T> select(T t);

	/**
	 * 查询全部结果
	 */
	BaseServiceImpl<T> selectAll();

	/**
	 * 创建查询对象
	 */
	QueryBuilder<T> creatQuery();

	List<T> list();

	/**
	 * 分页
	 */
	PageInfo<T> pageInfo(Integer pageNo, Integer pageSize);

	int count(T t);

	List<T> findByProperty(String property, String value);

	List<T> findByProperties(String[] properties, String[] values);

	T saveOrUpdate(T t);

	T save(T t);

//	T save(T t);

}
