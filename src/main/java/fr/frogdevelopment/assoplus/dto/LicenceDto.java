/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.*;
import javafx.collections.ObservableSet;

import java.io.Serializable;

public class LicenceDto implements ReferenceDto {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleStringProperty code = new SimpleStringProperty();
	private SimpleStringProperty label = new SimpleStringProperty();
	private SimpleSetProperty<OptionDto> options = new SimpleSetProperty<>();

	public LicenceDto() {

	}

	public LicenceDto(long id, String code, String label) {
		this.id.setValue(id);
		this.code.setValue(code);
		this.label.setValue(label);
		for (int i = 0; i < 3; i++) {
			options.add(new OptionDto(0, String.valueOf(i), "OptionDto " + i));
		}
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

	public ObservableSet<OptionDto> getOptions() {
		return options.get();
	}

	public SimpleSetProperty<OptionDto> optionsProperty() {
		return options;
	}

	public void setOptions(ObservableSet<OptionDto> options) {
		this.options.set(options);
	}
}
