/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.bean.Member;
import fr.frogdevelopment.assoplus.bean.SchoolYear;
import fr.frogdevelopment.assoplus.dto.MemberDtok;

public interface MembersService extends Service<Member, MemberDtok> {

    SchoolYear getCurrentSchoolYear();
}
