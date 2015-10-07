package com.evanzeimet.queryinfo.jpa.field;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface QueryInfoField {

	public String fieldName() default "";

	public boolean isQueryable() default true;

	public boolean isSelectable() default true;

	public boolean isSortable() default true;

}
