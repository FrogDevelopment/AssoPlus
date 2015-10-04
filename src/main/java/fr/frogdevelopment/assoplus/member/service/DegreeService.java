/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.service;

import fr.frogdevelopment.assoplus.core.service.Service;
import fr.frogdevelopment.assoplus.member.dto.DegreeDto;

public interface DegreeService extends Service<DegreeDto> {

	void deleteLicence(DegreeDto degreeDto);

}
