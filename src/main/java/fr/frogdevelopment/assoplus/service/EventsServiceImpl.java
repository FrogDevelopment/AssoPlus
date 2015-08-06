/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import org.springframework.stereotype.Service;

import fr.frogdevelopment.assoplus.dto.EventDto;
import fr.frogdevelopment.assoplus.entities.Event;

@Service("eventsService")
public class EventsServiceImpl extends AbstractService<Event, EventDto> implements EventsService {

    EventDto createDto(Event bean) {
        EventDto dto = new EventDto();
        dto.setId(bean.getId());
        dto.setTitle(bean.getTitle());
        dto.setDate(bean.getDate());
        dto.setText(bean.getText());
        dto.setCategoryCode(bean.getCategoryCode());

        return dto;
    }

    Event createBean(EventDto dto) {
        Event bean = new Event();
        bean.setId(dto.getId());
        bean.setTitle(dto.getTitle());
        bean.setDate(dto.getDate());
        bean.setText(dto.getText());
        bean.setCategoryCode(dto.getCategoryCode());

        return bean;
    }

}
