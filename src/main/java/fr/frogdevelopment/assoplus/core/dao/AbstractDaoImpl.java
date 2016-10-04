/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.core.dto.Entity;

import java.util.Collection;

@Transactional(propagation = Propagation.MANDATORY)
public abstract class AbstractDaoImpl<E extends Entity> implements Dao<E> {


    // **************************************** \\
    // ********** ABSTRACT METHODES *********** \\
    // **************************************** \\

    @Transactional(readOnly = true)
    abstract public Collection<E> getAll();

    abstract public void create(E entity);

    abstract public void update(E entity);

    abstract public void delete(Integer identifiant);

    abstract public void deleteAll();

    public void createAll(Collection<E> entities) {
        entities.forEach(this::create);
    }

    public void updateAll(Collection<E> entities) {
        entities.forEach(this::update);
    }

    public void save(E entity) {
        if (entity.isToDelete()) {
            delete(entity);
        } else if (entity.getId() == null) {
            create(entity);
        } else {
            update(entity);
        }
    }

    public void create(Collection<E> entities) {
        entities.forEach(this::save);
    }

    public void delete(E entity) {
        delete(entity.getId());
    }

}
