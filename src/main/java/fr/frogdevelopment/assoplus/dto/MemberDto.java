/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.*;

import java.io.Serializable;

public class MemberDto implements Serializable, Dto{

	private final LongProperty id = new SimpleLongProperty();
	private final IntegerProperty studentNumber = new SimpleIntegerProperty();
	private final StringProperty lastname = new SimpleStringProperty();
	private final StringProperty firstname = new SimpleStringProperty();
	private final StringProperty birthday = new SimpleStringProperty();
	private final StringProperty email = new SimpleStringProperty();
	private final StringProperty licence = new SimpleStringProperty();
	private final StringProperty option = new SimpleStringProperty();
	private final StringProperty phone = new SimpleStringProperty();
	private final StringProperty address = new SimpleStringProperty();
	private final StringProperty postalCode = new SimpleStringProperty();
	private final StringProperty city = new SimpleStringProperty();
	private final BooleanProperty feePaid = new SimpleBooleanProperty();

	public long getId() {
		return id.get();
	}

	public LongProperty idProperty() {
		return id;
	}

	public void setId(long id) {
		this.id.set(id);
	}

	public int getStudentNumber() {
		return studentNumber.get();
	}

	public IntegerProperty studentNumberProperty() {
		return studentNumber;
	}

	public void setStudentNumber(int studentNumber) {
		this.studentNumber.set(studentNumber);
	}

	public String getLastname() {
		return lastname.get();
	}

	public StringProperty lastnameProperty() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname.set(lastname);
	}

	public String getFirstname() {
		return firstname.get();
	}

	public StringProperty firstnameProperty() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname.set(firstname);
	}

	public String getBirthday() {
		return birthday.get();
	}

	public StringProperty birthdayProperty() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday.set(birthday);
	}

	public String getEmail() {
		return email.get();
	}

	public StringProperty emailProperty() {
		return email;
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public String getLicence() {
		return licence.get();
	}

	public StringProperty licenceProperty() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence.set(licence);
	}

	public String getOption() {
		return option.get();
	}

	public StringProperty optionProperty() {
		return option;
	}

	public void setOption(String option) {
		this.option.set(option);
	}

	public String getPhone() {
		return phone.get();
	}

	public StringProperty phoneProperty() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone.set(phone);
	}

	public String getAddress() {
		return address.get();
	}

	public StringProperty addressProperty() {
		return address;
	}

	public void setAddress(String address) {
		this.address.set(address);
	}

	public String getPostalCode() {
		return postalCode.get();
	}

	public StringProperty postalCodeProperty() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode.set(postalCode);
	}

	public String getCity() {
		return city.get();
	}

	public StringProperty cityProperty() {
		return city;
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public boolean getFeePaid() {
		return feePaid.get();
	}

	public BooleanProperty feePaidProperty() {
		return feePaid;
	}

	public void setFeePaid(boolean feePaid) {
		this.feePaid.set(feePaid);
	}
}
