/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.dao;

import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.book.entity.BookOrder;

import java.util.Collection;
import java.util.List;

@Repository("bookOrderDao")
public class BookOrderDaoImpl implements BookOrderDao {

    @Override
    public void deleteAll() {

    }

    @Override
    public List<BookOrder> getAll() {
        return null;
    }

    @Override
    public BookOrder getById(Integer identifiant) {
        return null;
    }

    @Override
    public void save(BookOrder entity) {

    }

    @Override
    public void saveAll(Collection<BookOrder> entities) {

    }

    @Override
    public void update(BookOrder entity) {

    }

    @Override
    public void updateAll(Collection<BookOrder> entities) {

    }

    @Override
    public void saveOrUpdate(BookOrder entity) {

    }

    @Override
    public void saveOrUpdateAll(Collection<BookOrder> entities) {

    }

    @Override
    public void delete(BookOrder entity) {

    }
}
