package com.neu.his.cloud.service.sms.service;

import com.neu.his.cloud.service.sms.dto.dms.DmsNonDrugResult;
import com.neu.his.cloud.service.sms.dto.sms.*;

import com.neu.his.cloud.service.sms.dto.sms.SmsSetmealResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsSetmealRuleResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsSetmealSkdRuleParam;

import java.util.Date;
import java.util.List;

public interface SmsSetmealService {


    /**
     * 查询所有医疗套餐
     * @param dmsNonDrugResult
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<SmsSetmealResult> selectSetmeal(DmsNonDrugResult dmsNonDrugResult, Integer pageSize, Integer pageNum);

    /**
     * 描述：根据deptId查找规则（不包括规则项）
     */
    List<SmsSetmealRuleResult> selectRuleByDept(Long deptId);

    /**
     * 描述：删除排班规则
     */
    int deleteSetmealRule(List<Long> ids);

    /**
     * 描述：查询排班记录
     */
    List<SmsSetMealSkdResult> listSetmealSkd(SmsSetmealSkdParam smsSetmealSkdParam);

    /**
     * 新增医疗套餐
     * @param param
     * @return
     */
    int createSetmealRule(SmsSetmealSkdRuleParam param);

    /**
     * 生成排班SKD
     * @param ruleIds
     * @param startDate
     * @param endDate
     * @return
     */
    int generateSetMealSkd(List<Long> ruleIds, Date startDate, Date endDate);

    /**
     * 根据排班规则ID查询排班详情
     * @param ruleId
     * @return
     */
    SmsSetmealRuleResult getSetmealRuleDetail(Long ruleId);

    /**
     * 更新排班规则
     * @param id
     * @param param
     * @return
     */
    int updateSetmealRule(Long id, SmsSetmealSkdRuleParam param);
}
