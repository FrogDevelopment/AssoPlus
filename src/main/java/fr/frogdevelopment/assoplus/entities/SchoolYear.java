/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "school_year")
public class SchoolYear implements Entity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue
    private Integer id;

    @Column(name = "year", nullable = false, unique = true)
    private String year;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "members_years",
//            joinColumns = {@JoinColumn(name = "member_id", nullable = false, updatable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "school_year_id", nullable = false, updatable = false)}
//    )
//    private Set<Member> members = new HashSet<>();

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

//    public Set<Member> getMembers() {
//        return members;
//    }
//
//    public void setMembers(Set<Member> members) {
//        this.members = members;
//    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("year", year)
//                .append("members", members)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SchoolYear that = (SchoolYear) o;

        return new EqualsBuilder()
                .append(year, that.year)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(year)
                .toHashCode();
    }
}
