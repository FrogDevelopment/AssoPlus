/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dao;

import fr.frogdevelopment.assoplus.core.dao.CommonDaoImpl;
import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.member.entity.Member;

@Repository("memberDao")
public class MemberDaoImpl extends CommonDaoImpl<Member> implements MemberDao {

}
