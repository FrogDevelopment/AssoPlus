/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemberDto implements Dto {

	private final SimpleIntegerProperty id = new SimpleIntegerProperty();
	private final SimpleIntegerProperty studentNumber = new SimpleIntegerProperty();
	private final SimpleStringProperty lastname = new SimpleStringProperty();
	private final SimpleStringProperty firstname = new SimpleStringProperty();
	private final SimpleStringProperty birthday = new SimpleStringProperty();
	private final SimpleStringProperty email = new SimpleStringProperty();
	private final SimpleStringProperty licenceCode = new SimpleStringProperty();
	private final SimpleStringProperty optionCode = new SimpleStringProperty();
	private final SimpleStringProperty phone = new SimpleStringProperty();
	private final SimpleStringProperty address = new SimpleStringProperty();
	private final SimpleStringProperty postalCode = new SimpleStringProperty();
	private final SimpleStringProperty city = new SimpleStringProperty();

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

	public String getLicenceCode() {
		return licenceCode.get();
	}

	public SimpleStringProperty licenceCodeProperty() {
		return licenceCode;
	}

	public void setLicenceCode(String licenceCode) {
		this.licenceCode.set(licenceCode);
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
