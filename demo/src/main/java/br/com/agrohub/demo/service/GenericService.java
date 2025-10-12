package br.com.agrohub.demo.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericService<T,D> {
    // T -  "table" ou seja a ENTIDADE
    // D -  "dados entrada e saída" ou seja DTO

    @Autowired
    protected ModelMapper mapper;

    private Type t, d;

    public GenericService() {
        super();
        t = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        d = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];        
    }


    protected D toDTO(T entity){
        D dto = mapper.map(entity, d);
        return dto;
    }
    

    protected T toEntity(D dto){
        T entity = mapper.map(dto, t);
        return entity;
    }
}
