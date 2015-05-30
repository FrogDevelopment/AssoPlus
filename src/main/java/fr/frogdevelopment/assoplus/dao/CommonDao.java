package fr.frogdevelopment.assoplus.dao;

import org.hibernate.HibernateException;

import java.io.Serializable;
import java.util.List;

public interface CommonDao<E> {

	void deleteAll();

	List<E> getAll();

	E getById(Serializable identifiant) throws HibernateException;

	List<E> getAllOrderedBy(String propertyName);

	void save(E entity);

	void update(E entity);

	void delete(E entity);

}
