/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
    private Long id;

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

    @Column(name = "licence", nullable = false)
    private String licence;

    @Column(name = "option", nullable = false)
    private String option;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "postalCode")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members", cascade = CascadeType.PERSIST)
    private Set<SchoolYear> schoolYears;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
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
                .append("licence", licence)
                .append("option", option)
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
                .append(id, member.id)
                .append(studentNumber, member.studentNumber)
                .append(lastname, member.lastname)
                .append(firstname, member.firstname)
                .append(birthday, member.birthday)
                .append(email, member.email)
                .append(licence, member.licence)
                .append(option, member.option)
                .append(phone, member.phone)
                .append(address, member.address)
                .append(postalCode, member.postalCode)
                .append(city, member.city)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(studentNumber)
                .append(lastname)
                .append(firstname)
                .append(birthday)
                .append(email)
                .append(licence)
                .append(option)
                .append(phone)
                .append(address)
                .append(postalCode)
                .append(city)
                .toHashCode();
    }
}
