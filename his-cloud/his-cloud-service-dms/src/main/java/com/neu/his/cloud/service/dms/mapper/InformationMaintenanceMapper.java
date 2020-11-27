package com.neu.his.cloud.service.dms.mapper;

import com.neu.his.cloud.service.dms.model.InformationMaintenance;
import com.neu.his.cloud.service.dms.model.InformationMaintenanceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InformationMaintenanceMapper {
    int countByExample(InformationMaintenanceExample example);

    int deleteByExample(InformationMaintenanceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(InformationMaintenance record);

    int insertSelective(InformationMaintenance record);

    List<InformationMaintenance> selectByExampleWithBLOBs(InformationMaintenanceExample example);

    List<InformationMaintenance> selectByExample(InformationMaintenanceExample example);

    InformationMaintenance selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") InformationMaintenance record, @Param("example") InformationMaintenanceExample example);

    int updateByExampleWithBLOBs(@Param("record") InformationMaintenance record, @Param("example") InformationMaintenanceExample example);

    int updateByExample(@Param("record") InformationMaintenance record, @Param("example") InformationMaintenanceExample example);

    int updateByPrimaryKeySelective(InformationMaintenance record);

    int updateByPrimaryKeyWithBLOBs(InformationMaintenance record);

    int updateByPrimaryKey(InformationMaintenance record);
}