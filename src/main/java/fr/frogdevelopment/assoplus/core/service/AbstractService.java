/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.service;

import fr.frogdevelopment.assoplus.core.dao.CommonDao;
import fr.frogdevelopment.assoplus.core.dto.Dto;
import fr.frogdevelopment.assoplus.core.entity.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractService<E extends Entity, D extends Dto> implements Service<D> {

    @Autowired
    protected CommonDao<E> dao;

    @Override
    @Transactional(value = "sqlite", propagation = Propagation.REQUIRED)
    public List<D> getAll() {
        return dao.getAll().stream().map(this::createDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "sqlite", propagation = Propagation.REQUIRED)
    public D saveData(D dto) {
        E bean = createBean(dto);
        dao.save(bean);
        dto.setId(bean.getId());

        return dto;
    }

    @Override
    @Transactional(value = "sqlite", propagation = Propagation.REQUIRED)
    public void saveAll(Collection<D> dtos) {
        dao.saveAll(createBeans(dtos));
    }

    @Override
    @Transactional(value = "sqlite", propagation = Propagation.REQUIRED)
    public D updateData(D dto) {
        E bean = createBean(dto);
        dao.update(bean);
        dto.setId(bean.getId());

        return dto;
    }

    @Override
    @Transactional(value = "sqlite", propagation = Propagation.REQUIRED)
    public void saveOrUpdateAll(Collection<D> dtos) {
        dao.saveOrUpdateAll(createBeans(dtos));
    }

    @Override
    @Transactional(value = "sqlite", propagation = Propagation.REQUIRED)
    public void deleteData(D dto) {
        dao.delete(createBean(dto));
    }

    protected abstract D createDto(E entity);

    protected Set<D> createDtos(Collection<E> beans) {
        return beans.stream().map(this::createDto).collect(Collectors.toSet());
    }

    protected abstract E createBean(D dto);

    protected Set<E> createBeans(Collection<D> dtos) {
        return dtos.stream().map(this::createBean).collect(Collectors.toSet());
    }

}
