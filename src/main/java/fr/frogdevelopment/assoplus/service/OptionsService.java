/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.OptionDto;

import java.util.Set;

public interface OptionsService extends Service<OptionDto> {

	Set<OptionDto> getAllOrderedByCode();

	void deleteOption(OptionDto optionDto);
}
