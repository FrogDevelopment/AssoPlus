/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.hibernate.HibernateException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CommonDao<E> {

	void deleteAll();

	List<E> getAll();

	E getById(Serializable identifiant) throws HibernateException;

	List<E> getAllOrderedBy(String propertyName);

	void save(E entity);

	void saveAll(Collection<E> entities);

	void update(E entity);

	void updateAll(Collection<E> entities) throws HibernateException;

	void saveOrUpdate(E entity) throws HibernateException;

	void saveOrUpdateAll(Collection<E> entities) throws HibernateException;

	void delete(E entity);

	void deleteAll(Collection<E> entities) throws HibernateException;
}
