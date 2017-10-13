package com.marketing.system.service.impl;

import com.marketing.system.entity.Members;
import com.marketing.system.mapper_two.MembersMapper;
import com.marketing.system.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembersServiceImpl implements MembersService {

    @Autowired
    private MembersMapper membersMapper;

    @Override
    public List<Members> getMembersById(String squadId) {

        List<Members> list = membersMapper.getMembersBysquadId(squadId);

        return list;
    }
}
