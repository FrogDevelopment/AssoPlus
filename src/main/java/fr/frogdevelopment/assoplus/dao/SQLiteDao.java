package fr.frogdevelopment.assoplus.dao;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.frogdevelopment.assoplus.entities.Entity;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SQLiteDao<E extends Entity> {

    private final Class<E> persistentClass;

    @SuppressWarnings("unchecked")
    public SQLiteDao() {
        persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    // ******************************************* \\
    // ********** PROTECTED METHODES ************* \\
    // ******************************************* \\

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite::sample.db");
    }

    protected Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    /**
     * @return Le nom de l'entité du DAO.
     * @see Class#getSimpleName()
     */
    protected final String getEntityName() {
        return this.persistentClass.getSimpleName();
    }

    // **************************************** \\
    // ********** PUBLIC METHODES ************* \\
    // **************************************** \\

    public void deleteAll() throws SQLException {
        getStatement().executeUpdate("DELETE FROM " + getEntityName());
    }

    public List<E> getAll() throws SQLException {
        ResultSet resultSet = getStatement().executeQuery("SELECT * FROM " + getEntityName());

        return extractEntities(resultSet);
    }

    public List<E> getAllOrderedBy(String propertyName) throws SQLException {
        ResultSet resultSet = getStatement().executeQuery("SELECT * FROM " + getEntityName() + " ORDER BY " + propertyName);

        return extractEntities(resultSet);
    }

    public E getById(Serializable identifiant) throws SQLException {
        Statement statement = getStatement();
        statement.setMaxRows(1);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + getEntityName() + " WHERE ID=" + identifiant); // FIXME voir pour le champ ID en dynamqiue

        List<E> entities = extractEntities(resultSet);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }

        return entities.get(0);
    }


    // ************************************ \\
    // ********** TRANSOCDAGE ************* \\
    // ************************************ \\

    protected AbstractTrancoder transcoder;

    @PostConstruct
    protected void init() throws SQLException {
        transcoder = buildTranscoder();
    }

    protected abstract AbstractTrancoder buildTranscoder();

    private List<E> extractEntities(ResultSet resultSet) throws SQLException {
        List<E> entities = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        Map<String, String> data;
        while (resultSet.next()) {
            data = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                data.put(metaData.getColumnName(i), resultSet.getString(i));
            }

            entities.add(transcoder.execute(data));
        }
        return entities;
    }

    protected abstract class AbstractTrancoder {

        private final Logger LOGGER = LoggerFactory.getLogger(AbstractTrancoder.class);

        private Map<String, String> bean;

        public final E execute(Map<String, String> bean) {
            if (bean == null) {
                throw new IllegalArgumentException("Erreur de transcodification : la variable d\'entrée est nulle !");
            } else {
                this.bean = bean;
                return this.transcode(bean);
            }
        }

        protected abstract E transcode(Map<String, String> data);

        protected String getValue(String key) {
            return bean.get(key.toUpperCase());
        }

        protected int getInt(String key) {
            Number n = this.getNumber(key);
            return n == null ? 0 : n.intValue();
        }

        protected long getLong(String key) {
            Number n = this.getNumber(key);
            return n == null ? 0L : n.longValue();
        }

        protected double getDouble(String key) {
            Number n = this.getNumber(key);
            return n == null ? 0.0D : n.doubleValue();
        }

        protected Date getDate(String key) {
            return this.getDate(key, "dd/MM/yyyy");
        }

        protected Date getDate(String key, String dateFormat) {
            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            String value = this.getValue(key);
            Date out = null;
            if (StringUtils.isNotBlank(value)) {
                try {
                    out = df.parse(value);
                } catch (ParseException e) {
                    LOGGER.error("Error during date parsing key={}, value={} : {}", key, value, e);
                }
            }

            return out;
        }

        protected Number getNumber(String key) {
            Number n = null;
            String value = this.getValue(key);
            if (StringUtils.isNotBlank(value)) {
                try {
                    n = NumberFormat.getInstance().parse(value);
                } catch (ParseException e) {
                    LOGGER.error("Error during number parsing key={}, value={} : {}", key, value, e);
                }
            }

            return n;
        }
    }

}
