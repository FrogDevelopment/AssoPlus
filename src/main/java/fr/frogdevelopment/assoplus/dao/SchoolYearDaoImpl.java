/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.entities.SchoolYear;

@Repository("schoolYearDao")
public class SchoolYearDaoImpl extends CommonDaoImpl<SchoolYear> implements SchoolYearDao {

    @Override
    protected RowMapper<SchoolYear> buildMapper() {
        return null;
    }

    @Override
    public void save(SchoolYear entity) {

    }

    @Override
    public void update(SchoolYear entity) {

    }

    @Override
    public SchoolYear getLastShoolYear() {
        return this.jdbcTemplate.queryForObject("SELECT * FROM school_year ORDER BY year", mapper);
    }
}
