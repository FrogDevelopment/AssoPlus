/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.entities.SchoolYear;

@Repository("schoolYearDao")
public class SchoolYearDaoImpl extends CommonDaoImpl<SchoolYear> implements SchoolYearDao {

    @Override
    public SchoolYear getLastShoolYear() {
        return this.jdbcTemplate.queryForObject("SELECT * FROM school_year ORDER BY year", mapper);
    }
}
