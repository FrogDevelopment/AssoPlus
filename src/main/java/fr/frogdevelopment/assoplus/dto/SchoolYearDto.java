/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.io.Serializable;

public class SchoolYearDto implements Serializable {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty schoolYear = new SimpleStringProperty();
    private final ListProperty<MemberDto> members = new SimpleListProperty<>();

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getSchoolYear() {
        return schoolYear.get();
    }

    public StringProperty schoolYearProperty() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear.set(schoolYear);
    }

    public ObservableList<MemberDto> getMembers() {
        return members.get();
    }

    public ListProperty<MemberDto> membersProperty() {
        return members;
    }

    public void setMembers(ObservableList<MemberDto> members) {
        this.members.set(members);
    }
}
