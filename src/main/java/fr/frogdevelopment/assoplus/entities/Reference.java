/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

import java.io.Serializable;

public interface Reference extends Serializable {

	String getCode();

	void setCode(String code);

	String getLabel();

	void setLabel(String label);
}
