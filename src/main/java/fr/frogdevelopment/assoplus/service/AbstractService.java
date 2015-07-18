/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dao.CommonDao;
import fr.frogdevelopment.assoplus.dto.Dto;
import fr.frogdevelopment.assoplus.entities.Entity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractService<E extends Entity, D extends Dto> implements fr.frogdevelopment.assoplus.service.Service<D> {

    @Autowired
    protected CommonDao<E> dao;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ObservableList<D> getAllData() {
        ObservableList<D> data = FXCollections.observableArrayList();

        List<E> entities = dao.getAll();

        entities.forEach(member -> data.add(createDto(member)));

        return data;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public D saveData(D dto) {
        E bean = createBean(dto);
        dao.save(bean);
        dto.setId(bean.getId());

        return dto;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveAll(Collection<D> dtos) {
        dao.saveAll(createBeans(dtos));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public D updateData(D dto) {
        E bean = createBean(dto);
        dao.update(bean);
        dto.setId(bean.getId());

        return dto;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveOrUpdateAll(Collection<D> dtos) {
        dao.saveOrUpdateAll(createBeans(dtos));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteData(D dto) {
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
