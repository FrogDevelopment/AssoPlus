/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "event")
public class Event implements Entity {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue
	private Integer id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "date", nullable = false)
	private String date;

	@Column(name = "text", nullable = false)
	private String text;

	@Column(name="published", nullable = false)
	private Integer published;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getPublished() {
		return published;
	}

	public void setPublished(Integer published) {
		this.published = published;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("title", title)
				.append("date", date)
				.append("text", text)
				.append("published", published)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Event event = (Event) o;

		return new EqualsBuilder()
				.append(id, event.id)
				.append(title, event.title)
				.append(date, event.date)
				.append(text, event.text)
				.append(published, event.published)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(id)
				.append(title)
				.append(date)
				.append(text)
				.append(published)
				.toHashCode();
	}
}
