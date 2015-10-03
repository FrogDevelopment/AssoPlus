/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

public interface Reference extends Entity{

	String getCode();

	void setCode(String code);

	String getLabel();

	void setLabel(String label);
}
