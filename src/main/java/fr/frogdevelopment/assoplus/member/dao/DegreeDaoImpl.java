/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.core.dao.AbstractDaoImpl;
import fr.frogdevelopment.assoplus.member.dto.Degree;
import fr.frogdevelopment.assoplus.member.dto.Option;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class DegreeDaoImpl extends AbstractDaoImpl<Degree> implements DegreeDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Collection<Degree> getAll() {
        String sql = "SELECT" +
                " d.degree_id AS degree_id," +
                " d.code AS degree_code," +
                " d.label AS degree_label," +
                " o.option_id AS option_id," +
                " o.code AS option_code," +
                " o.label AS option_label" +
                " FROM degree d" +
                " LEFT OUTER JOIN option o ON o.degree_id = d.degree_id";

        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, new EmptySqlParameterSource());

        Map<String, Degree> degreeMap = new HashMap<>();

        Degree degree;
        Option option;
        while (rs.next()) {

            String degree_code = rs.getString("degree_code");
            if (degreeMap.containsKey(degree_code)) {
                degree = degreeMap.get(degree_code);
            } else {
                degree = new Degree();
                degree.setId(rs.getInt("degree_id"));
                degree.setCode(degree_code);
                degree.setLabel(rs.getString("degree_label"));

                degreeMap.put(degree_code, degree);
            }

            String option_code = rs.getString("option_code");
            if (StringUtils.isNotBlank(option_code)) {
                option = new Option();
                option.setId(rs.getInt("option_id"));
                option.setCode(option_code);
                option.setLabel(rs.getString("option_label"));

                degree.addOption(option);
            }
        }

        return degreeMap.values();
    }

    @Override
    public void create(Degree degree) {
        String sql = "INSERT INTO degree (code,label) VALUES (:code,:label)";

        MapSqlParameterSource params = toSqlParameterSource(degree);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(sql, params, keyHolder);

        degree.setId(keyHolder.getKey().intValue());

        degree.getOptions().forEach(o -> create(degree.getId(), o));
    }

    private void create(int degreeId, Option option) {
        String sql = "INSERT INTO option (code,label,degree_id) VALUES (:code,:label,:degree_id)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("code", option.getCode());
        params.addValue("label", option.getLabel());
        params.addValue("degree_id", degreeId);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(sql, params, keyHolder);

        option.setId(keyHolder.getKey().intValue());
    }

    private MapSqlParameterSource toSqlParameterSource(Degree degree) {
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

        degree.getOptions().forEach((option) -> {
            if (option.isToDelete()) {
                jdbcTemplate.update("DELETE FROM option WHERE option_id = :id", new MapSqlParameterSource("id", option.getId()));
            } else if (option.getId() == null) {
                create(degree.getId(), option);
            } else {
                update(option);
            }
        });
    }

    private void update(Option option) {
        String sql = "UPDATE option SET" +
                " code=:code," +
                " label=:label" +
                " WHERE option_id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("code", option.getCode());
        params.addValue("label", option.getLabel());
        params.addValue("id", option.getId());

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
