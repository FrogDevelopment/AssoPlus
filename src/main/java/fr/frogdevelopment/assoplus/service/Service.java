/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.Set;

interface Service<D> {

	ObservableList<D> getAllData();

	D saveData(D dto);

	void saveAll(Collection<D> dtos);

	D updateData(D dto);

	void saveOrUpdateAll(Collection<D> dtos);

	void deleteData(D dto);
}
