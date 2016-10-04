/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dto;

import javafx.beans.property.SimpleStringProperty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import fr.frogdevelopment.assoplus.core.dto.Reference;

import java.util.HashSet;
import java.util.Set;

public class Degree implements Reference {

    private Integer id;
    private SimpleStringProperty code = new SimpleStringProperty();
    private SimpleStringProperty label = new SimpleStringProperty();
    private Set<Option> options = new HashSet<>();
    private boolean toDelete = false;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    @Override
    public String getLabel() {
        return label.get();
    }

    public SimpleStringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public void addOption(Option option) {
        this.options.add(option);
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("code", getCode())
                .append("label", getLabel())
                .append("options", options)
                .append("toDelete", toDelete)
                .toString();
    }
}
