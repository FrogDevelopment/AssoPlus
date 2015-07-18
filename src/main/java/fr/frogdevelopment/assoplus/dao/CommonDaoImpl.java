/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

@Transactional(propagation = Propagation.MANDATORY)
public class CommonDaoImpl<E> implements CommonDao<E> {

	@Autowired
	private SessionFactory sessionFactory;

	private final Class<E> persistentClass;

	@SuppressWarnings("unchecked")
	public CommonDaoImpl() {
		persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	// ******************************************* \\
	// ********** PROTECTED METHODES ************* \\
	// ******************************************* \\

	protected Session getSession() throws HibernateException {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * @return Le nom de l'entité du DAO.
	 * @see Class#getSimpleName()
	 */
	protected final String getEntityName() {
		return this.persistentClass.getSimpleName();
	}

	/**
	 * exemple : <br>
	 * doWork(connection -> connection.createStatement().executeUpdate("DELETE FROM members"));
	 */
	protected void doWork(Work work) throws HibernateException {
		getSession().doWork(work);
	}

	protected Criteria getCriteria() {
		return getSession().createCriteria(persistentClass);
	}
	protected Criteria getCriteria(String alias) {
		return getSession().createCriteria(persistentClass, alias);
	}

	// **************************************** \\
	// ********** PUBLIC METHODES ************* \\
	// **************************************** \\

	@Override
	public void deleteAll() throws HibernateException {
		getSession().createQuery("DELETE FROM " + getEntityName()).executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<E> getAll() throws HibernateException {
		return getCriteria().list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public E getById(Serializable identifiant) throws HibernateException {
		return (E) getSession().get(persistentClass, identifiant);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<E> getAllOrderedBy(String propertyName) throws HibernateException {
		return getCriteria().addOrder(Order.asc(propertyName)).list();
	}

	@Override
	public void save(E entity) throws HibernateException {
		getSession().save(entity);
	}

	@Override
	public void saveAll(Set<E> entities) {
		entities.forEach(this::save);
	}

	@Override
	public void update(E entity) throws HibernateException {
		getSession().update(entity);
	}

	@Override
	public void updateAll(Set<E> entities) throws HibernateException {
		entities.forEach(this::update);
	}

	@Override
	public void saveOrUpdate(E entity) throws HibernateException {
		getSession().saveOrUpdate(entity);
	}

	@Override
	public void saveOrUpdateAll(Set<E> entities) throws HibernateException {
		entities.forEach(this::update);
	}

	@Override
	public void delete(E entity) throws HibernateException {
		getSession().delete(entity);
	}

	@Override
	public void deleteAll(Set<E> entities) throws HibernateException {
		entities.forEach(this::delete);
	}

}
