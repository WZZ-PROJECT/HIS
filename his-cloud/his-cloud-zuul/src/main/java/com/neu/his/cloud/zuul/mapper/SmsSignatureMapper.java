package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.SmsSignature;
import com.neu.his.cloud.zuul.model.SmsSignatureExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsSignatureMapper {
    int countByExample(SmsSignatureExample example);

    int deleteByExample(SmsSignatureExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsSignature record);

    int insertSelective(SmsSignature record);

    List<SmsSignature> selectByExample(SmsSignatureExample example);

    SmsSignature selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsSignature record, @Param("example") SmsSignatureExample example);

    int updateByExample(@Param("record") SmsSignature record, @Param("example") SmsSignatureExample example);

    int updateByPrimaryKeySelective(SmsSignature record);

    int updateByPrimaryKey(SmsSignature record);
}