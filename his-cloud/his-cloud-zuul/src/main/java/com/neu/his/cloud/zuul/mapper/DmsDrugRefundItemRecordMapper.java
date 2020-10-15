package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.DmsDrugRefundItemRecord;
import com.neu.his.cloud.zuul.model.DmsDrugRefundItemRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DmsDrugRefundItemRecordMapper {
    int countByExample(DmsDrugRefundItemRecordExample example);

    int deleteByExample(DmsDrugRefundItemRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsDrugRefundItemRecord record);

    int insertSelective(DmsDrugRefundItemRecord record);

    List<DmsDrugRefundItemRecord> selectByExample(DmsDrugRefundItemRecordExample example);

    DmsDrugRefundItemRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsDrugRefundItemRecord record, @Param("example") DmsDrugRefundItemRecordExample example);

    int updateByExample(@Param("record") DmsDrugRefundItemRecord record, @Param("example") DmsDrugRefundItemRecordExample example);

    int updateByPrimaryKeySelective(DmsDrugRefundItemRecord record);

    int updateByPrimaryKey(DmsDrugRefundItemRecord record);
}