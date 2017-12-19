package com.diyiliu.support.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description: WebConfiguration
 * Author: DIYILIU
 * Update: 2017-12-11 13:36
 */

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Primary
    @Bean("sdb")
    @ConfigurationProperties(prefix = "xcmg.datasource.sdb")
    public DataSource serviceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("cdb")
    @ConfigurationProperties(prefix = "xcmg.datasource.cdb")
    public DataSource coreDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "serviceJdbcTemplate")
    public JdbcTemplate serviceJdbcTemplate() {

        return new JdbcTemplate(serviceDataSource());
    }

    @Bean(name = "coreJdbcTemplate")
    public JdbcTemplate coreJdbcTemplate() {

        return new JdbcTemplate(coreDataSource());
    }

    @Bean
    public Executor xcmgExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(5);
        executor.setThreadNamePrefix("xcmg-dbExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.initialize();

        return executor;
    }
}
