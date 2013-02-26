/*
 * Copyright by Akos Tajti (akos.tajti@gmail.com)
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Akos Tajti. ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Akos Tajti.
 */
package net.docca.backend.configurations;

import javax.sql.DataSource;

import net.docca.backend.Config;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * the jpa configuration for spring.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
@EnableTransactionManagement
@ImportResource("classpath*:*springDataConfig.xml")
@PropertySource(Config.DEFAULT_CONFIGURATION)
public class JpaConfiguration {
	/**
	 * the environment object injected by spring. used for accessing the configuration variables.
	 */
	@Autowired
	private Environment environment;

	/**
	 * created the entity manager bean.
	 *
	 * @return the entity manager
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManager() {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(this.dataSource());
		return bean;
	}

	/**
	 * creates the transaction manager bean.
	 * @return the transaction manager.
	 */
	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setDataSource(dataSource());
		return txManager;
	}

	/**
	 * creates the datasource bean.
	 * @return the data source
	 */
	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(environment.getProperty("database.jdbc.url"));
		dataSource.setUsername(environment.getProperty("database.user"));
		dataSource.setPassword(environment.getProperty("database.password"));
		dataSource.setDriverClassName(environment.getProperty("database.driver.name"));

		return dataSource;
	}
}

