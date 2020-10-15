package com.neu.his.cloud.service.dms.mapper;

import com.neu.his.cloud.service.dms.model.WxResults;
import com.neu.his.cloud.service.dms.model.WxResultsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WxResultsMapper {
    int countByExample(WxResultsExample example);

    int deleteByExample(WxResultsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(WxResults record);

    int insertSelective(WxResults record);

    List<WxResults> selectByExampleWithBLOBs(WxResultsExample example);

    List<WxResults> selectByExample(WxResultsExample example);

    WxResults selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") WxResults record, @Param("example") WxResultsExample example);

    int updateByExampleWithBLOBs(@Param("record") WxResults record, @Param("example") WxResultsExample example);

    int updateByExample(@Param("record") WxResults record, @Param("example") WxResultsExample example);

    int updateByPrimaryKeySelective(WxResults record);

    int updateByPrimaryKeyWithBLOBs(WxResults record);

    int updateByPrimaryKey(WxResults record);
}