/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.event.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import fr.frogdevelopment.assoplus.event.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PublishDaoImpl implements PublishDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishDaoImpl.class);

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setMysqlDataSource(MysqlDataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.MANDATORY, value = "mysql")
    public final boolean publish(Event event) {
        String query = String.format("INSERT INTO EVENT (TITLE, DATE, TEXT) VALUES ('%s', '%s', '%s')", event.getTitle(), event.getDate(), event.getText());
        LOGGER.debug("execute query : {}", query);
        int nb = this.jdbcTemplate.update(query);

        return nb == 1;
    }

}
