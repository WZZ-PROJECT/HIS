package com.neu.his.cloud.service.sms.mapper;

import com.neu.his.cloud.service.sms.model.SmsSetmealSkd;
import com.neu.his.cloud.service.sms.model.SmsSetmealSkdExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsSetmealSkdMapper {
    int countByExample(SmsSetmealSkdExample example);

    int deleteByExample(SmsSetmealSkdExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsSetmealSkd record);

    int insertSelective(SmsSetmealSkd record);

    List<SmsSetmealSkd> selectByExample(SmsSetmealSkdExample example);

    SmsSetmealSkd selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsSetmealSkd record, @Param("example") SmsSetmealSkdExample example);

    int updateByExample(@Param("record") SmsSetmealSkd record, @Param("example") SmsSetmealSkdExample example);

    int updateByPrimaryKeySelective(SmsSetmealSkd record);

    int updateByPrimaryKey(SmsSetmealSkd record);
}