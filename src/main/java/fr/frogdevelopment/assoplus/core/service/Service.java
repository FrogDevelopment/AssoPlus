/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.service;

import java.util.Collection;

public interface Service<D> {

	Collection<D> getAll();

	void saveData(D dto);

	void createAll(Collection<D> dtos);

	void updateData(D dto);

	void save(Collection<D> dtos);

	void deleteData(D dto);
}
