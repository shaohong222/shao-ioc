package com.shao.test.dao.impl;


import com.shao.framework.ioc.Part;
import com.shao.test.dao.Database;

/**
 * OracleDatabase
 *  Database实现类 类似spring 中的 依赖注入,
 * 	此处可以不写Part注解  默认会用 简单(非全限定)类名 注册到容器 如本类不写Part注解 则容器默认取 OracleDatabase 作为注册名
 * 	此处的 @Part("oracle") 中的 oracle 对应 在 Action 类中的 @Inject("oracle"),意为 将 OracleDatabase 类的实例
 * 	注入到 Action 类的 database 属性中(注:database是接口类型)
 */
@Part("oracle")
public class OracleDatabase implements Database {
	@Override
	public String operation() {
		return "oracle operation success !";
	}
}