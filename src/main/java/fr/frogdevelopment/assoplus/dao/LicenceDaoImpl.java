/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.bean.Licence;
import fr.frogdevelopment.assoplus.bean.Member;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("licenceDao")
public class LicenceDaoImpl extends CommonDaoImpl<Licence> implements LicenceDao {

}
