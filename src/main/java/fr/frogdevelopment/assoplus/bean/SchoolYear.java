/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="school_year", uniqueConstraints = {
        @UniqueConstraint(columnNames = "schoolYear")
})
public class SchoolYear {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String schoolYear;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "members_years",
            joinColumns = {@JoinColumn(name = "member_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "school_year_id", nullable = false, updatable = false)}
    )
    private Set<Member> members = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("schoolYear", schoolYear)
                .append("members", members)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SchoolYear that = (SchoolYear) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(schoolYear, that.schoolYear)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(schoolYear)
                .toHashCode();
    }
}
