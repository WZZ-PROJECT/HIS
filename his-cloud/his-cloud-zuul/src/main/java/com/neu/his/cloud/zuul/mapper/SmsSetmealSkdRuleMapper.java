package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.SmsSetmealSkdRule;
import com.neu.his.cloud.zuul.model.SmsSetmealSkdRuleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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