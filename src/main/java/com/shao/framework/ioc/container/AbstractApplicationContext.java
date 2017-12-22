package com.shao.framework.ioc.container;


import com.shao.framework.ioc.Inject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 容器注册基本实现
 * @author sh
 * @date 2017-12-8
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    /**
     * 内部工厂集合 延迟加载仓库
     */
    static final Map<Key<?>, InternalFactory> factorys = new HashMap<Key<?>, InternalFactory>();

    /**
     * 单实例仓库
     */
    static final Map<Key<?>, Object> staticRepository = new HashMap<Key<?>, Object>();

    /**
     * 单例占位符
     */
    static final Object placeHolder = new Object();

    /**
     * 容器注册
     * 注册到容器中的是接口类型与类的类型，此时斌没有创建实例对象，只是封装了内部工厂
     * @param interfaceType 接口类型，面向抽象设计概念 一个接口类型可以很多 类对象创建的实例
     * @param name 注册名称，此处尽量全局唯一，对应的获取对象时，接口类型和注册名称为联合主键，不可重复
     * @param classType 实现接口的类的类对象类型
     * @param type 创建方式 可选 PROTOTYPE 原型 和SINGLETON 单例模式
     * @param <T>
     */
    @Override
    public <T> void inject(Class<T> interfaceType, String name, Class<? extends T> classType, ScopeTypeEnum type) {
        if (type == null) {
            throw new NullPointerException("initialzation type can not bi null");
        }

        //构造内部工厂
        factorys.put(Key.newInstance(name, interfaceType), new InternalFactory() {
            @Override
            public T create(Context context) {
                //使用枚举的构造方法，面向对象的设计思想
                return type.create(classType,context);
            }
        });
        //若创建模式为单例，则使用单例占位符补位
        if(type == ScopeTypeEnum.SINGLETON){
            staticRepository.put(Key.newInstance(name,interfaceType),placeHolder);
        }
    }

    /**
     * 获得容器中实例
     * 延迟加载，意义在于注册到终其中的类并没有创建实例，在获取类实例的时候才创建
     * @param interfaceType 接口类类型 注册到容器中的子类可为多个，获取实例时使用接口类型与注册名称即可
     * @param name 注入容器时的注册名
     * @param <T>
     * @return
     */
    @Override
    public <T> T getBean(Class<T> interfaceType, String name) {
        return callback(new CallBackable(){
            @Override
            public T create(Context context) {
                //获得具体的事例
                return getInstance(interfaceType,name,context);
            }
        },interfaceType,name);
    }

    /**
     * 实例创建过程
     * @param callback 具体创建实例的句柄
     * @param keyInterfaceType 接口类型
     * @param keyName 注册名称
     * @param <T>
     * @return
     */
    <T> T callback(CallBackable callback,Class<T> keyInterfaceType,String keyName){
        Context context = new Context("create root context");
        //创建联合主键
        Key<T> key = Key.newInstance(keyName, keyInterfaceType);
        //查找单实例
        Object staticObject = staticRepository.get(key);
        T instance = staticObject != placeHolder ? (T) staticObject : null;
        if(instance != null){
            //返回单实例
            return instance;
        }
        //创建新实例
        instance = callback.create(context);
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field:fields) {
            //依赖注入的实现
            if(field.isAnnotationPresent(Inject.class)){
                Class<?> interfaceType = field.getType();
                Inject inject = field.getAnnotation(Inject.class);
                String name = field.getName();
                if(!inject.value().equals(Inject.DEFAULT_NAME)){
                    name = inject.value();
                }
                field.setAccessible(true);
                try {
                    field.set(instance,getBean(interfaceType,name));
                }catch (Exception e){
                    System.out.println("reflect field failure !");
                }
            }
        }
        if(staticRepository.containsKey(key)){
            //单实例仓库缓存
            staticRepository.put(key,instance);
        }
        return instance;
    }

    /***
     * <p> 具体实例创建的实现，当前类具备实例化的能力 此处将其抽象出来 为增加灵活性
     * @param interfaceType 接口类型
     * @param name 注册名
     * @param context 创建功能上下文
     * @return
     */
    abstract <T> T getInstance(Class<T> interfaceType, String name, Context context);

    interface CallBackable {
        <T> T create(Context context);
    }

    interface InternalFactory {
        <T> T create(Context context);
    }
}

    /**
     * 联合主键的定义
     * @param <T>
     */
    class Key<T>{
        /**
         * 接口类型
         */
        Class<T> interfaceType;

        /**
         * 注册名
         */
        String name;

        public Key(){}

        public Key(String name,Class<T> interfaceType){
            this.name = name;
            this.interfaceType = interfaceType;
        }

        public Class<?> getInterfaceType(){
            return interfaceType;
        }

        public void setInterfaceType(Class<T> interfaceType){
            this.interfaceType = interfaceType;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        /**
         * 此处HashCode必须重写，典型的重写hashcode 实现自定义的比较
         */
        @Override
        public int hashCode(){
            final int prime = 31;
            int result = 1;
            result = prime * result + ((interfaceType == null) ? 0 : interfaceType.hashCode());
            result = prime * result + ((name == null) ? 0 :name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj){
                return true;
            }
            if(obj == null){
                return false;
            }
            if(getClass() != obj.getClass()){
                return false;
            }
            Key other = (Key) obj;
            if(interfaceType == null){
                if(other.interfaceType != null){
                    return false;
                }
            }else if(!interfaceType.equals(other.interfaceType)){
                return false;
            }
            if(name == null){
                if(other.name != null){
                    return false;
                }
            }else if(!name.equals(other.name)){
                return false;
            }
            return true;
        }

        static <T> Key<T> newInstance(String name, Class<T> interfaceType) {
            return new Key<T>(name, interfaceType);
        }

    }
