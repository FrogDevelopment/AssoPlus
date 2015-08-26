/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.entities.Licence;

@Service("licencesService")
public class LicencesServiceImpl extends AbstractService<Licence, LicenceDto> implements LicencesService {

    LicenceDto createDto(Licence bean) {
        LicenceDto dto = new LicenceDto();
        dto.setId(bean.getId());
        dto.setCode(bean.getCode());
        dto.setLabel(bean.getLabel());

        return dto;
    }

    Licence createBean(LicenceDto dto) {
        Licence bean = new Licence();
        bean.setId(dto.getId());
        bean.setCode(dto.getCode());
        bean.setLabel(dto.getLabel());

        return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteLicence(LicenceDto licenceDto) {
        Licence licence = createBean(licenceDto);

        // FIXME à vérifier si besoin
//        licence.getOptions().stream().filter(option -> option.getId() != 0).forEach(optionDao::delete);
        dao.delete(licence);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteOption(LicenceDto licenceDto) {
        dao.delete(createBean(licenceDto));
    }

}
