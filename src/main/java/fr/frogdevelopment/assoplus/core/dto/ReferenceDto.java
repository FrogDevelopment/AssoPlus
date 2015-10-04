/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dto;

import fr.frogdevelopment.assoplus.core.dto.Dto;
import javafx.beans.property.SimpleStringProperty;

public interface ReferenceDto extends Dto {

	SimpleStringProperty codeProperty();

	String getCode();

	void setCode(String code);

	SimpleStringProperty labelProperty();

	String getLabel();

	void setLabel(String label);
}
