package com.neu.his.cloud.service.dms.service;

import com.neu.his.cloud.service.dms.dto.dms.AddInformParam;
import com.neu.his.cloud.service.dms.dto.dms.DmsCaseHistoryParam;
import com.neu.his.cloud.service.dms.dto.dms.DmsCaseHistoryResult;
import com.neu.his.cloud.service.dms.model.FamiliarInform;

import java.util.List;


/**
 * 历史病例
 */
public interface DmsCaseHistoryService {
    /**
     * 描述：插入初诊信息
     */
    int insertPriliminaryDise(DmsCaseHistoryParam dmsCaseHistoryParam);
    /**
     * 描述：根据门诊号查询历史病历
     */
    DmsCaseHistoryResult selectCaseHistoryByReg(Long registrationId, Integer status);

    /**
     * 描述：根据门诊号查询历史病历
     */
    DmsCaseHistoryResult selectCaseHistory(Long registrationId, Integer status);
    /**
     * 描述：提交门诊确诊信息
     */
    int submitDefiniteDise(DmsCaseHistoryParam dmsCaseHistoryParam);
    /**
     * 描述：诊毕
     */
    int endDiagnosis(DmsCaseHistoryParam dmsCaseHistoryParam);

    /**
     * 描述：诊中
     */
    Integer selectClinicalCaseHistoryByReg(Long registrationId);

    /**
     * 查询诊必的历史病历
     * @param patientId
     * @return
     */
    DmsCaseHistoryResult queryCaseHistory(long patientId);


    /**
     * 描述：插入知情告知信息
     */
    int insertFamiliarInform(AddInformParam addInformParam);

    /**
     * 描述：删除知情告知信息
     */
    int deleteFamiliarInform(AddInformParam addInformParam);

    /**
     * 描述：修改知情告知信息
     */
    int updateFamiliarInform(AddInformParam addInformParam);

    /**
     * 描述：查询知情告知信息
     */
    List<FamiliarInform> selectFamiliarInform(AddInformParam addInformParam);
}
