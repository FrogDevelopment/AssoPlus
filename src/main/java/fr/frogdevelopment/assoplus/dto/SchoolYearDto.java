/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class SchoolYearDto implements Dto {

    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty schoolYear = new SimpleStringProperty();
    private final SimpleListProperty<MemberDto> members = new SimpleListProperty<>();

    public Long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(Long id) {
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
