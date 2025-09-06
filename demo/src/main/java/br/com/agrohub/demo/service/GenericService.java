package br.com.agrohub.demo.service;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericService<E, D> {

    protected abstract E toEntity(D dto);

    protected abstract D toDTO(E entity);

    protected List<D> toListDTO(List<E> entityList) {
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
