/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class SchoolYearDto implements Dto {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty schoolYear = new SimpleStringProperty();
    private final SimpleListProperty<MemberDto> members = new SimpleListProperty<>();

    public Integer getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getSchoolYear() {
        return schoolYear.get();
    }

    public SimpleStringProperty schoolYearProperty() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear.set(schoolYear);
    }

    public ObservableList<MemberDto> getMembers() {
        return members.get();
    }

    public SimpleListProperty<MemberDto> membersProperty() {
        return members;
    }

    public void setMembers(ObservableList<MemberDto> members) {
        this.members.set(members);
    }
}
