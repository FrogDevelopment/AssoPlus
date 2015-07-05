/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.*;

import java.io.Serializable;

public class MemberDto implements Dto {

	private final SimpleLongProperty id = new SimpleLongProperty();
	private final SimpleIntegerProperty studentNumber = new SimpleIntegerProperty();
	private final SimpleStringProperty lastname = new SimpleStringProperty();
	private final SimpleStringProperty firstname = new SimpleStringProperty();
	private final SimpleStringProperty birthday = new SimpleStringProperty();
	private final SimpleStringProperty email = new SimpleStringProperty();
	private final SimpleStringProperty licence = new SimpleStringProperty();
	private final SimpleStringProperty option = new SimpleStringProperty();
	private final SimpleStringProperty phone = new SimpleStringProperty();
	private final SimpleStringProperty address = new SimpleStringProperty();
	private final SimpleStringProperty postalCode = new SimpleStringProperty();
	private final SimpleStringProperty city = new SimpleStringProperty();

	public long getId() {
		return id.get();
	}

	public SimpleLongProperty idProperty() {
		return id;
	}

	public void setId(long id) {
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

	public String getBirthday() {
		return birthday.get();
	}

	public SimpleStringProperty birthdayProperty() {
		return birthday;
	}

	public void setBirthday(String birthday) {
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

	public String getLicence() {
		return licence.get();
	}

	public SimpleStringProperty licenceProperty() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence.set(licence);
	}

	public String getOption() {
		return option.get();
	}

	public SimpleStringProperty optionProperty() {
		return option;
	}

	public void setOption(String option) {
		this.option.set(option);
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

	public String getAddress() {
		return address.get();
	}

	public SimpleStringProperty addressProperty() {
		return address;
	}

	public void setAddress(String address) {
		this.address.set(address);
	}

	public String getPostalCode() {
		return postalCode.get();
	}

	public SimpleStringProperty postalCodeProperty() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode.set(postalCode);
	}

	public String getCity() {
		return city.get();
	}

	public SimpleStringProperty cityProperty() {
		return city;
	}

	public void setCity(String city) {
		this.city.set(city);
	}
}
