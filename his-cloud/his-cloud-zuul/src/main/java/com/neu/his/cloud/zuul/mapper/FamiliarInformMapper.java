package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.FamiliarInform;
import com.neu.his.cloud.zuul.model.FamiliarInformExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FamiliarInformMapper {
    int countByExample(FamiliarInformExample example);

    int deleteByExample(FamiliarInformExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FamiliarInform record);

    int insertSelective(FamiliarInform record);

    List<FamiliarInform> selectByExample(FamiliarInformExample example);

    FamiliarInform selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FamiliarInform record, @Param("example") FamiliarInformExample example);

    int updateByExample(@Param("record") FamiliarInform record, @Param("example") FamiliarInformExample example);

    int updateByPrimaryKeySelective(FamiliarInform record);

    int updateByPrimaryKey(FamiliarInform record);
}