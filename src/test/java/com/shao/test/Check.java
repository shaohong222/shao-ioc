package com.shao.test;

import com.shao.framework.ioc.Before;

import java.lang.reflect.Method;

public class Check {
	
	@Before("com.shao.test.Action")
	public void before(Method method,Object[] args){
		if(method.getName().indexOf("execute")>-1){
			System.out.println("before ----");
		}

	}

}
