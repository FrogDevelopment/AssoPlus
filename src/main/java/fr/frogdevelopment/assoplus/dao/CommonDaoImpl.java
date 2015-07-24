package fr.frogdevelopment.assoplus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import fr.frogdevelopment.assoplus.entities.Entity;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public abstract class CommonDaoImpl<E extends Entity> implements CommonDao<E> {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private final Class<E> persistentClass;

    protected RowMapper<E> mapper;

    @SuppressWarnings("unchecked")
    public CommonDaoImpl() {
        persistentClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @PostConstruct
    private void init() {
        mapper = buildMapper();
    }

    protected abstract RowMapper<E> buildMapper();

    // ***************************************** \\
    // ********** PRIVATE METHODES ************* \\
    // ***************************************** \\


    // ******************************************* \\
    // ********** PROTECTED METHODES ************* \\
    // ******************************************* \\

    /**
     * @return Le nom de l'entité du DAO.
     * @see Class#getSimpleName()
     */
    protected final String getEntityName() {
        return this.persistentClass.getSimpleName(); // fixme récupérer le nom de la table via l'annotation @Table de la classe
    }

    // **************************************** \\
    // ********** PUBLIC METHODES ************* \\
    // **************************************** \\

    @Override
    public List<E> getAll() {
        return jdbcTemplate.query("SELECT * FROM " + getEntityName(), mapper);
    }

    public List<E> getAllOrderedBy(String propertyName) {
        return jdbcTemplate.query("SELECT * FROM " + getEntityName() + " ORDER BY " + propertyName, mapper); // FIXME voir pour le champ propertyName en dynamique
    }

    public E getById(Long identifiant) {
        return jdbcTemplate.queryForObject("SELECT * FROM " + getEntityName() + " WHERE ID = ?", new Object[]{identifiant}, mapper);
    }

    abstract public void save(E entity);

    public void saveAll(Collection<E> entities) {
        entities.forEach(this::save);
    }

    abstract public void update(E entity);

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

    public void delete(Long identifiant) {
        jdbcTemplate.update("DELETE FROM " + getEntityName() + " WHERE ID= ?", identifiant); // FIXME voir pour le champ ID en dynamique
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM " + getEntityName());
    }

}
