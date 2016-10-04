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
import fr.frogdevelopment.assoplus.member.dto.Option;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class OptionDaoImpl extends AbstractDaoImpl<Option> implements OptionDao {

    private RowMapper<Option> optionRowMapper = (rs, rowNum) -> {
        Option option = new Option();
        option.setId(rs.getInt("option_id"));
        option.setCode(rs.getString("code"));
        option.setLabel(rs.getString("label"));
        option.setDegreeCode(rs.getString("degree_code"));

        return option;
    };

    public OptionDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Option getById(Integer identifiant) {
        String sql = "SELECT" +
                " o.option_id AS option_id," +
                " o.code AS code," +
                " o.label AS label," +
                " d.code AS degree_code" +
                " FROM option o" +
                " INNER JOIN degree d ON d.degree_id = o.degree_id" +
                " WHERE option_id = :id";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", identifiant), optionRowMapper);
    }

    @Override
    public List<Option> getAll() {
        String sql = "SELECT" +
                " o.option_id AS option_id," +
                " o.code AS code," +
                " o.label AS label," +
                " d.code AS degree_code" +
                " FROM option o" +
                " INNER JOIN degree d ON d.degree_id = o.degree_id";
        return jdbcTemplate.query(sql, new EmptySqlParameterSource(), optionRowMapper);
    }

    @Override
    public void save(Option option) {
        String sql = "INSERT INTO option (code,label,degree_id)" +
                " SELECT :code,:label, d.degree_id" +
                " FROM degree d WHERE d.code = :degree_code";

        MapSqlParameterSource params = toSqlParameterSource(option);
        params.addValue("degree_code", option.getDegreeCode());

        this.jdbcTemplate.update(sql, params, keyHolder);

        option.setId(keyHolder.getKey().intValue());
    }

    @Override
    protected MapSqlParameterSource toSqlParameterSource(Option option) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("code", option.getCode());
        params.addValue("label", option.getLabel());

        return params;
    }

    @Override
    public void update(Option option) {
        String sql = "UPDATE option SET" +
                " code=:code," +
                " label=:label" +
                " WHERE option_id=:id";

        MapSqlParameterSource params = toSqlParameterSource(option);
        params.addValue("id", option.getId());

        this.jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(Integer identifiant) {
        String sql = "DELETE FROM option WHERE option_id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", identifiant));
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM option", new EmptySqlParameterSource());
    }


}
