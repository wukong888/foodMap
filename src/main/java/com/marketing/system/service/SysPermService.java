package com.marketing.system.service;

import com.marketing.system.entity.SysPerm;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

public interface SysPermService {

    List<SysPerm> selectAll();
}
