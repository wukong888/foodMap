package com.marketing.system.mapper_two;

import com.marketing.system.entity.TokenRecord;

import java.util.List;

public interface TokenRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TokenRecord record);

    int insertSelective(TokenRecord record);

    TokenRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TokenRecord record);

    int updateByPrimaryKey(TokenRecord record);

    List<TokenRecord> selectByToken(String token);
}