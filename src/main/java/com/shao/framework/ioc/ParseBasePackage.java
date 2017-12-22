package com.shao.framework.ioc;

import com.shao.framework.ioc.container.ApplicationContext;
import com.shao.framework.ioc.container.ScopeTypeEnum;
import com.shao.framework.ioc.container.XmlApplicationContext;

import java.io.File;

/**
 * 自动扫描包，并注册到容器
 */
public class ParseBasePackage {

    static ParseBasePackage basePackage = null;

    /**
     * 是否启用子目录扫描功能
     */
    private boolean scanSub = true;

    /**
     * 原是包路径
     */
    private String originalPath = null;

    /**
     * 容器上下文
     */
    private ApplicationContext context = XmlApplicationContext.getContainer();

    public static ParseBasePackage register(String packagePath){
        return register(packagePath,true);
    }

    public static ParseBasePackage register(String packagePath,boolean sub){
        basePackage = new ParseBasePackage(packagePath,sub);
        return basePackage;
    }

    public static ApplicationContext buildPackagePathApplication(String packagePath) {
        return register(packagePath).getApplicationContext();
    }

    public static ApplicationContext buildPackagePathApplication(String packagePath, boolean scanSub) {
        return register(packagePath, scanSub).getApplicationContext();
    }

    private ParseBasePackage(String path,boolean scanSub){
        if (path == null || path.indexOf(".") < 0) {
            System.out.println("此处有异常抛出");
            return;
        }
        this.scanSub = scanSub;
        this.originalPath = path;

        path = path.replace(".", "/");
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getFile());
        scanAndRegister(file);
    }

    /**
     * 完成包扫描功能并将扫描的类注册到容器中
     * @param directory 初始包路径
     */
    void scanAndRegister(File directory){
        new ScanControlPower(){

            @Override
            public void scan(File file, String packageName) {
                File[] files = file.listFiles();
                for (File f : files) {
                    if(f.isDirectory()){
                        if(scanSub){
                            scan(f,packageName + "." + f.getName());
                        }
                    }else {
                        if(f.getName().endsWith("class")){
                            String classFullName = packageName + "." + f.getName().substring(0,f.getName().length()-6);
                            register(classFullName);
                        }
                    }
                }

            }

            String getAnnoName(Class<?> clazz){
                if(clazz.isAnnotationPresent(Part.class)){
                    return clazz.getAnnotation(Part.class).value();
                }
                return null;
            }

            ScopeTypeEnum getAnnoScope(Class<?> clazz){
                if(clazz.isAnnotationPresent(Part.class)){
                    return clazz.getAnnotation(Part.class).scope();
                }
                return null;
            }


            /**
             * 容器注册
             * @param classFullName 要注册类的全限定名
             * @param <T>
             */
            @Override
            public <T> void register(String classFullName) {
                try {
                    Class<T> clazz = (Class<T>)this.getClass().getClassLoader().loadClass(classFullName);
                    if(!clazz.isInterface()){
                        // 获得注册名
                        String injectName = getAnnoName(clazz) != null ? getAnnoName(clazz) : clazz.getSimpleName();
                        // 注册类型 默认单例
                        ScopeTypeEnum type = (getAnnoScope(clazz) != null ? getAnnoScope(clazz) : ScopeTypeEnum.SINGLETON);
                        // 类层次结构是否有更好的定义
                        for(Class<? super T> clazzs : (Class<? super T>[])clazz.getInterfaces()) {
                            getApplicationContext().inject(clazzs, injectName, clazz, type);
                        }
                        // 非接口形式注册
                        getApplicationContext().inject(clazz, injectName, clazz, type); /* 非接口形式注册 */
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("class <" + classFullName + "> is not found# could not be loaded");
                }
            }
        }.scan(directory, originalPath);

    }

    public ApplicationContext getApplicationContext() {
        return this.context;
    }

    /**
     * 完成包扫描及类注册的功能
     */
    interface ScanControlPower{
        /**
         * 包自动扫描,携带包路径,启用子目录扫描 递归追加包名称
         * @param file 包路径
         * @param packageName 包名称
         */
        void scan(File file,String packageName);

        /**
         * 注册到容器
         * @param classFullName 要注册类的全限定名
         * @param <T>
         */
        <T> void register(String classFullName);
    }

}
