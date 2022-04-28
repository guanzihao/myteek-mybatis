package com.myteek.mybatis.generic.mapper;

import com.myteek.mybatis.generic.mapper.base.InsertMapper;
import com.myteek.mybatis.generic.mapper.base.SelectMapper;
import com.myteek.mybatis.generic.model.BaseModel;

public interface GenericWithoutPrimaryKeyMapper<T extends BaseModel> extends InsertMapper<T>, SelectMapper<T> {
}
