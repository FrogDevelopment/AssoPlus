/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OptionDto implements ReferenceDto {

	private SimpleIntegerProperty id = new SimpleIntegerProperty();
	private SimpleStringProperty code = new SimpleStringProperty();
	private SimpleStringProperty label = new SimpleStringProperty();
	private SimpleStringProperty licenceCode = new SimpleStringProperty();

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

	public String getLicenceCode() {
		return licenceCode.get();
	}

	public SimpleStringProperty licenceCodeProperty() {
		return licenceCode;
	}

	public void setLicenceCode(String licenceCode) {
		this.licenceCode.set(licenceCode);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("code", code)
				.append("label", label)
				.append("licenceCode", licenceCode)
				.toString();
	}
}
