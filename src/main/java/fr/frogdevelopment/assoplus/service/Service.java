/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import javafx.collections.ObservableList;

import java.util.Set;

interface Service<D> {

	ObservableList<D> getAllData();

	D saveData(D dto);

	void saveAll(Set<D> dtos);

	D updateData(D dto);

	void saveOrUpdateAll(Set<D> dtos);

	void deleteData(D dto);
}
