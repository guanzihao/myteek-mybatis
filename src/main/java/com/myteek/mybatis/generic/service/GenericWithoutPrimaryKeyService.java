package com.myteek.mybatis.generic.service;

import com.myteek.mybatis.generic.mapper.GenericWithoutPrimaryKeyMapper;
import com.myteek.mybatis.generic.model.BaseModel;

public interface GenericWithoutPrimaryKeyService<T extends BaseModel> extends GenericWithoutPrimaryKeyMapper<T> {

}
