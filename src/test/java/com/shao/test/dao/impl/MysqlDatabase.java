package com.shao.test.dao.impl;


import com.shao.framework.ioc.Part;
import com.shao.test.dao.Database;

@Part("mysql")
public class MysqlDatabase implements Database {
	@Override
	public String operation() {
		return "mysql operation success !";
	}
}