/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.event.service;

import fr.frogdevelopment.assoplus.core.service.Service;
import fr.frogdevelopment.assoplus.event.dto.EventDto;

public interface EventsService extends Service<EventDto> {

    boolean publishEvent(EventDto eventDto);

}
