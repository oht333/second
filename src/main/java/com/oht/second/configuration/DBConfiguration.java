package com.oht.second.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration // 환경설정 파일
@PropertySource("classpath:/application.properties")
public class DBConfiguration {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean // return 되는 객체를 IoC컨테이너에 등록
	@ConfigurationProperties(prefix="spring.datasource.hikari")//classpath에 위치한 해당 접두어로 시작하는 내용 읽기
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	
	@Bean
	public DataSource dataSource() {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		System.out.println(dataSource.toString());
		//hikari-Pool-1 -> 성공
		return dataSource;
	}
	
	@Autowired
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		
		factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/*Mapper.xml"));
		factoryBean.setTypeAliasesPackage("com.oht.second.vo");
		
		return factoryBean.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}

