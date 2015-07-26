/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.entities.Option;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service("optionsService")
public class OptionsServiceImpl extends AbstractService<Option, OptionDto> implements OptionsService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Set<OptionDto> getAllOrderedByCode() {
	    return createDtos(dao.getAllOrderedBy("code"));
    }

    OptionDto createDto(Option bean) {
	    OptionDto dto = new OptionDto();
	    dto.setId(bean.getId());
	    dto.setCode(bean.getCode());
	    dto.setLabel(bean.getLabel());
	    dto.setLicenceCode(bean.getLicenceCode());

	    return dto;
    }

    Option createBean(OptionDto dto) {Option bean = new Option();
	    bean.setId(dto.getId());
	    bean.setCode(dto.getCode());
	    bean.setLabel(dto.getLabel());
	    bean.setLicenceCode(dto.getLicenceCode());

	    return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteOption(OptionDto optionDto) {
        dao.delete(createBean(optionDto));
    }

}
