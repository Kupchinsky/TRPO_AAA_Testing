package ru.killer666.aaa.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class GsonService implements FactoryBean<Gson>, InitializingBean {
    private Gson gson;

    @Override
    public Gson getObject() throws Exception {
        return this.gson;
    }

    @Override
    public Class<?> getObjectType() {
        return Gson.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }
}
