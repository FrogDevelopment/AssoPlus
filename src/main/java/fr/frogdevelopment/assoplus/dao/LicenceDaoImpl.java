/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.entities.Licence;

@Repository("licenceDao")
public class LicenceDaoImpl extends CommonDaoImpl<Licence> implements LicenceDao {

}
