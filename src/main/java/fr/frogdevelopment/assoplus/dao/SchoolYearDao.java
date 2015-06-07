/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.bean.SchoolYear;

public interface SchoolYearDao extends CommonDao<SchoolYear> {

	SchoolYear getLastShoolYear();
}
