/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import fr.frogdevelopment.assoplus.core.entity.Reference;

public class Degree implements Reference {

	private Integer id;
	private String code;
	private String label;

	// ********************************** \\
	//            Getter & Setter         \\
	// ********************************** \\

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	// ********************************** \\
	//           Overriden methods        \\
	// ********************************** \\

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
