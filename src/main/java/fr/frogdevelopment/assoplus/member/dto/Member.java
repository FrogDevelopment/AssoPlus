/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import fr.frogdevelopment.assoplus.core.dto.Entity;

import java.time.LocalDate;

public class Member implements Entity, Comparable<Member> {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty studentNumber = new SimpleStringProperty("");
    private final SimpleStringProperty lastname = new SimpleStringProperty("");
    private final SimpleStringProperty firstname = new SimpleStringProperty("");
    private final SimpleObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleObjectProperty<Degree> degree = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Option> option = new SimpleObjectProperty<>();
    private final SimpleStringProperty phone = new SimpleStringProperty("");
    private final SimpleBooleanProperty subscription = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty annals = new SimpleBooleanProperty(false);

    private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    @Override
    public Integer getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getStudentNumber() {
        return studentNumber.get();
    }

    public SimpleStringProperty studentNumberProperty() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
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

    public void setBirthday(String birthday) {
        if (StringUtils.isNotBlank(birthday)) {
            this.birthday.set(LocalDate.parse(birthday));
        }
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

    public Degree getDegree() {
        return degree.get();
    }

    public SimpleObjectProperty<Degree> degreeProperty() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree.set(degree);
    }

    public Option getOption() {
        return option.get();
    }

    public SimpleObjectProperty<Option> optionProperty() {
        return option;
    }

    public void setOption(Option option) {
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
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
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
        line[0] = getStudentNumber();
        line[1] = getLastname();
        line[2] = getFirstname();
        if (getBirthday() != null) {
            line[3] = getBirthday().toString();
        } else {
            line[3] = "";
        }
        line[4] = getEmail();
        line[5] = getDegree() != null ? getDegree().getCode() : "";
        line[6] = getOption() != null ? getOption().getCode() : "";
        line[7] = getPhone();
        line[8] = String.valueOf(getSubscription());
        line[9] = String.valueOf(getAnnals());

        return line;
    }
}
