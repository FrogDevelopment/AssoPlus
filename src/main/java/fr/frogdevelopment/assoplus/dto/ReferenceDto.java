/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleStringProperty;

public interface ReferenceDto extends Dto {

	SimpleStringProperty codeProperty();

	String getCode();

	void setCode(String code);

	SimpleStringProperty labelProperty();

	String getLabel();

	void setLabel(String label);
}
