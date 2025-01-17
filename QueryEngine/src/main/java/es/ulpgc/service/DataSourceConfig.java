package es.ulpgc.service;

import es.ulpgc.data.CSVDataSource;
import es.ulpgc.data.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource csvDataSource() {
        return new CSVDataSource("/app/csv_data/index_content.csv", "/app/csv_data/index_metadata.csv");
    }
}
