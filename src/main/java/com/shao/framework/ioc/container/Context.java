package com.shao.framework.ioc.container;

/**
 * 实例创建功能上下文，可构造自定义环境
 * @author sh
 * 2015-12-8
 */
public class Context {
    private  String defaultName;

    public Context(){}

    public Context(String name){
        this.defaultName = name;
    }

    public <T> T newInstance(Class<T> classType){
        try {
            return (T) (classType).newInstance();
        } catch (Exception e){
            throw new NullPointerException("class type is null init error !");
        }
    }
}
