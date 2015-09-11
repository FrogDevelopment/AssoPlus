/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.entities.Event;

//@Repository("eventDao")
public class EventDaoImpl extends CommonDaoImpl<Event> implements EventDao {

    public EventDaoImpl() {
        skipCreate = true;
    }
}
