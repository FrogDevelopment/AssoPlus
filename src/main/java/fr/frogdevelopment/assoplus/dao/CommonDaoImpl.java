package fr.frogdevelopment.assoplus.dao;

import fr.frogdevelopment.assoplus.entities.Entity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

public abstract class CommonDaoImpl<E extends Entity> implements CommonDao<E> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	private final Class<E> persistentClass;
	protected final RowMapper<E> mapper;

	private final String idName;
	private final String tableName;

	@SuppressWarnings("unchecked")
	public CommonDaoImpl() {

		persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		if (!persistentClass.isAnnotationPresent(javax.persistence.Entity.class)) {
			throw new IllegalStateException("Not an entity !!");
		}

		if (persistentClass.isAnnotationPresent(Table.class)) {
			tableName = persistentClass.getDeclaredAnnotation(Table.class).name().toUpperCase();
		} else {
			throw new IllegalStateException("No table defined !!");
		}

		if (persistentClass.isAnnotationPresent(Id.class)) {
			idName = persistentClass.getDeclaredAnnotation(Column.class).name().toUpperCase();
		} else {
			throw new IllegalStateException("No Id defined !!");
		}

		mapper = buildMapper();
	}

	// ***************************************** \\
	// ********** PRIVATE METHODES ************* \\
	// ***************************************** \\

	private RowMapper<E> buildMapper() {
		return (rs, rowNum) -> {
			E entity = null;

			try {
				entity = persistentClass.newInstance();

				Column column;
				for (Field field : persistentClass.getDeclaredFields()) {
					// Simple case
					if (field.isAnnotationPresent(Column.class)) {
						// to be able to write the field's value
						AccessController.doPrivileged((PrivilegedAction<E>) () -> {
							field.setAccessible(true);

							return null;
						});

						column = field.getAnnotation(Column.class);
						Class<?> type = field.getType();
						field.set(entity, type.cast(rs.getObject(column.name())));
					}
				}

				// Collection case

			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return entity;
		};
	}

	// ******************************************* \\
	// ********** PROTECTED METHODES ************* \\
	// ******************************************* \\

	protected final String getTableName() {
		return tableName;
	}

	// **************************************** \\
	// ********** PUBLIC METHODES ************* \\
	// **************************************** \\

	@Override
	public List<E> getAll() {
		return jdbcTemplate.query("SELECT * FROM " + getTableName(), mapper);
	}

	public List<E> getAllOrderedBy(String propertyName) {
		return jdbcTemplate.query("SELECT * FROM " + getTableName() + " ORDER BY " + propertyName, mapper); // FIXME voir pour le champ propertyName en dynamique
	}

	public E getById(Integer identifiant) {
		return jdbcTemplate.queryForObject("SELECT * FROM " + getTableName() + " WHERE " + idName + " = ?", new Object[]{identifiant}, mapper);
	}

	public final void save(E entity) {
		try {
			Column column;
			Map<String, String> map = new HashMap<>();
			for (Field field : persistentClass.getDeclaredFields()) {
				column = field.getAnnotation(Column.class);

				// to be able to write the field's value
				AccessController.doPrivileged((PrivilegedAction<E>) () -> {
					field.setAccessible(true);

					return null;
				});

				Object value = field.get(entity);
				map.put(column.name(), String.valueOf(value));
			}

			String query = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, String.join(",", map.keySet()), String.join(",", map.values()));
			LOGGER.debug("execute query : {}", query);
			this.jdbcTemplate.update(query);
		} catch (IllegalAccessException e) {
			throw  new IllegalStateException("Error while constructing query", e); // fixme
		}
	}

	public void saveAll(Collection<E> entities) {
		entities.forEach(this::save);
	}

	public final void update(E entity) {
		try {
			Column column;
			Collection<String> setValues = new ArrayList<>();
			for (Field field : persistentClass.getDeclaredFields()) {
				column = field.getAnnotation(Column.class);

				// to be able to write the field's value
				AccessController.doPrivileged((PrivilegedAction<E>) () -> {
					field.setAccessible(true);

					return null;
				});

				Object value = field.get(entity);
				setValues.add(column.name() + " = " + String.valueOf(value));
			}

			String query = String.format("UPDATE %s %s WHERE %s = %s", tableName, String.join(",", setValues), idName, entity.getId());
			LOGGER.debug("execute query : {}", query);
			this.jdbcTemplate.update(query);
		} catch (IllegalAccessException e) {
			throw  new IllegalStateException("Error while constructing query", e); // fixme
		}
	}

	public void updateAll(Collection<E> entities) {
		entities.forEach(this::update);
	}

	public void saveOrUpdate(E entity) {
		if (entity.getId() == 0) {
			save(entity);
		} else {
			update(entity);
		}
	}

	public void saveOrUpdateAll(Collection<E> entities) {
		entities.forEach(this::saveOrUpdate);
	}

	public void delete(E entity) {
		delete(entity.getId());
	}

	public void delete(Integer identifiant) {
		jdbcTemplate.update("DELETE FROM " + getTableName() + " WHERE " + idName + " = ?", identifiant);
	}

	public void deleteAll() {
		jdbcTemplate.update("DELETE FROM " + getTableName());
	}

}
