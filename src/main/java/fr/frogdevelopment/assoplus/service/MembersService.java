/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.entities.SchoolYear;
import fr.frogdevelopment.assoplus.dto.MemberDto;

import java.io.File;

public interface MembersService extends Service<MemberDto> {

    SchoolYear getCurrentSchoolYear();

    void importMembers(File file);
}
