package fr.frogdevelopment.assoplus.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import fr.frogdevelopment.assoplus.entities.Entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = EntityTest.TABLE_NAME)
public class EntityTest implements Entity {

    public static final String TABLE_NAME = "test";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_LABEL = "label";

    @Id
    @Column(name = EntityTest.COLUMN_ID, unique = true, nullable = false)
    @GeneratedValue
    private Long id;

    @Column(name = EntityTest.COLUMN_CODE, unique = true, nullable = false)
    private String code;

    @Column(name = EntityTest.COLUMN_LABEL, unique = false, nullable = false)
    private String label;

    public EntityTest() {
    }

    public EntityTest(Long id) {
        this.id = id;
    }

    public EntityTest(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("code", code)
                .append("label", label)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EntityTest entityTest = (EntityTest) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(code, entityTest.code)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(code)
                .toHashCode();
    }

}
