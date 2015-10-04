/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dao;

import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.Collection;
import java.util.List;

public interface CommonDao<E> {

	void deleteAll();

	List<E> getAll();

	E getById(Integer identifiant) ;

	void save(E entity);

	void saveAll(Collection<E> entities);

	void update(E entity);

	void updateAll(Collection<E> entities) ;

	void saveOrUpdate(E entity) ;

	void saveOrUpdateAll(Collection<E> entities) ;

	void delete(E entity);

}
