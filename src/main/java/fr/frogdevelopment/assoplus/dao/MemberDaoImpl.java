/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.entities.Member;

import java.util.List;

@Repository("memberDao")
public class MemberDaoImpl extends CommonDaoImpl<Member> implements MemberDao {

	@Override
	public List<Integer> getAllNumbers() {
		return this.jdbcTemplate.queryForList("SELECT number FROM member ORDER BY number", Integer.class);
	}
}
