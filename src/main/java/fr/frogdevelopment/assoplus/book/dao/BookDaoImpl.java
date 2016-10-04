/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.dao;

import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.book.entity.Book;

import java.util.Collection;
import java.util.List;

@Repository("bookDao")
public class BookDaoImpl implements BookDao {

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Book> getAll() {
        return null;
    }

    @Override
    public void create(Book entity) {

    }

    @Override
    public void createAll(Collection<Book> entities) {

    }

    @Override
    public void update(Book entity) {

    }

    @Override
    public void updateAll(Collection<Book> entities) {

    }

    @Override
    public void save(Book entity) {

    }

    @Override
    public void create(Collection<Book> entities) {

    }

    @Override
    public void delete(Book entity) {

    }
}
