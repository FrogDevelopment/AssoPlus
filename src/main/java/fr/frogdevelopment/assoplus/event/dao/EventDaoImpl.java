/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.event.dao;

import fr.frogdevelopment.assoplus.core.dao.CommonDaoImpl;
import fr.frogdevelopment.assoplus.event.entity.Event;
import org.springframework.stereotype.Repository;

@Repository("eventDao")
public class EventDaoImpl extends CommonDaoImpl<Event> implements EventDao {

}
