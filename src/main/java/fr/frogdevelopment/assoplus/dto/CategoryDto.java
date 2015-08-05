/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CategoryDto implements ReferenceDto {

    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty code = new SimpleStringProperty();
    private SimpleStringProperty label = new SimpleStringProperty();

    public Integer getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.set(id);
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

    @Override
    public String toString() {
        return code.getValue() + "-" + label.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CategoryDto)) return false;

        CategoryDto that = (CategoryDto) o;

        return new EqualsBuilder()
                .append(code.getValue(), that.code.getValue())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(code)
                .toHashCode();
    }

}
