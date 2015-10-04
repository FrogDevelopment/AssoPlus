/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.event.service;

import fr.frogdevelopment.assoplus.core.service.AbstractService;
import fr.frogdevelopment.assoplus.event.dao.PublishDao;
import fr.frogdevelopment.assoplus.event.dto.EventDto;
import fr.frogdevelopment.assoplus.event.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("eventsService")
public class EventsServiceImpl extends AbstractService<Event, EventDto> implements EventsService {

    @Autowired
    private PublishDao publishDao;

    @Override
    protected EventDto createDto(Event bean) {
        EventDto dto = new EventDto();
        dto.setId(bean.getId());
        dto.setTitle(bean.getTitle());
        dto.setDate(bean.getDate());
        dto.setText(bean.getText());
        dto.setPublished(1 == bean.getPublished());

        return dto;
    }

    @Override
    protected Event createBean(EventDto dto) {
        Event bean = new Event();
        bean.setId(dto.getId());
        bean.setTitle(dto.getTitle());
        bean.setDate(dto.getDate());
        bean.setText(dto.getText());
        bean.setPublished(dto.getPublished() ? 1 : 0);

        return bean;
    }

    @Override
    @Transactional(value = "mysql", propagation = Propagation.REQUIRES_NEW)
    public boolean publishEvent(EventDto eventDto) {
        Event bean = createBean(eventDto);

        return publishDao.publish(bean);
    }

}
