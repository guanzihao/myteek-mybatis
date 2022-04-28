package com.myteek.mybatis.generic.mapper.base;

import com.myteek.mybatis.generic.model.BaseModel;

public interface UpdateMapper<T extends BaseModel> {

    /**
     * update by primary key
     * @param model model
     * @return int
     */
    int updateByPrimaryKey(T model);

    /**
     * update by select key selective
     * @param model model
     * @return int
     */
    int updateByPrimaryKeySelective(T model);

}
