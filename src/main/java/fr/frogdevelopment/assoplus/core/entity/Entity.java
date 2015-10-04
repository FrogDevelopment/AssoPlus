/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.entity;

import java.io.Serializable;

public interface Entity extends Serializable {

	Integer getId();

	void setId(Integer id);
}
