/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import fr.frogdevelopment.assoplus.core.entity.Entity;

public class Member implements Entity {

    private Integer id;
    private String studentNumber;
    private String lastname;
    private String firstname;
    private String birthday;
    private String email;
    private String degreeCode;
    private String optionCode;
    private String phone;
    private Boolean subscription;
    private Boolean annals;

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

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
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

    public String getDegreeCode() {
        return degreeCode;
    }

    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
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

    public Boolean getSubscription() {
        return subscription;
    }

    public void setSubscription(Boolean subscription) {
        this.subscription = subscription;
    }

    public Boolean getAnnals() {
        return annals;
    }

    public void setAnnals(Boolean annals) {
        this.annals = annals;
    }

    // ********************************** \\
    //           Overriden methods        \\
    // ********************************** \\

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("studentNumber", studentNumber)
                .append("lastname", lastname)
                .append("firstname", firstname)
                .append("birthday", birthday)
                .append("email", email)
                .append("degreeCode", degreeCode)
                .append("optionCode", optionCode)
                .append("phone", phone)
                .append("subscription", subscription)
                .append("annals", annals)
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
