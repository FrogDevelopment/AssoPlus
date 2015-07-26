/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
@Table(name = "licence", uniqueConstraints = {
		@UniqueConstraint(columnNames = "code"),
		@UniqueConstraint(columnNames = "label")
})
public class Licence implements Reference, Entity {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "label", unique = true, nullable = false)
	private String label;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "licence")
	private Set<Option> options;

	public Licence() {
	}

	public Licence(String code, String label) {
		this.code = code;
		this.label = label;
		this.options = new HashSet<>();
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

	public Set<Option> getOptions() {
		return options;
	}

	public void setOptions(Set<Option> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("code", code)
				.append("label", label)
				.append("options", options)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Licence licence = (Licence) o;

		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(id, licence.id)
				.append(code, licence.code)
				.append(label, licence.label)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
				.append(id)
				.append(code)
				.append(label)
				.toHashCode();
	}
}
