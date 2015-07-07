/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.bean.Licence;
import fr.frogdevelopment.assoplus.bean.Option;
import fr.frogdevelopment.assoplus.dao.LicenceDao;
import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("licencesService")
public class LicencesServiceImpl extends AbstractService<Licence, LicenceDto> implements LicencesService {

	@Autowired
	private LicenceDao licenceDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Set<LicenceDto> getAllOrderedByCode() {
		List<Licence> licences = licenceDao.getAllOrderedBy("code");

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
			licencesDto = createDTOs(licences);
		}

		return licencesDto;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(Set<LicenceDto> dtos) {
		Set<Licence> licences = createBeans(dtos);
		licenceDao.saveAll(licences);
	}

	public LicenceDto createDTO(Licence bean) {
		LicenceDto dto = new LicenceDto();
		dto.setId(bean.getId());
		dto.setCode(bean.getCode());
		dto.setLabel(bean.getLabel());
		dto.getOptions().addAll(fromBeans(bean.getOptions()));

		return dto;
	}

	public Licence createBean(LicenceDto dto) {
		Licence bean = new Licence();
		bean.setId(dto.getId());
		bean.setCode(dto.getCode());
		bean.setLabel(dto.getLabel());
		bean.setOptions(fromDtos(dto.getOptions()));

		return bean;
	}

	public Set<OptionDto> fromBeans(Collection<Option> beans) {
		return beans.stream().map(this::fromBean).collect(Collectors.toSet());
	}

	public OptionDto fromBean(Option bean) {
		OptionDto dto = new OptionDto();
		dto.setId(bean.getId());
		dto.setCode(bean.getCode());
		dto.setLabel(bean.getLabel());

		return dto;
	}

	public Set<Option> fromDtos(Collection<OptionDto> dtos) {
		return dtos.stream().map(this::fromDto).collect(Collectors.toSet());
	}

	public Option fromDto(OptionDto dto) {
		Option bean = new Option();
		bean.setId(dto.getId());
		bean.setCode(dto.getCode());
		bean.setLabel(dto.getLabel());

		return bean;
	}

}
