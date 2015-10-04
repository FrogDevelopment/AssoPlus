/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.event.dao;

import fr.frogdevelopment.assoplus.event.entity.Event;

public interface PublishDao {

    boolean publish(Event event);
}
