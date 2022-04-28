package com.myteek.mybatis.generic.mapper.base;

import com.myteek.mybatis.generic.model.BaseModel;

import java.util.List;

public interface InsertMapper<T extends BaseModel> {

    /**
     * insert data
     * @param model model
     * @return int
     */
    int insert(T model);

    /**
     * insert non null properties
     * @param model model
     * @return int
     */
    int insertSelective(T model);

    /**
     * batch insert
     * @param list list model
     * @return int
     */
    int batchInsert(List<T> list);

    /**
     * insert into ... on duplicate key update
     * @param model model
     * @return int
     */
    int insertOrUpdateSelective(T model);

}
