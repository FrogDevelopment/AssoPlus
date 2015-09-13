/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.EventDto;
import fr.frogdevelopment.assoplus.entities.Event;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface EventsService extends Service<EventDto> {

    boolean publishEvent(EventDto eventDto);

}
