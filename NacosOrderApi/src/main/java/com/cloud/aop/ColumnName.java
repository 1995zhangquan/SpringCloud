package com.cloud.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnName {

    String value(); //字段名
    boolean isPrimaryKey() default false;
    String dataType() default "varchar(255)"; //数据类型


}
