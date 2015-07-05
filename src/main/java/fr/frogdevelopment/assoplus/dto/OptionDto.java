/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class OptionDto implements ReferenceDto {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleStringProperty code = new SimpleStringProperty();
	private SimpleStringProperty label = new SimpleStringProperty();
	private ObjectProperty<LicenceDto> licenceDto = new SimpleObjectProperty<>();

	public OptionDto(long id, String code, String label) {
		this.id.setValue(id);
		this.code.setValue(code);
		this.label.setValue(label);
	}

	public long getId() {
		return id.get();
	}

	public SimpleLongProperty idProperty() {
		return id;
	}

	public void setId(long id) {
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

	public LicenceDto getLicenceDto() {
		return licenceDto.get();
	}

	public ObjectProperty<LicenceDto> licenceDtoProperty() {
		return licenceDto;
	}

	public void setLicenceDto(LicenceDto licenceDto) {
		this.licenceDto.set(licenceDto);
	}
}
