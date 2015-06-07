/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dao.CommonDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

abstract class AbstractService<E,D> implements fr.frogdevelopment.assoplus.service.Service<E,D> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

	@Autowired
	private CommonDao<E> dao;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ObservableList<D> getAllData() {
		ObservableList<D> data = FXCollections.observableArrayList();

		List<E> entities = dao.getAll();

		entities.forEach(member -> data.add(createDTO(member)));

		return data;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveData(D dto) {
		dao.save(createBean(dto));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateData(D dto) {
		dao.update(createBean(dto));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteData(D dto) {
		dao.delete(createBean(dto));
	}

	abstract protected D createDTO(E entity) ;

	abstract protected E createBean(D dto) ;

}
