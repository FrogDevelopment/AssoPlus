/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.bean.Member;

import java.util.List;

public interface MemberDao extends CommonDao<Member> {

	List<Integer> getAllNumbers();

}
