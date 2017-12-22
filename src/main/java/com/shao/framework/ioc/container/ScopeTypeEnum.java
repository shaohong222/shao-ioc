package com.shao.framework.ioc.container;

public enum ScopeTypeEnum {
    PROTOTYPE("prototype"){
        @Override
        public <T> T create(Class<T> classType, Context context){
            return context.newInstance(classType);
        }
    },
    SINGLETON("singleton"){
        @Override
        public <T> T create(Class<T> classType, Context context) {
            //TODO singleton object is not like this initialization
            return context.newInstance(classType);
        }
    };

    String type;
    private ScopeTypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public abstract <T> T create(Class<T> classType, Context context);

}
