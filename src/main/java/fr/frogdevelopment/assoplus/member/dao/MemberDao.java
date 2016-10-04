/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dao;

import fr.frogdevelopment.assoplus.core.dao.Dao;
import fr.frogdevelopment.assoplus.member.dto.Member;

public interface MemberDao extends Dao<Member> {

    void updateSubscription(Integer memberId, boolean subscription);

    void updateAnnals(Integer memberId, boolean annals);
}
