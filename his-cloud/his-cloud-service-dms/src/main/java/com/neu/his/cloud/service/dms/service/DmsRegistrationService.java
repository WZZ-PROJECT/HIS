package com.neu.his.cloud.service.dms.service;

import com.neu.his.cloud.service.dms.common.CommonResult;
import com.neu.his.cloud.service.dms.dto.app.AppRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.DmsRegHistoryResult;
import com.neu.his.cloud.service.dms.dto.dms.WXDmsRegistrationParam;
import com.neu.his.cloud.service.dms.dto.dms.WxProgramResultsParam;
import com.neu.his.cloud.service.dms.model.BmsAccount;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 挂号
 */
public interface DmsRegistrationService {

    CommonResult amountSufficient(Long registrationId,List<BigDecimal> amount);

    /**
     * 描述：挂号
     */
    int createRegistration(WXDmsRegistrationParam wxDmsRegistrationParam);
    /**
     * 描述：根据身份证号查询历史挂号记录
     */
    List<DmsRegHistoryResult> listRegHistory(String identificationNo);

    /**
     * 描述:app挂号
     * 1.先根据skd_id判断remain是否>0
     * 2.如果大于0，绑定医生（skd_id、bind_status=1）,并修改sms_skd中的排班限额（-1）
     * 3.向dms_registration插入信息
     * 4.向bms_bills_record中插入账单记录
     */
    int appRegistration(AppRegistrationParam appRegistrationParam);

    /**
     * 描述：时间间隔
     */
    List<String> TimeDifference(String ruletime,Long skdId,int noon);
    /**
     * 描述：挂号微信支付
     */
    Map<String, String> WxPay(WXDmsRegistrationParam dmsRegistrationParam);

    /**
     * 描述：封装微信支付的结果
     */
    int WxPayResult(Map<String, String> stringStringMap,Long patientid);
    /**
     * 描述：查看该病人是不是有账号
     */
    BmsAccount SelectAccountByCardId(WXDmsRegistrationParam dmsRegistrationParam);
    /**
     * 描述：该病人没有账号则新建一个账号,有账号直接使用以前的账号
     */
    int insertBmsAccount(WXDmsRegistrationParam dmsRegistrationParam);

    /**
     * 描述：充值
     */
    int recharge(WXDmsRegistrationParam wxDmsRegistrationParam);
    /**
     * 描述：退款
     */
    int rollback(WXDmsRegistrationParam wxDmsRegistrationParam);

    /**
     * 描述：微信小程序结果封装
     */
    int WxProgramResults(WxProgramResultsParam wxProgramResultsParam);

    WXDmsRegistrationParam WxProgramCompletion(WXDmsRegistrationParam wXDmsRegistrationParam);

    int amountSufficient(Long patientId,BigDecimal acount);

    int subBmsAccount(Long patientId,BigDecimal acount);

    /**
     * 描述：微信小程序生成病人账号
     */
    int WXXinsertBmsAccount(WXDmsRegistrationParam dmsRegistrationParam);

}
