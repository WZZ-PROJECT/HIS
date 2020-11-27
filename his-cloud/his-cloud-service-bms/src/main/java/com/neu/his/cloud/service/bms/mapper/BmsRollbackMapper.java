package com.neu.his.cloud.service.bms.mapper;

import com.neu.his.cloud.service.bms.model.BmsRollback;
import com.neu.his.cloud.service.bms.model.BmsRollbackExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BmsRollbackMapper {
    int countByExample(BmsRollbackExample example);

    int deleteByExample(BmsRollbackExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BmsRollback record);

    int insertSelective(BmsRollback record);

    List<BmsRollback> selectByExample(BmsRollbackExample example);

    BmsRollback selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BmsRollback record, @Param("example") BmsRollbackExample example);

    int updateByExample(@Param("record") BmsRollback record, @Param("example") BmsRollbackExample example);

    int updateByPrimaryKeySelective(BmsRollback record);

    int updateByPrimaryKey(BmsRollback record);
}