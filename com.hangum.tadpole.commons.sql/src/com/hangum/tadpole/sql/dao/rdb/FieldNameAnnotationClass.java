package com.hangum.tadpole.sql.dao.rdb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldNameAnnotationClass {
	String fieldKey();
}
