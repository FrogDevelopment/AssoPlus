/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dto;

import java.io.Serializable;

public interface Dto extends Serializable {

	void setId(Integer id);

	Integer getId();
}
