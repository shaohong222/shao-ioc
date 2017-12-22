package com.shao.framework.ioc.container;

/**
 * 提供默认的实现
 * @author sh
 */
public class XmlApplicationContext extends AbstractApplicationContext {

    private XmlApplicationContext(){}

    private static class SingleHolder{
       static final ApplicationContext container = new XmlApplicationContext();
    }

    public static ApplicationContext getContainer(){
        return SingleHolder.container;
    }

    /**
     * 父类的默认实现
     * @param interfaceType 接口类型
     * @param name 注册名
     * @param context 创建功能上下文
     * @param <T>
     * @return
     */
    @Override
    <T> T getInstance(Class<T> interfaceType, String name, Context context) {
        InternalFactory factory = factorys.get(Key.newInstance(name, interfaceType));
        if(factory == null){
            throw new NullPointerException("internal factory is null, initialzation failed !");
        }
        return factory.create(context);
    }
}
