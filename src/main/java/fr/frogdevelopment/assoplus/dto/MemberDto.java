/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class MemberDto implements Dto {

	private final SimpleIntegerProperty id = new SimpleIntegerProperty();
	private final SimpleIntegerProperty studentNumber = new SimpleIntegerProperty();
	private final SimpleStringProperty lastname = new SimpleStringProperty();
	private final SimpleStringProperty firstname = new SimpleStringProperty();
	private final SimpleObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();
	private final SimpleStringProperty email = new SimpleStringProperty();
	private final SimpleStringProperty degreeCode = new SimpleStringProperty();
	private final SimpleStringProperty optionCode = new SimpleStringProperty();
	private final SimpleStringProperty phone = new SimpleStringProperty();

	public Integer getId() {
		return id.get();
	}

	public SimpleIntegerProperty idProperty() {
		return id;
	}

	public void setId(Integer id) {
		this.id.set(id);
	}

	public int getStudentNumber() {
		return studentNumber.get();
	}

	public SimpleIntegerProperty studentNumberProperty() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber.set(studentNumber);
	}

	public String getLastname() {
		return lastname.get();
	}

	public SimpleStringProperty lastnameProperty() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname.set(lastname);
	}

	public String getFirstname() {
		return firstname.get();
	}

	public SimpleStringProperty firstnameProperty() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname.set(firstname);
	}

	public LocalDate getBirthday() {
		return birthday.get();
	}

	public SimpleObjectProperty<LocalDate> birthdayProperty() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday.set(birthday);
	}

	public String getEmail() {
		return email.get();
	}

	public SimpleStringProperty emailProperty() {
		return email;
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public String getDegreeCode() {
		return degreeCode.get();
	}

	public SimpleStringProperty degreeCodeProperty() {
		return degreeCode;
	}

	public void setDegreeCode(String degreeCode) {
		this.degreeCode.set(degreeCode);
	}

	public String getOptionCode() {
		return optionCode.get();
	}

	public SimpleStringProperty optionCodeProperty() {
		return optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode.set(optionCode);
	}

	public String getPhone() {
		return phone.get();
	}

	public SimpleStringProperty phoneProperty() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone.set(phone);
	}

}
