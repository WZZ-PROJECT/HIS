package com.neu.his.cloud.zuul.mapper;

import com.neu.his.cloud.zuul.model.BmsRefundBill;
import com.neu.his.cloud.zuul.model.BmsRefundBillExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BmsRefundBillMapper {
    int countByExample(BmsRefundBillExample example);

    int deleteByExample(BmsRefundBillExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BmsRefundBill record);

    int insertSelective(BmsRefundBill record);

    List<BmsRefundBill> selectByExample(BmsRefundBillExample example);

    BmsRefundBill selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BmsRefundBill record, @Param("example") BmsRefundBillExample example);

    int updateByExample(@Param("record") BmsRefundBill record, @Param("example") BmsRefundBillExample example);

    int updateByPrimaryKeySelective(BmsRefundBill record);

    int updateByPrimaryKey(BmsRefundBill record);
}