package com.shao.framework.ioc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CompentScan {

    private final List<String > list = new ArrayList<>();

    public void scan(String packageName){
        String s = packageName.replace(".", "/");
        File f = new File(Thread.currentThread().getContextClassLoader()
                .getResource(s).getFile());

        for (File c:f.listFiles()) {
            if(c.getName().endsWith(".class")){
                list.add(packageName+"."+c.getName().substring(0,c.getName().lastIndexOf(".")));

            }
        }
    }

    public List<String> getList(){
        return list;
    }

}
