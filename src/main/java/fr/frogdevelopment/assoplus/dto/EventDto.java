/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EventDto implements Dto {

	private SimpleIntegerProperty id = new SimpleIntegerProperty();
	private SimpleStringProperty title = new SimpleStringProperty();
	private SimpleStringProperty date = new SimpleStringProperty();
	private SimpleStringProperty text = new SimpleStringProperty();
	private SimpleStringProperty categoryCode = new SimpleStringProperty();

	public Integer getId() {
		return id.get();
	}

	public SimpleIntegerProperty idProperty() {
		return id;
	}

	public void setId(Integer id) {
		this.id.set(id);
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public String getTitle() {
		return title.get();
	}

	public SimpleStringProperty titleProperty() {
		return title;
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getDate() {
		return date.get();
	}

	public SimpleStringProperty dateProperty() {
		return date;
	}

	public void setDate(String date) {
		this.date.set(date);
	}

	public String getText() {
		return text.get();
	}

	public SimpleStringProperty textProperty() {
		return text;
	}

	public void setText(String text) {
		this.text.set(text);
	}

	public String getCategoryCode() {
		return categoryCode.get();
	}

	public SimpleStringProperty categoryCodeProperty() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode.set(categoryCode);
	}
}
