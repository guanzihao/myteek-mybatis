package com.myteek.mybatis.generic.model;

import java.io.Serializable;

public class BasePrimaryKeyModel<K extends Serializable> extends BaseModel {

    protected K id;

    public BasePrimaryKeyModel() {
    }

    public BasePrimaryKeyModel(K id) {
        this.id = id;
    }

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

}
