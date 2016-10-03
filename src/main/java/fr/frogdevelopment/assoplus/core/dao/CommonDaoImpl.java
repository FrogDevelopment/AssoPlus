/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.core.entity.Entity;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommonDaoImpl<E extends Entity> implements CommonDao<E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setSqliteDataSource(BasicDataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final Class<E> persistentClass;
    protected final RowMapper<E> mapper = (rs, rowNum) -> buildEntity(rs);

    protected final String idName;
    protected final String tableName;

    @SuppressWarnings("unchecked")
    public CommonDaoImpl() {

        persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (!persistentClass.isAnnotationPresent(javax.persistence.Entity.class)) {
            throw new IllegalStateException("Not an entity !!");
        }

        if (persistentClass.isAnnotationPresent(Table.class)) {
            tableName = persistentClass.getDeclaredAnnotation(Table.class).name();
        } else {
            throw new IllegalStateException("No table defined !!");
        }

        String targetId = null;
        for (Field field : persistentClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                targetId = field.getAnnotation(Column.class).name();
                break;
            }
        }

        if (StringUtils.isNoneBlank(targetId)) {
            idName = targetId;
        } else {
            throw new IllegalStateException("No Id defined !!");
        }

    }

    // ***************************************** \\
    // ********** PACKAGE METHODES ************* \\ (visible for tests)
    // ***************************************** \\
    @PostConstruct
    void init() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName);
        sb.append("(");

        List<String> columns = new ArrayList<>();
        List<String> foreignKeys = new ArrayList<>();
        Column column;
        ForeignKey foreignKey;
        for (Field field : persistentClass.getDeclaredFields()) {
            // Simple case
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                Class<?> type = field.getType();
                String col = column.name() + " ";
                if (type == Integer.class || type == int.class
                        || type == Long.class || type == long.class) {
                    col += "INTEGER";
                    if (field.isAnnotationPresent(Id.class)) {
                        col += " PRIMARY KEY";
                    }
                    if (field.isAnnotationPresent(GeneratedValue.class)) {
                        col += " AUTOINCREMENT";
                    }
                } else if (type == String.class) {
                    col += "TEXT";
                } else if (type == Float.class || type == float.class
                        || type == Double.class || type == double.class) {
                    col += "REAL";
                }

                if (!field.isAnnotationPresent(Id.class)) {
                    if (column.unique()) {
                        col += " UNIQUE";
                    }

                    if (!column.nullable()) {
                        col += " NOT NULL";
                    }
                }
                columns.add(col);
            }

            if (field.isAnnotationPresent(ForeignKey.class)) {
                foreignKey = field.getAnnotation(ForeignKey.class);
                foreignKeys.add(String.format("FOREIGN KEY(%s) REFERENCES %s(%s)", foreignKey.primaryKey(), foreignKey.table(), foreignKey.primaryKey()));
            }
        }

        columns.addAll(foreignKeys);

        sb.append(String.join(", ", columns));
        sb.append(")");

        LOGGER.debug("Execute query {}", sb.toString());
        jdbcTemplate.update(sb.toString());
    }

    E buildEntity(ResultSet rs) throws SQLException {
        try {
            E entity = persistentClass.newInstance();

            Column column;
            for (Field field : persistentClass.getDeclaredFields()) {
                // Simple case
                if (field.isAnnotationPresent(Column.class)) {
                    // to be able to write the field's value
                    if (!field.isAccessible()) {
                        AccessController.doPrivileged((PrivilegedAction<E>) () -> {
                            field.setAccessible(true);

                            return null;
                        });
                    }

                    column = field.getAnnotation(Column.class);
                    Class<?> type = field.getType();
                    field.set(entity, type.cast(rs.getObject(column.name())));
                }
            }

            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Error while constructing entity", e);
        }
    }

    // **************************************** \\
    // ********** PUBLIC METHODES ************* \\
    // **************************************** \\

    @Override
    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public List<E> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + tableName, mapper);
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public E getById(Integer identifiant) {
        return jdbcTemplate.queryForObject("SELECT * FROM " + tableName + " WHERE " + idName + " = ?", new Object[]{identifiant}, mapper);
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public final void save(E entity) {
        try {
            Column column;
            Map<String, String> map = new HashMap<>();
            for (Field field : persistentClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                if (field.isAnnotationPresent(Id.class)) {
                    continue;
                }

                column = field.getAnnotation(Column.class);

                // to be able to access the field's value
                if (!field.isAccessible()) {
                    AccessController.doPrivileged((PrivilegedAction<E>) () -> {
                        field.setAccessible(true);

                        return null;
                    });
                }

                Object value = field.get(entity);
                if (value == null) {
                    map.put(column.name(), null);
                } else {
                    final String valueOf = String.valueOf(value);
                    if (field.getType() == String.class) {
                        map.put(column.name(), "'" + valueOf.replace("'", "''") + "'");
                    } else {
                        map.put(column.name(), valueOf);
                    }
                }
            }

            String query = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, String.join(", ", map.keySet()), String.join(",", map.values()));
            LOGGER.debug("execute query : {}", query);
            this.jdbcTemplate.update(con -> con.prepareStatement(query, new String[]{idName}), keyHolder);

            entity.setId(keyHolder.getKey().intValue());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Error while constructing query", e); // fixme
        }
    }

    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public void saveAll(Collection<E> entities) {
        entities.forEach(this::save);
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public final void update(E entity) {
        try {
            Column column;
            Collection<String> setValues = new ArrayList<>();
            for (Field field : persistentClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                if (field.isAnnotationPresent(Id.class)) {
                    continue;
                }

                column = field.getAnnotation(Column.class);

                // to be able to write the field's value
                if (!field.isAccessible()) {
                    AccessController.doPrivileged((PrivilegedAction<E>) () -> {
                        field.setAccessible(true);

                        return null;
                    });
                }

                Object value = field.get(entity);
                if (value == null) {
                    setValues.add(column.name() + " = " + null);
                } else {
                    String valueOf = String.valueOf(value);
                    if (field.getType() == String.class) {
                        setValues.add(column.name() + "=" + "'" + valueOf.replace("'", "''") + "'");
                    } else {
                        setValues.add(column.name() + "=" + valueOf);
                    }
                }
            }

            String query = String.format("UPDATE %s SET %s WHERE %s = %s", tableName, String.join(", ", setValues), idName, entity.getId());
            LOGGER.debug("execute query : {}", query);
            this.jdbcTemplate.update(query);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Error while constructing query", e); // fixme
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public void updateAll(Collection<E> entities) {
        entities.forEach(this::update);
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public void saveOrUpdate(E entity) {
        if (entity.getId() == 0) {
            save(entity);
        } else {
            update(entity);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public void saveOrUpdateAll(Collection<E> entities) {
        entities.forEach(this::saveOrUpdate);
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public void delete(E entity) {
        delete(entity.getId());
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public void delete(Integer identifiant) {
        jdbcTemplate.update("DELETE FROM " + tableName + " WHERE " + idName + " = ?", identifiant);
    }

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

}