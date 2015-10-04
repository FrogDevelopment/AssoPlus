/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.event.dto;

import fr.frogdevelopment.assoplus.core.dto.Dto;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EventDto implements Dto {

	private SimpleIntegerProperty id = new SimpleIntegerProperty();
	private SimpleStringProperty title = new SimpleStringProperty();
	private SimpleStringProperty date = new SimpleStringProperty();
	private SimpleStringProperty text = new SimpleStringProperty();
	private final SimpleBooleanProperty published = new SimpleBooleanProperty(false);

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

	public boolean getPublished() {
		return published.get();
	}

	public SimpleBooleanProperty publishedProperty() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published.set(published);
	}
}
