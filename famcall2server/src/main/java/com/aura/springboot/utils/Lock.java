package com.aura.springboot.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
 
	/**
	 * The {@link LockModeType} to be used when executing the annotated query or CRUD method.
	 * 
	 * @return
	 */
	LockModeType value();
}