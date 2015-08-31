/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LicenceDto implements ReferenceDto {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleStringProperty code = new SimpleStringProperty();
	private SimpleStringProperty label = new SimpleStringProperty();

	public Long getId() {
		return id.get();
	}

	public SimpleLongProperty idProperty() {
		return id;
	}

	public void setId(Long id) {
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

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("code", code)
				.append("label", label)
				.toString();
	}
}
