package com.myteek.mybatis.generic.mapper.base;

import com.myteek.mybatis.generic.model.BaseModel;
import com.myteek.mybatis.page.Constant;
import com.myteek.mybatis.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SelectMapper<T extends BaseModel> {

    /**
     * condition equal model properties
     * @param model model
     * @return List&lt;T&gt;
     */
    List<T> select(@Param("model") T model);

    /**
     * condition string column like and other column equals model properties
     * @param model model
     * @return List&lt;T&gt;
     */
    List<T> selectLike(@Param("model") T model);

    /**
     * condition equal model properties
     * @param model model
     * @return T
     */
    T selectOne(@Param("model") T model);

    /**
     * page list
     * @param model model
     * @return List&lt;T&gt;
     */
    List<T> selectPageList(@Param("model") T model, @Param(Constant.PAGE_KEY) Page page);

    /**
     * page list like
     * @param model model
     * @return List&lt;T&gt;
     */
    List<T> selectPageListLike(@Param("model") T model, @Param(Constant.PAGE_KEY) Page page);

    /**
     * select count
     * @param model model
     * @return Integer
     */
    Integer selectCount(@Param("model") T model);

    /**
     * select count like
     * @param model model
     * @return Integer
     */
    Integer selectCountLike(@Param("model") T model);

    /**
     * select all
     * @return List&lt;T&gt;
     */
    List<T> selectAll();

}
