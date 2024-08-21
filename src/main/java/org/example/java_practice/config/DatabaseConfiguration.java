package org.example.java_practice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories( basePackages = {"org.example.java_practice.dao"})
@RequiredArgsConstructor
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    private String primaryUrl;

    @Value("${spring.datasource.username}")
    private String primaryUser;

    @Value("${spring.datasource.password}")
    private String primaryPassword;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private Integer connectionTimeout;

    @Value("${spring.datasource.hikari.idle-timeout}")
    private Integer idleTimeout;

    @Value("${spring.datasource.hikari.max-lifetime}")
    private Integer maxLifeTime;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private Integer minimumIdle;

    @Value("${spring.datasource.hikari.transaction-isolation}")
    private String transactionIsolation;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private Integer maximumPoolSize;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_versioned_data}")
    private Boolean batchVersionData;

    @Value("${spring.jpa.properties.hibernate.order_updates}")
    private Boolean orderUpdate;

    @Value("${spring.jpa.properties.hibernate.order_inserts}")
    private Boolean orderInsert;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Integer batchSize;

}

