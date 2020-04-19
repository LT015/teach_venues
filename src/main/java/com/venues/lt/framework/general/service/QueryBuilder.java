package com.venues.lt.framework.general.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

public class QueryBuilder<T> {

    private Mapper<T> mapper;
    private Example example;
    private Criteria criteria;

    public QueryBuilder(Mapper<T> mapper, Example example) {
        this.mapper = mapper;
        this.example = example;
        criteria = this.example.createCriteria();
    }

    public QueryBuilder<T> andIsNull(String property) {
        criteria.andIsNull(property);
        return this;
    }

    public QueryBuilder<T> andIsNotNull(String property) {
        criteria.andIsNotNull(property);
        return this;
    }

    public QueryBuilder<T> andEqualTo(String property, Object value) {
        criteria.andEqualTo(property, value);
        return this;
    }

    public QueryBuilder<T> andNotEqualTo(String property, Object value) {
        criteria.andNotEqualTo(property, value);
        return this;
    }

    public QueryBuilder<T> andGreaterThan(String property, Object value) {
        criteria.andGreaterThan(property, value);
        return this;
    }

    public QueryBuilder<T> andGreaterThanOrEqualTo(String property, Object value) {
        criteria.andGreaterThanOrEqualTo(property, value);
        return this;
    }

    public QueryBuilder<T> andLessThan(String property, Object value) {
        criteria.andLessThan(property, value);
        return this;
    }

    public QueryBuilder<T> andLessThanOrEqualTo(String property, Object value) {
        criteria.andLessThanOrEqualTo(property, value);
        return this;
    }

    public QueryBuilder<T> andIn(String property, Iterable<?> values) {
        criteria.andIn(property, values);
        return this;
    }

    public QueryBuilder<T> andNotIn(String property, Iterable<?> values) {
        criteria.andNotIn(property, values);
        return this;
    }

    public QueryBuilder<T> andBetween(String property, Object value1, Object value2) {
        criteria.andBetween(property, value1, value2);
        return this;
    }

    public QueryBuilder<T> andNotBetween(String property, Object value1, Object value2) {
        criteria.andNotBetween(property, value1, value2);
        return this;
    }

    public QueryBuilder<T> andLike(String property, String value) {
        criteria.andLike(property, value);
        return this;
    }

    public QueryBuilder<T> andNotLike(String property, String value) {
        criteria.andNotLike(property, value);
        return this;
    }

    public QueryBuilder<T> andCondition(String condition) {
        criteria.andCondition(condition);
        return this;
    }

    public QueryBuilder<T> andCondition(String condition, Object value) {
        criteria.andCondition(condition, value);
        return this;
    }

    /**
     * 将此对象的不为空的字段参数作为相等查询条件
     */
    public QueryBuilder<T> andEqualTo(Object param) {
        criteria.andEqualTo(param);
        return this;
    }

    public QueryBuilder<T> andAllEqualTo(Object param) {
        criteria.andAllEqualTo(param);
        return this;
    }

    public QueryBuilder<T> setOrderByClause(String orderByClause) {
        example.setOrderByClause(orderByClause);
        return this;
    }

    public QueryBuilder<T> setDistinct(boolean distinct) {
        example.setDistinct(distinct);
        return this;
    }

    public List<T> list() {
        return mapper.selectByExample(example);
    }

    public Integer count() {
        return mapper.selectCountByExample(example);
    }

    public Integer delete() {
        return mapper.deleteByExample(example);
    }

    /**
     * 根据Example条件更新实体`record`包含的全部属性，null值会被更新
     */
    public Integer update(T t) {
        return mapper.updateByExample(t, example);
    }

    /**
     * 根据条件更新实体包含的不是null的属性值
     */
    public Integer updateByExampleSelective(T t) {
        return mapper.updateByExampleSelective(t, example);
    }

    public PageInfo<T> pageInfo(Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        PageHelper.startPage(pageNo, pageSize);
        List<T> result = mapper.selectByExample(example);
        PageInfo<T> pageInfo = new PageInfo<T>(result);
        return pageInfo;
    }
}
