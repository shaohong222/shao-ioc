package com.shao.test;


import com.shao.framework.ioc.Inject;
import com.shao.test.dao.Database;

/**
 * Action
 * 实现依赖注入, 注意@Inject("oracle") 注解的使用 @Inject("oracle") 注解可以不写参数 , 若不使用参数注解属性, 程序会取属性名作为注册名向容器获取实例
 * @author sh
 */
public class Action {
	public int i = 0;
	@Inject("mysql")
	private Database database;

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public void execute() {
		System.out.println(database.operation());
	}
	
	public static void main(String[] args) {
		System.out.println("123");
	}
}
