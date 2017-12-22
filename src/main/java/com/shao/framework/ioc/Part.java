package com.shao.framework.ioc;



import com.shao.framework.ioc.container.ScopeTypeEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Part {
	String value();
	ScopeTypeEnum scope() default ScopeTypeEnum.SINGLETON;
}
