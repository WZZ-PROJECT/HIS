package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.FamiliarInformTemplate;
import com.neu.his.cloud.zuul.model.FamiliarInformTemplateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FamiliarInformTemplateMapper {
    int countByExample(FamiliarInformTemplateExample example);

    int deleteByExample(FamiliarInformTemplateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FamiliarInformTemplate record);

    int insertSelective(FamiliarInformTemplate record);

    List<FamiliarInformTemplate> selectByExample(FamiliarInformTemplateExample example);

    FamiliarInformTemplate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FamiliarInformTemplate record, @Param("example") FamiliarInformTemplateExample example);

    int updateByExample(@Param("record") FamiliarInformTemplate record, @Param("example") FamiliarInformTemplateExample example);

    int updateByPrimaryKeySelective(FamiliarInformTemplate record);

    int updateByPrimaryKey(FamiliarInformTemplate record);
}