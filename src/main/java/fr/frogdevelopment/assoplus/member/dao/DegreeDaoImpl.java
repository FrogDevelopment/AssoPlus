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
import fr.frogdevelopment.assoplus.member.entity.Degree;

import java.util.List;

@Repository()
public class DegreeDaoImpl extends AbstractDaoImpl<Degree> implements DegreeDao {

    private RowMapper<Degree> degreeRowMapper = (rs, rowNum) -> {
        Degree degree = new Degree();
        degree.setId(rs.getInt("degree_id"));
        degree.setCode(rs.getString("code"));
        degree.setLabel(rs.getString("label"));

        return degree;
    };

    public DegreeDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Degree getById(Integer identifiant) {
        String sql = "SELECT * FROM degree WHERE degree_id = :id";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", identifiant), degreeRowMapper);
    }

    @Override
    public List<Degree> getAll() {
        return jdbcTemplate.query("SELECT * FROM degree", new EmptySqlParameterSource(), degreeRowMapper);
    }

    @Override
    public void save(Degree degree) {
        String sql = "INSERT INTO degree (code,label) VALUES (:code,:label)";

        MapSqlParameterSource params = toSqlParameterSource(degree);

        this.jdbcTemplate.update(sql, params, keyHolder);

        degree.setId(keyHolder.getKey().intValue());
    }

    @Override
    protected MapSqlParameterSource toSqlParameterSource(Degree degree) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("code", degree.getCode());
        params.addValue("label", degree.getLabel());

        return params;
    }

    @Override
    public void update(Degree degree) {
        String sql = "UPDATE degree SET" +
                " code=:code," +
                " label=:label" +
                " WHERE degree_id=:id";

        MapSqlParameterSource params = toSqlParameterSource(degree);
        params.addValue("id", degree.getId());

        this.jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(Integer identifiant) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", identifiant);

        // delete children option
        String sql = "DELETE FROM option WHERE degree_id = :id";
        jdbcTemplate.update(sql, params);

        // delete degree
        sql = "DELETE FROM degree WHERE degree_id = :id";
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deleteAll() {
        // delete children option
        jdbcTemplate.update("DELETE FROM option", new EmptySqlParameterSource());

        // delete degrees
        jdbcTemplate.update("DELETE FROM degree", new EmptySqlParameterSource());
    }
}
