/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.EventDto;
import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.entities.Event;
import fr.frogdevelopment.assoplus.entities.Licence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service("eventsService")
public class EventsServiceImpl extends AbstractService<Event, EventDto> implements EventsService {

    EventDto createDto(Event bean) {
        EventDto dto = new EventDto();
        dto.setId(bean.getId());

        return dto;
    }

    Event createBean(EventDto dto) {
        Event bean = new Event();
        bean.setId(dto.getId());

        return bean;
    }

}
