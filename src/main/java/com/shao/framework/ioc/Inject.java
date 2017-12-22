package com.shao.framework.ioc;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface Inject {
    static final String DEFAULT_NAME = "default";

    String value() default DEFAULT_NAME;

    boolean required() default true;
}
