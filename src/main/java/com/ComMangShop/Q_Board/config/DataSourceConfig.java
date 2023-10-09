package com.ComMangShop.Q_Board.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataSourceConfig {


	//	HikariCP DataSource
    @Bean
    public HikariDataSource dataSource()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/Q_Board");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }


}
