package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.SmsSetmealSkd;
import com.neu.his.cloud.zuul.model.SmsSetmealSkdExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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