/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.dao;

import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import fr.frogdevelopment.assoplus.entity.TestEntity;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CommonDaoTest {

    class MockitoDao extends CommonDaoImpl<TestEntity> {
    }

    @InjectMocks
    private MockitoDao commonDao = new MockitoDao();

    @Mock
    protected JdbcTemplate jdbcTemplate;

    @Test
    public void testQueryCreation() {
        StrBuilder sb = new StrBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(TestEntity.TABLE_NAME);
        sb.append("(");
        sb.append(TestEntity.COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(TestEntity.COLUMN_CODE).append(" TEXT UNIQUE NOT NULL, ");
        sb.append(TestEntity.COLUMN_LABEL).append(" TEXT NOT NULL");
        sb.append(")");

        commonDao.init();

        verify(jdbcTemplate).update(sb.toString());
    }

    @Test
    public void testQueryGetAll() {
        commonDao.getAll();
        verify(jdbcTemplate).query("SELECT * FROM " + TestEntity.TABLE_NAME, commonDao.mapper);
    }

    @Test
    public void testQueryGetAllOrderedBy() {
        commonDao.getAllOrderedBy(TestEntity.COLUMN_CODE);
        verify(jdbcTemplate).query("SELECT * FROM " + TestEntity.TABLE_NAME + " ORDER BY " + TestEntity.COLUMN_CODE, commonDao.mapper);
    }

    @Test
    public void testQueryGetById() {
        commonDao.getById(0);
        verify(jdbcTemplate).queryForObject("SELECT * FROM " + TestEntity.TABLE_NAME + " WHERE " + TestEntity.COLUMN_ID + " = ?", new Object[]{0}, commonDao.mapper);
    }

    @Test
    public void testDelete() {
        TestEntity test = new TestEntity();
        test.setId(0);
        commonDao.delete(test);
        verify(jdbcTemplate).update("DELETE FROM " + TestEntity.TABLE_NAME + " WHERE " + TestEntity.COLUMN_ID + " = ?", 0);
    }

    @Test
    public void testDeleteAll() {
        commonDao.deleteAll();
        verify(jdbcTemplate).update("DELETE FROM " + TestEntity.TABLE_NAME);
    }

    @Test
    public void testSaveOrUpdateAll() {
        Collection<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(new TestEntity(0));
        testEntities.add(new TestEntity(1));

        commonDao.saveOrUpdateAll(testEntities);

        doNothing().when(commonDao).save(any(TestEntity.class));
        doNothing().when(commonDao).update(any(TestEntity.class));
//        when(commonDao.saveOrUpdate(any(TestEntity.class))).then(invocation -> return);
        verify(commonDao, times(testEntities.size())).saveOrUpdate(any(TestEntity.class));

        verify(commonDao, times(1)).save(any(TestEntity.class));
        verify(commonDao, times(1)).update(any(TestEntity.class));
    }

    @Test
    public void testSaveAll() {
        Collection<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(new TestEntity(0));

        commonDao.saveAll(testEntities);

        doNothing().when(spy(commonDao)).save(any(TestEntity.class));
        verify(commonDao, times(testEntities.size())).save(any(TestEntity.class));
    }

    @Test
    public void testUpdateAll() {
        Collection<TestEntity> testEntities = new ArrayList<>();
        testEntities.add(new TestEntity(1));

        commonDao.updateAll(testEntities);

        doNothing().when(commonDao).update(any(TestEntity.class));
        verify(commonDao, times(testEntities.size())).save(any(TestEntity.class));
    }

//    @Test
//    public void testMapper() {
//        Mockito.when(jdbcTemplate.query(Mockito.anyString(), commonDao.mapper)).then(invocation -> {
//
//        });
//    }

}