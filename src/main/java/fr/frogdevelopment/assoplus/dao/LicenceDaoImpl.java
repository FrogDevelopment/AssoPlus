/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.entities.Licence;
import org.springframework.stereotype.Repository;

@Repository("licenceDao")
public class LicenceDaoImpl extends CommonDaoImpl<Licence> implements LicenceDao {

}
