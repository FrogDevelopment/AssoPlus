/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.entities.Book;
import fr.frogdevelopment.assoplus.entities.Member;
import org.springframework.stereotype.Repository;

@Repository("bookDao")
public class BookDaoImpl extends CommonDaoImpl<Book> implements BookDao {

}
