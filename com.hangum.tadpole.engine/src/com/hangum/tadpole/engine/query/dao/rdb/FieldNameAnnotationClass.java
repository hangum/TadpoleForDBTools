package com.hangum.tadpole.engine.query.dao.rdb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldNameAnnotationClass {
	String fieldKey();
}
