/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dao.CommonDao;
import fr.frogdevelopment.assoplus.datasource.RoutingDataSource;
import fr.frogdevelopment.assoplus.dto.Dto;
import fr.frogdevelopment.assoplus.entities.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.frogdevelopment.assoplus.datasource.RoutingDataSource.DataSource.*;

abstract class AbstractService<E extends Entity, D extends Dto> implements fr.frogdevelopment.assoplus.service.Service<D> {

    @Autowired
    protected CommonDao<E> dao;

    protected void setContext() {
        RoutingDataSource.setDataSource(SQLITE);
    }

    @Override
    public List<D> getAll() {
        setContext();
        return _getAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    private List<D> _getAll() {
        return dao.getAll().stream().map(this::createDto).collect(Collectors.toList());
    }

    @Override
    public D saveData(D dto) {
        setContext();
        return _saveData(dto);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private D _saveData(D dto) {
        E bean = createBean(dto);
        dao.save(bean);
        dto.setId(bean.getId());

        return dto;
    }

    @Override
    public void saveAll(Collection<D> dtos) {
        setContext();
        _saveAll(dtos);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void _saveAll(Collection<D> dtos) {
        dao.saveAll(createBeans(dtos));
    }

    @Override
    public D updateData(D dto) {
        setContext();
        return _updateData(dto);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private D _updateData(D dto) {
        E bean = createBean(dto);
        dao.update(bean);
        dto.setId(bean.getId());

        return dto;
    }

    @Override
    public void saveOrUpdateAll(Collection<D> dtos) {
        setContext();
        _saveOrUpdateAll(dtos);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void _saveOrUpdateAll(Collection<D> dtos) {
        dao.saveOrUpdateAll(createBeans(dtos));
    }

    @Override
    public void deleteData(D dto) {
        setContext();
        _deleteData(dto);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private void _deleteData(D dto) {
        dao.delete(createBean(dto));
    }

    abstract D createDto(E entity);

    Set<D> createDtos(Collection<E> beans) {
        return beans.stream().map(this::createDto).collect(Collectors.toSet());
    }

    abstract E createBean(D dto);

    Set<E> createBeans(Collection<D> dtos) {
        return dtos.stream().map(this::createBean).collect(Collectors.toSet());
    }

}
