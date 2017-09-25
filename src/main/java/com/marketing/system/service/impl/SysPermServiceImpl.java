package com.marketing.system.service.impl;

import com.marketing.system.entity.SysPerm;
import com.marketing.system.mapper.SysPermMapper;
import com.marketing.system.service.SysPermService;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysPermServiceImpl implements SysPermService{

    @Resource
    private SysPermMapper sysPermMapper;

    @Override
    public List<SysPerm> selectAll() {
        return sysPermMapper.selectAll();
    }
}
