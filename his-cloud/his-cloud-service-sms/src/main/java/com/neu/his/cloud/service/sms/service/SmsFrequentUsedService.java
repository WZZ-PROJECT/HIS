package com.neu.his.cloud.service.sms.service;

import com.neu.his.cloud.service.sms.dto.sms.AddInformParam;
import com.neu.his.cloud.service.sms.dto.sms.InformParam;
import com.neu.his.cloud.service.sms.dto.sms.SmsFrequentUsedResult;
import com.neu.his.cloud.service.sms.model.FamiliarInformTemplate;

import java.util.List;


/**
 * 常用项
 */
public interface SmsFrequentUsedService {

    /**
     * 描述：新增常用项
     */
    int addFrequent(Long staffId, int addType, Long addId);

    /**
     * 描述：删除常用项
     */
    int deleteFrequent(Long staffId, int deleteType, Long deleteId);

    /**
     * 描述：根据staffId和type查询常用项
     */
    SmsFrequentUsedResult selectFrequent(Long staffId, int selectType);


    /**
     * 描述：添加知情告知
     */
    int addInform(AddInformParam addInformParam);
    /**
     * 描述：删除知情告知
     */
    int deleteInform(Long frequentId);
    /**
     * 描述：修改知情告知
     */
    int updateInform(AddInformParam addInformParam);

    /**
     * 描述：查询知情告知
     */
    List<FamiliarInformTemplate> selectInform(InformParam informParam);

}
