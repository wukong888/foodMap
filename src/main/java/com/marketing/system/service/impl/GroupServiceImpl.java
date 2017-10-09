package com.marketing.system.service.impl;

import com.marketing.system.entity.Group;
import com.marketing.system.mapper_two.GroupMapper;
import com.marketing.system.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public List<Group> getGroup(int id) {

        return groupMapper.getGroup(id);
    }

    @Override
    public List<Group> getGroupNo() {

        return groupMapper.getGroupNo();
    }

    @Override
    public Group getGroupBySquadId(int SquadId) {

        return groupMapper.getGroupBySquadId(SquadId);
    }
}
