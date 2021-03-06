package com.marketing.system.util;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.marketing.system.mapper_two", sqlSessionTemplateRef = "SqlSessionTemplate_two")
public class DataSource2Config {

    @Bean(name = "second-DataSource")
    @ConfigurationProperties(prefix = "spring.second-datasource")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "SqlSessionFactory_two")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("second-DataSource") DataSource dataSource, PageHelper pageHelper) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(new Interceptor[]{pageHelper});
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mybatis_two/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "TransactionManager_two")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("second-DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "SqlSessionTemplate_two")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("SqlSessionFactory_two") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
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
    }

}