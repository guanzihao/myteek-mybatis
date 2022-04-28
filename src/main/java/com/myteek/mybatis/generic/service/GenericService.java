package com.myteek.mybatis.generic.service;

import com.myteek.mybatis.generic.mapper.GenericMapper;
import com.myteek.mybatis.generic.model.BaseModel;

import java.io.Serializable;

public interface GenericService<T extends BaseModel, K extends Serializable> extends GenericMapper<T, K> {

}
