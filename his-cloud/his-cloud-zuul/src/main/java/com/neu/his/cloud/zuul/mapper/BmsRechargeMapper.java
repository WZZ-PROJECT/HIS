package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.BmsRecharge;
import com.neu.his.cloud.zuul.model.BmsRechargeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BmsRechargeMapper {
    int countByExample(BmsRechargeExample example);

    int deleteByExample(BmsRechargeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BmsRecharge record);

    int insertSelective(BmsRecharge record);

    List<BmsRecharge> selectByExampleWithBLOBs(BmsRechargeExample example);

    List<BmsRecharge> selectByExample(BmsRechargeExample example);

    BmsRecharge selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BmsRecharge record, @Param("example") BmsRechargeExample example);

    int updateByExampleWithBLOBs(@Param("record") BmsRecharge record, @Param("example") BmsRechargeExample example);

    int updateByExample(@Param("record") BmsRecharge record, @Param("example") BmsRechargeExample example);

    int updateByPrimaryKeySelective(BmsRecharge record);

    int updateByPrimaryKeyWithBLOBs(BmsRecharge record);

    int updateByPrimaryKey(BmsRecharge record);
}