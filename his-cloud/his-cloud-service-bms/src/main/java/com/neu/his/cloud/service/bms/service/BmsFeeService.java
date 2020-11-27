package com.neu.his.cloud.service.bms.service;

import com.neu.his.cloud.service.bms.dto.bms.*;
import com.neu.his.cloud.service.bms.dto.dms.WXDmsRegistrationParam;
import com.neu.his.cloud.service.bms.model.DmsRegistration;


import java.util.Date;
import java.util.List;

/**
 * 收缴费
 */
public interface BmsFeeService {
    /**
     * 描述：查询当日挂号人
     */
    List<BmsRegistrationPatientResult> listRegisteredPatient(String medicalRecordNo, Date queryDate);
    /**
     * 描述：根据挂号id查询未缴费项目
     */
    List<BmsChargeResult>  listChargeByRegistrationId(Long registrationId);
    /**
     * 描述：收费
     */
    int charge(List<BmsChargeParam> bmsChargeParamList) throws Exception;
    /**
     * 描述：退费查询（根据挂号id查找出所有发票）
     */
    List<BmsRefundChargeResult> listRefundByRegistrationId(Long registrationId);
    /**
     * 描述：非药品、药品退费
     */
    int refundCharge(List<BmsRefundChargeParam> bmsRefundChargeParamList);
    /**
     * 描述：挂号退费
     */
    int refundRegistrationCharge(BmsRefundRegChargeParam bmsRefundRegChargeParam) throws Exception;
    /**
     * 描述：微信付款码支付（检查，检验，开立药物使用的接口）
     */
    int WxPay(BmsChargeParam bmsChargeParam, DmsRegistration dmsRegistration);

    int WxPay(WXDmsRegistrationParam dmsRegistrationParam);

    /**
     * 根据病人id查询所有项目
     * @param bmsParam
     * @return
     */
    List<BmsResult> listCharge(BmsParam bmsParam);

    String queryById(String cardId);
}

