/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.entities.Degree;

@Repository("licenceDao")
public class LicenceDaoImpl extends CommonDaoImpl<Degree> implements LicenceDao {

}
