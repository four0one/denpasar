package com.plumeria.denpasar.util;

import java.lang.annotation.*;

/**
 * Created by chenwei on 2016/9/1.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRepeatSubmit {

    int time() default 10;

    String value() default "";

    String[] keys();

    boolean fix() default false;
}
