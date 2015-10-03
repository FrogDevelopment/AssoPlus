/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.entities.BookOrder;
import org.springframework.stereotype.Repository;

@Repository("bookOrderDao")
public class BookOrderDaoImpl extends CommonDaoImpl<BookOrder> implements BookOrderDao {

}
