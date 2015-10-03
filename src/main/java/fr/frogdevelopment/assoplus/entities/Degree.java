/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "degree")
public class Degree implements Reference {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue
	private Integer id;

	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "label", unique = false, nullable = false)
	private String label;

	public Degree() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("code", code)
				.append("label", label)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Degree degree = (Degree) o;

		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(code, degree.code)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
				.append(code)
				.toHashCode();
	}
}
