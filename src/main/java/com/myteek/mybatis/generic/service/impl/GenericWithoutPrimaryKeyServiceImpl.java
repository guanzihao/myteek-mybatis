package com.myteek.mybatis.generic.service.impl;

import com.myteek.mybatis.generic.mapper.GenericWithoutPrimaryKeyMapper;
import com.myteek.mybatis.generic.model.BaseModel;
import com.myteek.mybatis.generic.service.GenericWithoutPrimaryKeyService;
import com.myteek.mybatis.page.Page;

import java.util.List;

public abstract class GenericWithoutPrimaryKeyServiceImpl<T extends BaseModel,
        M extends GenericWithoutPrimaryKeyMapper<T>> implements GenericWithoutPrimaryKeyService<T> {

    public abstract M getGenericMapper();

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

}
