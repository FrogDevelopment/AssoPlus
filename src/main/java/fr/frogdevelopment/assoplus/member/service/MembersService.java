/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.service;

import fr.frogdevelopment.assoplus.core.service.Service;
import fr.frogdevelopment.assoplus.member.dto.MemberDto;

public interface MembersService extends Service<MemberDto> {

    void updateSubscription(MemberDto dto);

    void updateAnnals(MemberDto dto);
}
