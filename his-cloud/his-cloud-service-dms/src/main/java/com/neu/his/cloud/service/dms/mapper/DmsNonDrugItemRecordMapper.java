package com.neu.his.cloud.service.dms.mapper;

import com.neu.his.cloud.service.dms.model.DmsNonDrugItemRecord;
import com.neu.his.cloud.service.dms.model.DmsNonDrugItemRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DmsNonDrugItemRecordMapper {
    int countByExample(DmsNonDrugItemRecordExample example);

    int deleteByExample(DmsNonDrugItemRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsNonDrugItemRecord record);

    int insertSelective(DmsNonDrugItemRecord record);

    List<DmsNonDrugItemRecord> selectByExample(DmsNonDrugItemRecordExample example);

    DmsNonDrugItemRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsNonDrugItemRecord record, @Param("example") DmsNonDrugItemRecordExample example);

    int updateByExample(@Param("record") DmsNonDrugItemRecord record, @Param("example") DmsNonDrugItemRecordExample example);

    int updateByPrimaryKeySelective(DmsNonDrugItemRecord record);

    int updateByPrimaryKey(DmsNonDrugItemRecord record);
}