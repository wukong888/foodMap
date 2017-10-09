package com.marketing.system.service;

import com.marketing.system.entity.Group;

import java.util.List;

public interface GroupService {

    List<Group> getGroup(int id);

    List<Group> getGroupNo();

    Group getGroupBySquadId(int SquadId);
}
