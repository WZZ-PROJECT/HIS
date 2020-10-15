package com.neu.his.cloud.service.sms.service;

import com.neu.his.cloud.service.sms.dto.sms.SmsSignatureParam;
import com.neu.his.cloud.service.sms.dto.sms.SmsSignatureResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsSignatureTypeResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsWorkloadResult;

import java.util.Date;
import java.util.List;

/**
 * 工作量
 */
public interface SmsSignatureService {


    /**
     * 新增签章
     * @param signatureParam
     * @return
     */
    int create(SmsSignatureParam signatureParam);

    /**
     * 删除签章
     * @param ids
     * @return
     */
    int delete(List<Long> ids);

    /**
     * 更新签章
     * @param id
     * @param signatureParam
     * @return
     */
    int update(Long id, SmsSignatureParam signatureParam);

    /**
     * 查询签章（分页）
     * @param signatureParam
     * @return
     */
    List<SmsSignatureResult> select(SmsSignatureParam signatureParam);

    /**
     * 查询正在应用的签章
     * @return
     */
    SmsSignatureResult selectAll();

    /**
     * 查询正在应用的签章类型
     * @return
     */
    List<SmsSignatureTypeResult> selectType();
}
