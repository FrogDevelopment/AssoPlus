/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.bean.Licence;
import fr.frogdevelopment.assoplus.bean.Option;
import fr.frogdevelopment.assoplus.dao.OptionDao;
import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("licencesService")
public class LicencesServiceImpl extends AbstractService<Licence, LicenceDto> implements LicencesService {

    @Autowired
    private OptionDao optionDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<LicenceDto> getAllOrderedByCode() {
        List<Licence> licences = dao.getAllOrderedBy("code");

        Set<LicenceDto> licencesDto;
        if (licences.isEmpty()) {
            licencesDto = new HashSet<>(Arrays.<LicenceDto>asList(
                    new LicenceDto("L1", "Licence 1"),
                    new LicenceDto("L2", "Licence 2"),
                    new LicenceDto("L3", "Licence 3"),
                    new LicenceDto("M1", "Master 1"),
                    new LicenceDto("M2", "Master 2"),
                    new LicenceDto("D", "Doctorat")
            ));

        } else {
            licencesDto = createDtos(licences);
        }

        return licencesDto;
    }

    LicenceDto createDto(Licence bean) {
        LicenceDto dto = new LicenceDto();
        dto.setId(bean.getId());
        dto.setCode(bean.getCode());
        dto.setLabel(bean.getLabel());
        dto.getOptions().addAll(createOptionDtos(bean.getOptions()));

        return dto;
    }

    Licence createBean(LicenceDto dto) {
        Licence bean = new Licence();
        bean.setId(dto.getId());
        bean.setCode(dto.getCode());
        bean.setLabel(dto.getLabel());
        bean.setOptions(createOptionBeans(dto.getOptions(), bean));

        return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteOption(LicenceDto licenceDto, OptionDto optionDto) {
        Licence licence = createBean(licenceDto);
        Option option = createOptionBean(licence, optionDto);

        optionDao.delete(option);
    }

    private Set<OptionDto> createOptionDtos(Collection<Option> beans) {
        return beans.stream().map((bean) -> {
            OptionDto dto = new OptionDto();
            dto.setId(bean.getId());
            dto.setCode(bean.getCode());
            dto.setLabel(bean.getLabel());

            return dto;
        }).collect(Collectors.toSet());
    }

    private Set<Option> createOptionBeans(Collection<OptionDto> dtos, Licence licence) {
        return dtos.stream().map((dto) -> createOptionBean(licence, dto)).collect(Collectors.toSet());
    }

    private Option createOptionBean(Licence licence, OptionDto dto) {
        Option bean = new Option();
        bean.setId(dto.getId());
        bean.setCode(dto.getCode());
        bean.setLabel(dto.getLabel());
        bean.setLicence(licence);

        return bean;
    }

}
