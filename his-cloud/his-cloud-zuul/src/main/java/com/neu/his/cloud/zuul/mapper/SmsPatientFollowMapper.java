package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.SmsPatientFollow;
import com.neu.his.cloud.zuul.model.SmsPatientFollowExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsPatientFollowMapper {
    int countByExample(SmsPatientFollowExample example);

    int deleteByExample(SmsPatientFollowExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsPatientFollow record);

    int insertSelective(SmsPatientFollow record);

    List<SmsPatientFollow> selectByExample(SmsPatientFollowExample example);

    SmsPatientFollow selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsPatientFollow record, @Param("example") SmsPatientFollowExample example);

    int updateByExample(@Param("record") SmsPatientFollow record, @Param("example") SmsPatientFollowExample example);

    int updateByPrimaryKeySelective(SmsPatientFollow record);

    int updateByPrimaryKey(SmsPatientFollow record);
}