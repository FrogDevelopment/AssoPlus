/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.dao;

import fr.frogdevelopment.assoplus.core.dao.CommonDaoImpl;
import fr.frogdevelopment.assoplus.book.entity.Book;
import org.springframework.stereotype.Repository;

@Repository("bookDao")
public class BookDaoImpl extends CommonDaoImpl<Book> implements BookDao {

}
