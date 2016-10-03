package fr.frogdevelopment.assoplus.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class CoreListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("classpath:/sql/database_v1.0.sql")
    private Resource createTable;

    private final DataSource dataSource;

    @Autowired
    public CoreListener(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(createTable);
        databasePopulator.execute(dataSource);
    }
}
