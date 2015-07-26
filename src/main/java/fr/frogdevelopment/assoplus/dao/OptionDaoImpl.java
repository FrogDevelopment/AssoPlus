/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.entities.Option;

@Repository("optionDao")
public class OptionDaoImpl extends CommonDaoImpl<Option> implements OptionDao {

}
