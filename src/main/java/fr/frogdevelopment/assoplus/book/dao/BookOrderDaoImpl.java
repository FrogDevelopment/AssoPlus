/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.dao;

import fr.frogdevelopment.assoplus.core.dao.CommonDaoImpl;
import fr.frogdevelopment.assoplus.book.entity.BookOrder;
import org.springframework.stereotype.Repository;

@Repository("bookOrderDao")
public class BookOrderDaoImpl extends CommonDaoImpl<BookOrder> implements BookOrderDao {

}
