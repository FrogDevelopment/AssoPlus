/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.service;

import fr.frogdevelopment.assoplus.core.service.Service;
import fr.frogdevelopment.assoplus.member.dto.Member;

public interface MembersService extends Service<Member> {

    void updateSubscription(Member member);

    void updateAnnals(Member member);
}
