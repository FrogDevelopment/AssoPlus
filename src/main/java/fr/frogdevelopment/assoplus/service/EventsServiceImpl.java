/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.datasource.RoutingDataSource;
import org.springframework.stereotype.Service;

import fr.frogdevelopment.assoplus.dto.EventDto;
import fr.frogdevelopment.assoplus.entities.Event;

import static fr.frogdevelopment.assoplus.datasource.RoutingDataSource.DataSource.*;

@Service("eventsService")
public class EventsServiceImpl extends AbstractService<Event, EventDto> implements EventsService {

    @Override
    protected void setContext() {
        RoutingDataSource.setDataSource(MYSQL);
    }

    EventDto createDto(Event bean) {
        EventDto dto = new EventDto();
        dto.setId(bean.getId());
        dto.setTitle(bean.getTitle());
        dto.setDate(bean.getDate());
        dto.setText(bean.getText());

        return dto;
    }

    Event createBean(EventDto dto) {
        Event bean = new Event();
        bean.setId(dto.getId());
        bean.setTitle(dto.getTitle());
        bean.setDate(dto.getDate());
        bean.setText(dto.getText());

        return bean;
    }

}
