/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.core.entity.Entity;

import java.util.Collection;
import java.util.List;

@Transactional(propagation = Propagation.MANDATORY)
public abstract class AbstractDaoImpl<E extends Entity> {

    protected final NamedParameterJdbcTemplate jdbcTemplate;
    protected final KeyHolder keyHolder = new GeneratedKeyHolder();

    @Autowired
    public AbstractDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // **************************************** \\
    // ********** ABSTRACT METHODES *********** \\
    // **************************************** \\

    @Transactional(readOnly = true)
    abstract public E getById(Integer identifiant);

    @Transactional(readOnly = true)
    abstract public List<E> getAll();

    abstract public void save(E entity);

    abstract public void update(E entity);

    abstract public void delete(Integer identifiant);

    abstract public void deleteAll();

    abstract protected MapSqlParameterSource toSqlParameterSource(E entity);

    public void saveAll(Collection<E> entities) {
        entities.forEach(this::save);
    }

    public void updateAll(Collection<E> entities) {
        entities.forEach(this::update);
    }

    public void saveOrUpdate(E entity) {
        if (entity.getId() == null || entity.getId() == 0) {
            save(entity);
        } else {
            update(entity);
        }
    }

    public void saveOrUpdateAll(Collection<E> entities) {
        entities.forEach(this::saveOrUpdate);
    }

    public void delete(E entity) {
        delete(entity.getId());
    }

}
