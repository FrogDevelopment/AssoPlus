/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.service;

import java.util.Collection;
import java.util.List;

public interface Service<D> {

	List<D> getAll();

	void saveData(D dto);

	void saveAll(Collection<D> dtos);

	void updateData(D dto);

	void saveOrUpdateAll(Collection<D> dtos);

	void deleteData(D dto);
}
