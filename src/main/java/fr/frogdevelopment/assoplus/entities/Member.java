/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(columnNames = "studentNumber")
})
public class Member implements Serializable, Entity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "studentNumber", unique = true, nullable = false)
    private Integer studentNumber;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "licence_code", nullable = false)
    private String licenceCode;

    @Column(name = "option_code", nullable = false)
    private String optionCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "postalCode")
    private String postalCode;

    @Column(name = "city")
    private String city;

//    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members", cascade = CascadeType.PERSIST)
    private Set<SchoolYear> schoolYears;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenceCode() {
        return licenceCode;
    }

    public void setLicenceCode(String licenceCode) {
        this.licenceCode = licenceCode;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Set<SchoolYear> getSchoolYears() {
        return schoolYears;
    }

    public void setSchoolYears(Set<SchoolYear> schoolYears) {
        this.schoolYears = schoolYears;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("studentNumber", studentNumber)
                .append("lastname", lastname)
                .append("firstname", firstname)
                .append("birthday", birthday)
                .append("email", email)
                .append("licenceCode", licenceCode)
                .append("optionCode", optionCode)
                .append("phone", phone)
                .append("address", address)
                .append("postalCode", postalCode)
                .append("city", city)
                .append("schoolYears", schoolYears)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return new EqualsBuilder()
                .append(studentNumber, member.studentNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(studentNumber)
                .toHashCode();
    }
}
