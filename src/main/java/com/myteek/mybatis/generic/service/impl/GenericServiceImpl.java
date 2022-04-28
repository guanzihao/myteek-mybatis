package com.myteek.mybatis.generic.service.impl;

import com.myteek.mybatis.generic.mapper.GenericMapper;
import com.myteek.mybatis.generic.model.BaseModel;
import com.myteek.mybatis.generic.service.GenericService;
import com.myteek.mybatis.page.Page;

import java.io.Serializable;
import java.util.List;

public abstract class GenericServiceImpl<T extends BaseModel,
        K extends Serializable, M extends GenericMapper<T, K>> implements GenericService<T, K> {

    public abstract M getGenericMapper();

    @Override
    public int deleteByPrimaryKey(K id) {
        return getGenericMapper().deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<K> ids) {
        return getGenericMapper().deleteByIds(ids);
    }

    @Override
    public int insert(T model) {
        return getGenericMapper().insert(model);
    }

    @Override
    public int insertSelective(T model) {
        return getGenericMapper().insertSelective(model);
    }

    @Override
    public int batchInsert(List<T> list) {
        return getGenericMapper().batchInsert(list);
    }

    @Override
    public int insertOrUpdateSelective(T model) {
        return getGenericMapper().insertOrUpdateSelective(model);
    }

    @Override
    public List<T> select(T model) {
        return getGenericMapper().select(model);
    }

    @Override
    public List<T> selectLike(T model) {
        return getGenericMapper().selectLike(model);
    }

    @Override
    public T selectOne(T model) {
        return getGenericMapper().selectOne(model);
    }

    @Override
    public List<T> selectPageList(T model, Page page) {
        return getGenericMapper().selectPageList(model, page);
    }

    @Override
    public List<T> selectPageListLike(T model, Page page) {
        return getGenericMapper().selectPageListLike(model, page);
    }

    @Override
    public Integer selectCount(T model) {
        return getGenericMapper().selectCount(model);
    }

    @Override
    public Integer selectCountLike(T model) {
        return getGenericMapper().selectCountLike(model);
    }

    @Override
    public List<T> selectAll() {
        return getGenericMapper().selectAll();
    }

    @Override
    public T selectByPrimaryKey(K id) {
        return getGenericMapper().selectByPrimaryKey(id);
    }

    public List<T> selectByIds(List<K> ids) {
        return getGenericMapper().selectByIds(ids);
    }

    @Override
    public int updateByPrimaryKey(T model) {
        return getGenericMapper().updateByPrimaryKey(model);
    }

    @Override
    public int updateByPrimaryKeySelective(T model) {
        return getGenericMapper().updateByPrimaryKeySelective(model);
    }

}
