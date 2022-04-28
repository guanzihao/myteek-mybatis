package com.myteek.mybatis.generic.mapper.base;

import com.myteek.mybatis.generic.model.BaseModel;

import java.io.Serializable;
import java.util.List;

public interface SelectPrimaryKeyMapper<T extends BaseModel, K extends Serializable> {

    /**
     * select by primary key
     * @param id primary id
     * @return T
     */
    T selectByPrimaryKey(K id);

    /**
     * select by id list
     * @param ids id list
     * @return List&lt;T&gt;
     */
    List<T> selectByIds(List<K> ids);

}
