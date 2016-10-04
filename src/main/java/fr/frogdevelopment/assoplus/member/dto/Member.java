/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dto;

import javafx.beans.property.SimpleBooleanProperty;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import fr.frogdevelopment.assoplus.core.dto.Entity;

import java.time.LocalDate;

public class Member implements Entity, Comparable<Member> {

    private Integer id;
    private String studentNumber;
    private String lastname;
    private String firstname;
    private LocalDate birthday;
    private String email;
    private Degree degree;
    private Option option;
    private String phone;
    private final SimpleBooleanProperty subscription = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty annals = new SimpleBooleanProperty(false);

    private boolean selected = false;

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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setBirthday(String birthday) {
        if (StringUtils.isNotBlank(birthday)) {
            setBirthday(LocalDate.parse(birthday));
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDegreeLabel() {
        String label = "";
        if (degree != null) {
            label = degree.getCode() + " - " + degree.getLabel();
        }

        return label;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public String getOptionLabel() {
        String label = "";
        if (option != null) {
            label = option.getCode() + " - " + option.getLabel();
        }

        return label;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getAnnals() {
        return annals.get();
    }

    public SimpleBooleanProperty annalsProperty() {
        return annals;
    }

    public void setAnnals(boolean annals) {
        this.annals.set(annals);
    }

    public boolean getSubscription() {
        return subscription.get();
    }

    public SimpleBooleanProperty subscriptionProperty() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription.set(subscription);
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("studentNumber", studentNumber)
                .append("lastname", lastname)
                .append("firstname", firstname)
                .append("birthday", birthday)
                .append("email", email)
                .append("degree", degree)
                .append("option", option)
                .append("phone", phone)
                .append("subscription", subscription)
                .append("annals", annals)
                .append("selected", selected)
                .toString();
    }

    @Override
    public int compareTo(Member o) {
        return new CompareToBuilder()
                .append(getStudentNumber(), o.getStudentNumber())
                .build();
    }

    public String[] toCSV() {
        String[] line = new String[10];
        line[0] = studentNumber;
        line[1] = lastname;
        line[2] = firstname;
        if (birthday != null) {
            line[3] = birthday.toString();
        } else {
            line[3] = "";
        }
        line[4] = email;
        line[5] = degree != null ? degree.getCode() : "";
        line[6] = option != null ? option.getCode() : "";
        line[7] = phone;
        line[8] = String.valueOf(getSubscription());
        line[9] = String.valueOf(getAnnals());

        return line;
    }
}
