/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dao;

import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import fr.frogdevelopment.assoplus.core.entity.EntityTest;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CommonDaoTest {

    class MockitoDao extends CommonDaoImpl<EntityTest> {
    }

    @InjectMocks
    private MockitoDao commonDao = new MockitoDao();

    @Mock
    protected JdbcTemplate jdbcTemplate;

    @Test
    @Ignore
    public void testQueryCreation() {
        StrBuilder sb = new StrBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(EntityTest.TABLE_NAME);
        sb.append("(");
        sb.append(EntityTest.COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(EntityTest.COLUMN_CODE).append(" TEXT UNIQUE NOT NULL, ");
        sb.append(EntityTest.COLUMN_LABEL).append(" TEXT NOT NULL");
        sb.append(")");

        commonDao.init();

        verify(jdbcTemplate).update(sb.toString());
    }

    @Test
    public void testQueryGetAll() {
        commonDao.getAll();
        verify(jdbcTemplate).query("SELECT * FROM " + EntityTest.TABLE_NAME, commonDao.mapper);
    }

    @Test
    public void testQueryGetById() {
        commonDao.getById(0);
        verify(jdbcTemplate).queryForObject("SELECT * FROM " + EntityTest.TABLE_NAME + " WHERE " + EntityTest.COLUMN_ID + " = ?", new Object[]{0}, commonDao.mapper);
    }

    @Test
    public void testDelete() {
        EntityTest test = new EntityTest();
        test.setId(0);
        commonDao.delete(test);
        verify(jdbcTemplate).update("DELETE FROM " + EntityTest.TABLE_NAME + " WHERE " + EntityTest.COLUMN_ID + " = ?", 0);
    }

    @Test
    public void testDeleteAll() {
        commonDao.deleteAll();
        verify(jdbcTemplate).update("DELETE FROM " + EntityTest.TABLE_NAME);
    }

    @Test
    @Ignore
    public void testSaveOrUpdateAll() {
        Collection<EntityTest> testEntities = new ArrayList<>();
        testEntities.add(new EntityTest(0));
        testEntities.add(new EntityTest(1));

        commonDao.saveOrUpdateAll(testEntities);

        doNothing().when(commonDao).save(any(EntityTest.class));
        doNothing().when(commonDao).update(any(EntityTest.class));
//        when(commonDao.saveOrUpdate(any(TestEntity.class))).then(invocation -> return);
        verify(commonDao, times(testEntities.size())).saveOrUpdate(any(EntityTest.class));

        verify(commonDao, times(1)).save(any(EntityTest.class));
        verify(commonDao, times(1)).update(any(EntityTest.class));
    }

    @Test
    @Ignore
    public void testSaveAll() {
        Collection<EntityTest> testEntities = new ArrayList<>();
        testEntities.add(new EntityTest(0));

        commonDao.saveAll(testEntities);

        doNothing().when(spy(commonDao)).save(any(EntityTest.class));
        verify(commonDao, times(testEntities.size())).save(any(EntityTest.class));
    }

    @Test
    @Ignore
    public void testUpdateAll() {
        Collection<EntityTest> testEntities = new ArrayList<>();
        testEntities.add(new EntityTest(1));

        commonDao.updateAll(testEntities);

        doNothing().when(commonDao).update(any(EntityTest.class));
        verify(commonDao, times(testEntities.size())).save(any(EntityTest.class));
    }

//    @Test
//    public void testMapper() {
//        Mockito.when(jdbcTemplate.query(Mockito.anyString(), commonDao.mapper)).then(invocation -> {
//
//        });
//    }

}