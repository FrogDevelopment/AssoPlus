/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dao;

import fr.frogdevelopment.assoplus.core.dao.CommonDaoImpl;
import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.member.entity.Option;

@Repository("optionDao")
public class OptionDaoImpl extends CommonDaoImpl<Option> implements OptionDao {

}
