package com.neu.his.cloud.service.sms.mapper;

import com.neu.his.cloud.service.sms.model.SmsSetmealSkdRule;
import com.neu.his.cloud.service.sms.model.SmsSetmealSkdRuleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsSetmealSkdRuleMapper {
    int countByExample(SmsSetmealSkdRuleExample example);

    int deleteByExample(SmsSetmealSkdRuleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsSetmealSkdRule record);

    int insertSelective(SmsSetmealSkdRule record);

    List<SmsSetmealSkdRule> selectByExample(SmsSetmealSkdRuleExample example);

    SmsSetmealSkdRule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsSetmealSkdRule record, @Param("example") SmsSetmealSkdRuleExample example);

    int updateByExample(@Param("record") SmsSetmealSkdRule record, @Param("example") SmsSetmealSkdRuleExample example);

    int updateByPrimaryKeySelective(SmsSetmealSkdRule record);

    int updateByPrimaryKey(SmsSetmealSkdRule record);
}