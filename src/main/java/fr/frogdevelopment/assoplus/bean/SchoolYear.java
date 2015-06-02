package fr.frogdevelopment.assoplus.bean;

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
}
