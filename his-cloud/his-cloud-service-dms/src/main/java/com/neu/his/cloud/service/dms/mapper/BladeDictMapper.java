package com.neu.his.cloud.service.dms.mapper;

import com.neu.his.cloud.service.dms.model.BladeDict;
import com.neu.his.cloud.service.dms.model.BladeDictExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BladeDictMapper {
    int countByExample(BladeDictExample example);

    int deleteByExample(BladeDictExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BladeDict record);

    int insertSelective(BladeDict record);

    List<BladeDict> selectByExample(BladeDictExample example);

    BladeDict selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BladeDict record, @Param("example") BladeDictExample example);

    int updateByExample(@Param("record") BladeDict record, @Param("example") BladeDictExample example);

    int updateByPrimaryKeySelective(BladeDict record);

    int updateByPrimaryKey(BladeDict record);
}