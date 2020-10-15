package com.neu.his.cloud.service.sms.mapper;

import com.neu.his.cloud.service.sms.model.SmsSetmealSkdRuleItem;
import com.neu.his.cloud.service.sms.model.SmsSetmealSkdRuleItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsSetmealSkdRuleItemMapper {
    int countByExample(SmsSetmealSkdRuleItemExample example);

    int deleteByExample(SmsSetmealSkdRuleItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsSetmealSkdRuleItem record);

    int insertSelective(SmsSetmealSkdRuleItem record);

    List<SmsSetmealSkdRuleItem> selectByExample(SmsSetmealSkdRuleItemExample example);

    SmsSetmealSkdRuleItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsSetmealSkdRuleItem record, @Param("example") SmsSetmealSkdRuleItemExample example);

    int updateByExample(@Param("record") SmsSetmealSkdRuleItem record, @Param("example") SmsSetmealSkdRuleItemExample example);

    int updateByPrimaryKeySelective(SmsSetmealSkdRuleItem record);

    int updateByPrimaryKey(SmsSetmealSkdRuleItem record);
}