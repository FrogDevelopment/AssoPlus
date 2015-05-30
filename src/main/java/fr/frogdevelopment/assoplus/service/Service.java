package fr.frogdevelopment.assoplus.service;

import javafx.collections.ObservableList;

interface Service<E, D> {

	ObservableList<D> getAllData();

	void saveData(D dto);

	void updateData(D dto);

	void deleteData(D dto);
}
