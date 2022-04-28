package com.myteek.mybatis.generic.mapper;

import com.myteek.mybatis.generic.mapper.base.DeleteMapper;
import com.myteek.mybatis.generic.mapper.base.InsertMapper;
import com.myteek.mybatis.generic.mapper.base.SelectMapper;
import com.myteek.mybatis.generic.mapper.base.SelectPrimaryKeyMapper;
import com.myteek.mybatis.generic.mapper.base.UpdateMapper;
import com.myteek.mybatis.generic.model.BaseModel;

import java.io.Serializable;

public interface GenericMapper<T extends BaseModel, K extends Serializable>
        extends InsertMapper<T>, DeleteMapper<K>, UpdateMapper<T>, SelectMapper<T>, SelectPrimaryKeyMapper<T, K> {
}
