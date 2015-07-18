/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;

import java.util.Set;

public interface LicencesService extends Service<LicenceDto> {


	Set<LicenceDto> getAllOrderedByCode();

	//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	void deleteLicence(LicenceDto licenceDto);

	void deleteOption(LicenceDto licenceDto, OptionDto optionDto);
}
