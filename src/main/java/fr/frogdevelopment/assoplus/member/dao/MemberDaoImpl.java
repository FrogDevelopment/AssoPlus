/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.frogdevelopment.assoplus.core.dao.AbstractDaoImpl;
import fr.frogdevelopment.assoplus.member.entity.Member;

import javax.sql.DataSource;
import java.util.List;

@Repository()
public class MemberDaoImpl extends AbstractDaoImpl<Member> implements MemberDao {

    private RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getInt("member_id"));
        member.setStudentNumber(rs.getString("student_number"));
        member.setLastname(rs.getString("lastname"));
        member.setFirstname(rs.getString("firstname"));
        member.setBirthday(rs.getString("birthday"));
        member.setEmail(rs.getString("email"));
        member.setDegreeCode(rs.getString("degree_code"));
        member.setOptionCode(rs.getString("option_code"));
        member.setPhone(rs.getString("phone"));
        member.setSubscription(rs.getBoolean("subscription"));
        member.setAnnals(rs.getBoolean("annals"));

        return member;
    };

    public MemberDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        super(jdbcTemplate);
    }

    @Override
    public Member getById(Integer identifiant) {
        String sql = "SELECT * FROM member WHERE member_id = :id";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", identifiant), memberRowMapper);
    }

    @Override
    public List<Member> getAll() {
        return jdbcTemplate.query("SELECT * FROM member", new EmptySqlParameterSource(), memberRowMapper);
    }

    @Override
    public void save(Member member) {
        String sql = "INSERT INTO member (student_number,lastname,firstname,birthday,email,degree_code,option_code,phone,subscription,annals)" +
                " VALUES (:studentNumber,:lastname,:firstname,:birthday,:email,:degreeCode,:optionCode,:phone,:subscription,:annals)";

        MapSqlParameterSource params = toSqlParameterSource(member);

        this.jdbcTemplate.update(sql, params, keyHolder);

        member.setId(keyHolder.getKey().intValue());
    }

    @Override
    protected MapSqlParameterSource toSqlParameterSource(Member member) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("studentNumber", member.getStudentNumber());
        params.addValue("lastname", member.getLastname());
        params.addValue("firstname", member.getFirstname());
        params.addValue("birthday", member.getBirthday());
        params.addValue("email", member.getEmail());
        params.addValue("degreeCode", member.getDegreeCode());
        params.addValue("optionCode", member.getOptionCode());
        params.addValue("phone", member.getPhone());
        params.addValue("subscription", member.getSubscription());
        params.addValue("annals", member.getAnnals());

        return params;
    }

    @Override
    public void update(Member member) {
        String sql = "UPDATE member SET" +
                " student_number=:studentNumber," +
                " lastname=:lastname," +
                " firstname=:firstname," +
                " birthday=:birthday," +
                " email=:email," +
                " degree_code=:degreeCode," +
                " option_code=:optionCode," +
                " phone=:phone," +
                " subscription=:subscription," +
                " annals=:annals" +
                " WHERE member_id=:id";

        MapSqlParameterSource params = toSqlParameterSource(member);
        params.addValue("id", member.getId());

        this.jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(Integer identifiant) {
        String sql = "DELETE FROM member WHERE member_id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", identifiant));
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM member", new EmptySqlParameterSource());
    }

    @Override
    public void updateSubscription(Integer memberId, boolean subscription) {
        String sql = "UPDATE member SET" +
                " subscription=:subscription" +
                " WHERE member_id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("subscription", subscription);
        params.addValue("id", memberId);

        this.jdbcTemplate.update(sql, params);
    }

    @Override
    public void updateAnnals(Integer memberId, boolean annals) {
        String sql = "UPDATE member SET" +
                " annals=:annals" +
                " WHERE member_id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("annals", annals);
        params.addValue("id", memberId);

        this.jdbcTemplate.update(sql, params);
    }
}
