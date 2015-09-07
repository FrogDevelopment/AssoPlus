/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.entities.Degree;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.dto.DegreeDto;

@Service("licencesService")
public class DegreeServiceImpl extends AbstractService<Degree, DegreeDto> implements DegreeService {

    DegreeDto createDto(Degree bean) {
        DegreeDto dto = new DegreeDto();
        dto.setId(bean.getId());
        dto.setCode(bean.getCode());
        dto.setLabel(bean.getLabel());

        return dto;
    }

    Degree createBean(DegreeDto dto) {
        Degree bean = new Degree();
        bean.setId(dto.getId());
        bean.setCode(dto.getCode());
        bean.setLabel(dto.getLabel());

        return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteLicence(DegreeDto degreeDto) {
        Degree degree = createBean(degreeDto);

        // FIXME à vérifier si besoin
//        degree.getOptions().stream().filter(option -> option.getId() != 0).forEach(optionDao::delete);
        dao.delete(degree);
    }

}
