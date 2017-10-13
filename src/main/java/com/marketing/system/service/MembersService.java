package com.marketing.system.service;

import com.marketing.system.entity.Members;

import java.util.List;

public interface MembersService {

    List<Members> getMembersById(String squadId);
}
