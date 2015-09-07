/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.DegreeDto;

public interface DegreeService extends Service<DegreeDto> {

	void deleteLicence(DegreeDto degreeDto);

}
