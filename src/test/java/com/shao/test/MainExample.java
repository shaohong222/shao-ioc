package com.shao.test;


import com.shao.framework.ioc.ParseBasePackage;
import com.shao.framework.ioc.container.ApplicationContext;

public class MainExample {
	public static void main(String[] args) {
		ApplicationContext context = ParseBasePackage.buildPackagePathApplication("com.shao.test");
		Action action = context.getBean(Action.class, "Action");
		action.execute();
	}
}
