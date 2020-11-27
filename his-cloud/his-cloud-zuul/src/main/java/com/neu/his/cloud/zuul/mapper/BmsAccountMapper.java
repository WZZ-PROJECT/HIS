package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.BmsAccount;
import com.neu.his.cloud.zuul.model.BmsAccountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BmsAccountMapper {
    int countByExample(BmsAccountExample example);

    int deleteByExample(BmsAccountExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BmsAccount record);

    int insertSelective(BmsAccount record);

    List<BmsAccount> selectByExample(BmsAccountExample example);

    BmsAccount selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BmsAccount record, @Param("example") BmsAccountExample example);

    int updateByExample(@Param("record") BmsAccount record, @Param("example") BmsAccountExample example);

    int updateByPrimaryKeySelective(BmsAccount record);

    int updateByPrimaryKey(BmsAccount record);
}