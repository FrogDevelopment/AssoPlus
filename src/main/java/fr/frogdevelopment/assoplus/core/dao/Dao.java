/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dao;

import java.util.Collection;

public interface Dao<E> {

	void deleteAll();

	Collection<E> getAll();

    void create(E entity);

	void createAll(Collection<E> entities);

	void update(E entity);

	void updateAll(Collection<E> entities) ;

	void save(E entity) ;

	void create(Collection<E> entities) ;

	void delete(E entity);

}
