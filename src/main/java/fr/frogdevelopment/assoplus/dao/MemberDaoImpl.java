/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.bean.Member;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("memberDao")
public class MemberDaoImpl extends CommonDaoImpl<Member> implements MemberDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> getAllNumbers() {
		return getCriteria()
				.setProjection(Projections.property("number"))
				.addOrder(Order.asc("number"))
				.list();
	}


}
