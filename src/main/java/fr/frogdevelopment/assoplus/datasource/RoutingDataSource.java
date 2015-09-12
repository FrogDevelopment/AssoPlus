/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DataSource> contextHolder = new ThreadLocal<>();

    public static void setDataSource(DataSource dataSource) {
        Assert.notNull(dataSource, "dataSource cannot be null");
        contextHolder.set(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }

    public enum DataSource {
        SQLITE,
        MYSQL
    }
}