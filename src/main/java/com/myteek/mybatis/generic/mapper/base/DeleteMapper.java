package com.myteek.mybatis.generic.mapper.base;

import java.io.Serializable;
import java.util.List;

public interface DeleteMapper<K extends Serializable> {

    /**
     * delete by key
     * @param id primary id
     * @return int
     */
    int deleteByPrimaryKey(K id);

    /**
     * delete by key list
     * @param ids primary ids
     * @return int
     */
    int deleteByIds(List<K> ids);

}
