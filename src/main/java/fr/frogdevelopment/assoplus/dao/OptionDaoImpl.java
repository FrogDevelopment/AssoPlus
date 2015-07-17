/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.entities.Option;
import org.springframework.stereotype.Repository;

@Repository("optionDao")
public class OptionDaoImpl extends CommonDaoImpl<Option> implements OptionDao {

}
