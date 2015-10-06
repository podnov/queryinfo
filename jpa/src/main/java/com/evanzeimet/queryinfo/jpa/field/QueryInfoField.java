package com.evanzeimet.queryinfo.jpa.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface QueryInfoField {

	public String fieldName() default "";

	public boolean isQueryable() default true;

	public boolean isResult() default true;

}
