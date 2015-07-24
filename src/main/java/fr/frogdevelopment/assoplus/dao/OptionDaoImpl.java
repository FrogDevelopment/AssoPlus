/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.entities.Option;

@Repository("optionDao")
public class OptionDaoImpl extends CommonDaoImpl<Option> implements OptionDao {

    @Override
    protected RowMapper<Option> buildMapper() {
        return null;
    }

    @Override
    public void save(Option entity) {

    }

    @Override
    public void update(Option entity) {

    }
}
