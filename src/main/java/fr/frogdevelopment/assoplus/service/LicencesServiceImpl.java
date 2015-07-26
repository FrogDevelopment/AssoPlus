/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.entities.Licence;
import fr.frogdevelopment.assoplus.dao.OptionDao;
import fr.frogdevelopment.assoplus.dto.LicenceDto;

import java.util.*;

@Service("licencesService")
public class LicencesServiceImpl extends AbstractService<Licence, LicenceDto> implements LicencesService {

    @Autowired
    private OptionDao optionDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Set<LicenceDto> getAllOrderedByCode() {
        List<Licence> licences = dao.getAllOrderedBy("code");

        if (licences.isEmpty()) {
	        licences = new ArrayList<>(Arrays.<Licence>asList(
                    new Licence("L1", "Licence 1"),
                    new Licence("L2", "Licence 2"),
                    new Licence("L3", "Licence 3"),
                    new Licence("M1", "Master 1"),
                    new Licence("M2", "Master 2"),
                    new Licence("D", "Doctorat")
            ));
            dao.saveAll(licences);
        }

	    return createDtos(licences);
    }

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
