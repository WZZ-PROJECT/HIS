package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.SmsSignatureType;
import com.neu.his.cloud.zuul.model.SmsSignatureTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SmsSignatureTypeMapper {
    int countByExample(SmsSignatureTypeExample example);

    int deleteByExample(SmsSignatureTypeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsSignatureType record);

    int insertSelective(SmsSignatureType record);

    List<SmsSignatureType> selectByExample(SmsSignatureTypeExample example);

    SmsSignatureType selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsSignatureType record, @Param("example") SmsSignatureTypeExample example);

    int updateByExample(@Param("record") SmsSignatureType record, @Param("example") SmsSignatureTypeExample example);

    int updateByPrimaryKeySelective(SmsSignatureType record);

    int updateByPrimaryKey(SmsSignatureType record);
}