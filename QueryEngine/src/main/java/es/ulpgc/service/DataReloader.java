package es.ulpgc.service;

import es.ulpgc.data.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataReloader {
    private final ApplicationContext context;
    private final QueryEngine queryEngine;

    public DataReloader(ApplicationContext context, QueryEngine queryEngine) {
        this.context = context;
        this.queryEngine = queryEngine;
    }

    @Scheduled(fixedRate = 60000) // Reload every 60 seconds
    public void reloadData() {
        DataSource dataSource = context.getBean(DataSource.class); // New instance each time
        queryEngine.reloadIndex(dataSource);
    }
}