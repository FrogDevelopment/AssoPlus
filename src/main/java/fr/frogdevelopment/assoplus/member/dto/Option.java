/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import fr.frogdevelopment.assoplus.core.dto.Reference;

public class Option implements Reference {

	private SimpleIntegerProperty id = new SimpleIntegerProperty();
	private SimpleStringProperty code = new SimpleStringProperty();
	private SimpleStringProperty label = new SimpleStringProperty();
	private SimpleStringProperty degreeCode = new SimpleStringProperty();

	public Integer getId() {
		return id.get();
	}

	public SimpleIntegerProperty idProperty() {
		return id;
	}

	public void setId(Integer id) {
		this.id.set(id);
	}

	@Override
	public String getCode() {
		return code.get();
	}

	public SimpleStringProperty codeProperty() {
		return code;
	}

	public void setCode(String code) {
		this.code.set(code);
	}

	@Override
	public String getLabel() {
		return label.get();
	}

	public SimpleStringProperty labelProperty() {
		return label;
	}

	public void setLabel(String label) {
		this.label.set(label);
	}

	public String getDegreeCode() {
		return degreeCode.get();
	}

	public SimpleStringProperty degreeCodeProperty() {
		return degreeCode;
	}

	public void setDegreeCode(String degreeCode) {
		this.degreeCode.set(degreeCode);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", id)
				.append("code", code)
				.append("label", label)
				.append("degreeCode", degreeCode)
				.toString();
	}
}
