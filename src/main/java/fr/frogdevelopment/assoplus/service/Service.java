/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import javafx.collections.ObservableList;

interface Service<E, D> {

	ObservableList<D> getAllData();

	D saveData(D dto);

	D updateData(D dto);

	void deleteData(D dto);
}
