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

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.Driver;

/**
 * the jpa configuration for spring.
 *
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Configuration
@EnableTransactionManagement
@ImportResource("classpath*:*springDataConfig.xml")
public class JpaConfiguration {
	/**
	 * created the entity manager bean.
	 *
	 * @return the entity manager
	 */
	@Bean
	public final LocalContainerEntityManagerFactoryBean entityManager() {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(this.dataSource());
		return bean;
	}

	/**
	 * creates the transaction manager bean.
	 * @return the transaction manager.
	 */
	@Bean
	public final JpaTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setDataSource(dataSource());
		return txManager;
	}

	/**
	 * creates the datasource bean.
	 * @return the data source
	 */
	@Bean
	public final DataSource dataSource() {
		// TODO: externalize these strings using @PropertySource
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/docca?autoReconnect=true&amp;"
				+ "zeroDateTimeBehavior=convertToNull&amp;emulateLocators=true"
				+ "&amp;characterEncoding=UTF-8");
		dataSource.setUsername("cbroot");
		dataSource.setPassword("cbpassword");
		dataSource.setDriverClassName(Driver.class.getName());

		return dataSource;
	}
}

