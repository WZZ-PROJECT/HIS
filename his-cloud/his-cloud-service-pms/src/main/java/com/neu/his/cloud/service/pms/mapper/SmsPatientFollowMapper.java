package com.neu.his.cloud.service.pms.mapper;

import com.neu.his.cloud.service.pms.model.SmsPatientFollow;
import com.neu.his.cloud.service.pms.model.SmsPatientFollowExample;
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