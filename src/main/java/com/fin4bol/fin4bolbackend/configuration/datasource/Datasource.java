package com.fin4bol.fin4bolbackend.configuration.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Datasource class.
 */
@Configuration
public class Datasource {

    /**
     * Bean that take care of the connection with the database.
     *
     * @return DataSourceBuilder
     */
    @Bean
    @ConfigurationProperties("app.datasource")
    HikariDataSource hikariDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
