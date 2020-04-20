package com.venues.lt.framework.general.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.venues.lt.framework.general.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired
	private BaseMapper<T> mapper;
	private boolean selective = false;
	private T t;
	private String method;

	@SuppressWarnings("unchecked")
	public Example getExample() {
		Type t = this.getClass().getGenericSuperclass();
		Type[] p = ((ParameterizedType) t).getActualTypeArguments();
		return new Example((Class<T>) p[0]);
	}

	@Override
	public T add(T t) {
		if (selective)
			mapper.insert(t);
		else
			mapper.insertSelective(t);
		return t;
	}

	@Override
	public Integer updateByPrimaryKey(T t) {
		if (selective)
			return mapper.updateByPrimaryKeySelective(t);
		else
			return mapper.updateByPrimaryKey(t);
	}

	@Override
	public Integer delete(T t) {
		return mapper.delete(t);
	}

	@Override
	public Integer deleteByPrimaryKey(Object key) {
		return mapper.deleteByPrimaryKey(key);
	}

	@Override
	public BaseService<T> setSelective(boolean selective) {
		this.selective = selective;
		return this;
	}

	@Override
	public T selectByPrimaryKey(Object key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public T selectOne(T t) {
		return mapper.selectOne(t);
	}

	@Override
	public BaseServiceImpl<T> select(T t) {
		this.t = t;
		this.method = "select";
		return this;
	}

	@Override
	public BaseServiceImpl<T> selectAll() {
		this.method = "selectAll";
		return this;
	}

	@Override
	public QueryBuilder<T> creatQuery() {
		return new QueryBuilder<T>(mapper, getExample());
	}

	@Override
	public List<T> list() {
		return creatResult();
	}

	@Override
	public PageInfo<T> pageInfo(Integer pageNo, Integer pageSize) {
		pageNo = pageNo == null ? 1 : pageNo;
		pageSize = pageSize == null ? 10 : pageSize;
		PageHelper.startPage(pageNo, pageSize);
		List<T> result = creatResult();
		PageInfo<T> pageInfo = new PageInfo<T>(result);
		return pageInfo;
	}

	@Override
	public int count(T t) {
		return mapper.selectCount(t);
	}

	@Override
	public List<T> findByProperty(String property, String value) {
		Example example = this.getExample();
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo(property, value);
		return mapper.selectByExample(example);
	}

	@Override
	public List<T> findByProperties(String[] properties, String[] values) {
		Example example = this.getExample();
		Example.Criteria criteria = example.createCriteria();
		for (int i = 0; i < properties.length; i++) {
			criteria.andEqualTo(properties[i], values[i]);
		}
		return mapper.selectByExample(example);
	}

	public boolean isSelective() {
		return selective;
	}

	private List<T> creatResult() {
		return ("select".equals(method) ? mapper.select(this.t) : mapper.selectAll());
	}

	protected void creatPageHelper(Integer pageNo, Integer pageSize) {
		pageNo = pageNo == null ? 1 : pageNo;
		pageSize = pageSize == null ? 10 : pageSize;
		PageHelper.startPage(pageNo, pageSize);
	}

	protected PageInfo<T> getPageInfo(List<T> list) {
		PageInfo<T> pageInfo = new PageInfo<T>(list);
		return pageInfo;
	}

	public T saveOrUpdate(T t) {
		T r = mapper.selectByPrimaryKey(t);
		if (r == null) {
			mapper.insert(t);
		} else {
			mapper.updateByPrimaryKeySelective(t);
		}
		return r;
	}

	public T save(T t){
		mapper.insert(t);
		return t;
	}

	public int insertList(List<T> var1) {
		return mapper.insertList(var1);
	}
}
