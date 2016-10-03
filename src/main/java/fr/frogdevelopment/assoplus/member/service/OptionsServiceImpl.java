/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.service;

import fr.frogdevelopment.assoplus.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.member.dto.OptionDto;
import fr.frogdevelopment.assoplus.member.entity.Option;

@Service("optionsService")
public class OptionsServiceImpl extends AbstractService<Option, OptionDto> implements OptionsService {

    @Override
    protected OptionDto createDto(Option bean) {
        OptionDto dto = new OptionDto();
        dto.setId(bean.getId());
        dto.setCode(bean.getCode());
        dto.setLabel(bean.getLabel());
        dto.setDegreeCode(bean.getDegreeCode());

        return dto;
    }

    @Override
    protected Option createBean(OptionDto dto) {
        Option bean = new Option();
        bean.setId(dto.getId());
        bean.setCode(dto.getCode());
        bean.setLabel(dto.getLabel());
        bean.setDegreeCode(dto.getDegreeCode());

        return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteOption(OptionDto optionDto) {
        dao.delete(createBean(optionDto));
    }

}
