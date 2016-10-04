/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.core.dao.AbstractDaoImpl;
import fr.frogdevelopment.assoplus.member.dto.Degree;
import fr.frogdevelopment.assoplus.member.dto.Member;
import fr.frogdevelopment.assoplus.member.dto.Option;

import javax.sql.DataSource;
import java.util.List;

@Repository()
@Transactional(propagation = Propagation.MANDATORY)
public class MemberDaoImpl extends AbstractDaoImpl<Member> implements MemberDao {

    private RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getInt("member_id"));
        member.setStudentNumber(rs.getString("student_number"));
        member.setLastname(rs.getString("lastname"));
        member.setFirstname(rs.getString("firstname"));
        member.setBirthday(rs.getString("birthday"));
        member.setEmail(rs.getString("email"));

        Degree degree = new Degree();
        degree.setId(rs.getInt("degree_id"));
        degree.setCode(rs.getString("degree_code"));
        degree.setLabel(rs.getString("degree_label"));
        member.setDegree(degree);

        Option option = new Option();
        option.setId(rs.getInt("option_id"));
        option.setCode(rs.getString("option_code"));
        option.setLabel(rs.getString("option_label"));
        member.setOption(option);

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
        String sql = "SELECT" +
                " m.*," +
                " d.degree_id AS degree_id," +
                " d.code AS degree_code," +
                " d.label AS degree_label," +
                " o.option_id AS option_id," +
                " o.code AS option_code," +
                " o.label AS option_label" +
                " FROM member m" +
                " LEFT OUTER JOIN degree d ON d.code = m.degree_code" +
                " LEFT OUTER JOIN option o ON o.degree_id = d.degree_id AND o.code = m.option_code" +
                " WHERE member_id = :id";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", identifiant), memberRowMapper);
    }

    @Override
    public List<Member> getAll() {
        String sql = "SELECT" +
                " m.*," +
                " d.degree_id AS degree_id," +
                " d.code AS degree_code," +
                " d.label AS degree_label," +
                " o.option_id AS option_id," +
                " o.code AS option_code," +
                " o.label AS option_label" +
                " FROM member m" +
                " LEFT OUTER JOIN degree d ON d.code = m.degree_code" +
                " LEFT OUTER JOIN option o ON o.degree_id = d.degree_id AND o.code = m.option_code";
        return jdbcTemplate.query(sql, new EmptySqlParameterSource(), memberRowMapper);
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
        if (member.getBirthday() != null) {
            params.addValue("birthday", member.getBirthday().toString());
        } else {
            params.addValue("birthday", null);
        }
        params.addValue("birthday", member.getBirthday());
        params.addValue("email", member.getEmail());
        if (member.getDegree() != null) {
            params.addValue("degreeCode", member.getDegree().getCode());
        } else {
            params.addValue("degreeCode", null);
        }
        if (member.getOption() != null) {
            params.addValue("optionCode", member.getOption().getCode());
        } else {
            params.addValue("optionCode", null);
        }
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
