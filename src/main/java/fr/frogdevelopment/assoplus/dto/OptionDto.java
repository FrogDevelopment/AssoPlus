/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import fr.frogdevelopment.assoplus.entities.Licence;
import fr.frogdevelopment.assoplus.entities.Option;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class OptionDto implements ReferenceDto {

	private SimpleLongProperty id = new SimpleLongProperty();
	private SimpleStringProperty code = new SimpleStringProperty();
	private SimpleStringProperty label = new SimpleStringProperty();

	public static Set<OptionDto> createDtos(Collection<Option> beans) {
		return beans.stream().map(OptionDto::createDto).collect(Collectors.toSet());
	}

	public static OptionDto createDto(Option bean) {
		OptionDto dto = new OptionDto();
		dto.setId(bean.getId());
		dto.setCode(bean.getCode());
		dto.setLabel(bean.getLabel());

		return dto;
	}

	public static Set<Option> createBeans(Collection<OptionDto> dtos, Licence licence) {
		return dtos.stream().map((dto) -> createBean(licence, dto)).collect(Collectors.toSet());
	}

	public static Option createBean(Licence licence, OptionDto dto) {
		Option bean = new Option();
		bean.setId(dto.getId());
		bean.setCode(dto.getCode());
		bean.setLabel(dto.getLabel());
		bean.setLicence(licence);

		return bean;
	}

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

}
