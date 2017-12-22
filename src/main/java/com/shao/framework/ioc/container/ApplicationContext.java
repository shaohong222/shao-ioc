package com.shao.framework.ioc.container;

/**
 * IOC 容器接口 获取系统中对象创建的控制权，需要注入
 * @author sh
 * @date 2017-12-8
 */
public interface ApplicationContext {

    /**
     * 注入
     * 向容器注入要管理的类对象 需要接口类型面向
     * @param interfaceType 接口类型，面向抽象设计概念 一个接口类型可以很多 类对象创建的实例
     * @param name 注册名称，此处尽量全局唯一，对应的获取对象时，接口类型和注册名称为联合主键，不可重复
     * @param classType 实现接口的类的类对象类型
     * @param type 创建方式 可选 PROTOTYPE 原型 和SINGLETON 单例模式
     * @param <T>
     */
    <T> void inject(Class<T> interfaceType, String name, Class<? extends T> classType, ScopeTypeEnum type);

    /**
     * 向容器获取实例
     * 根据接口类类型与注册名的联合主键，全局唯一，获取实例
     * @param interfaceType 接口类类型
     * @param name 注入容器时的注册名
     * @param <T>
     * @return 接口类型的实例对象
     */
    <T> T getBean(Class<T> interfaceType, String name);

}
