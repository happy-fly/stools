package com.kingh.core.anno;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {

	String id();

	String name() default "";

	String desc() default "";

	String next() default "";
}
