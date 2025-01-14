package es.ulpgc.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource csvDataSource() {
        return new CSVDataSource("index_content.csv", "index_metadata.csv");
    }
}
