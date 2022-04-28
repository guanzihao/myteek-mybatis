package com.myteek.mybatis.constant;

public class Constants {

    public static final String DEFAULT_PRIMARY_COLUMN_NAME = "id";

    public static final String BASE_MODEL_CLASS_PATH = "com.myteek.mybatis.generic.model.BaseModel";

    public static final String BASE_PRIMARY_KEY_MODEL_CLASS_PATH =
            "com.myteek.mybatis.generic.model.BasePrimaryKeyModel";

    public static final String GENERIC_MAPPER_CLASS_PATH = "com.myteek.mybatis.generic.mapper.GenericMapper";

    public static final String GENERIC_WITHOUT_PRIMARY_KEY_MAPPER_CLASS_PATH =
            "com.myteek.mybatis.generic.mapper.GenericWithoutPrimaryKeyMapper";

    public static final String GENERIC_SERVICE_CLASS_PATH = "com.myteek.mybatis.generic.service.GenericService";

    public static final String GENERIC_WITHOUT_PRIMARY_KEY_SERVICE_CLASS_PATH =
            "com.myteek.mybatis.generic.service.GenericWithoutPrimaryKeyService";

    public static final String GENERIC_SERVICE_IMPL_CLASS_PATH =
            "com.myteek.mybatis.generic.service.impl.GenericServiceImpl";

    public static final String GENERIC_WITHOUT_PRIMARY_KEY_SERVICE_IMPL_CLASS_PATH =
            "com.myteek.mybatis.generic.service.impl.GenericWithoutPrimaryKeyServiceImpl";

    public static final String AUTOWIRED_CLASS_PATH = "org.springframework.beans.factory.annotation.Autowired";

    public static final String SERVICE_CLASS_PATH = "org.springframework.stereotype.Service";

    public static final String TRANSACTIONAL_CLASS_PATH = "org.springframework.transaction.annotation.Transactional";

    public static final String MAPPER_CLASS_PATH = "org.apache.ibatis.annotations.Mapper";

}
