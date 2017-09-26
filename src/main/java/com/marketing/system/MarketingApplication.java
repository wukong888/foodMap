package com.marketing.system;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;
//@MapperScan("com.marketing.system.mapper")
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class})
public class MarketingApplication extends SpringBootServletInitializer {
	private static final Logger logger = LoggerFactory.getLogger(MarketingApplication.class);
	//DataSource配置
	/*@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource dataSource() {
		return new DataSource();
	}*/
	//提供SqlSeesion
	/*@Bean
	public SqlSessionFactory sqlSessionFactoryBean(PageHelper pageHelper) throws Exception {

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		//sqlSessionFactoryBean.setDataSource(dataSource());
		//自定义数据库配置的时候，需要将pageHelper的bean注入到Plugins中，
		// 如果采用系统默认的数据库配置，则只需要定义pageHelper的bean，会自动注入。
		sqlSessionFactoryBean.setPlugins(new Interceptor[] { pageHelper });

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));

		return sqlSessionFactoryBean.getObject();
	}*/

	/*@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}*/

	/*@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();

		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		p.setProperty("dialect", "mysql");
		p.setProperty("supportMethodsArguments", "false");
		p.setProperty("pageSizeZero", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}*/

	public static void main(String[] args) {
		SpringApplication.run(MarketingApplication.class, args);
		logger.info("============= SpringBoot Start Success =============");
	}
}
