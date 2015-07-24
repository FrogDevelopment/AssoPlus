/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.entities.Licence;

@Repository("licenceDao")
public class LicenceDaoImpl extends CommonDaoImpl<Licence> implements LicenceDao {

    @Override
    protected RowMapper<Licence> buildMapper() {
        return null;
    }

    @Override
    public void save(Licence entity) {

    }

    @Override
    public void update(Licence entity) {

    }
}
