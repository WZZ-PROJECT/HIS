package com.neu.his.cloud.service.dms.service;

import com.neu.his.cloud.service.dms.dto.dms.*;
import com.neu.his.cloud.service.dms.model.SmsStaff;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface DmsAppService {

    /**
     * 描述：查询该病人历史就诊医生信息
     */
    List<SmsStaffRecord> listHistoryDoctor(@RequestParam("patientId")  Long patientId);

    /**
     * 描述：查询医生信息以及医生排班信息
     */
    DmsStaffResult listDoctor(@RequestParam("staffId")  Long staffId,@RequestParam Long patientId);

    /**
     * @since 根据医生id查询医生详细信息
     * @param id
     * @return DmsStaffResult
     */
    DmsStaffResult selectStaffById(Long id,Long patientId);

    /**
     * @since 查询医生挂号所需费用
     * @param dmsDoctorBillsParam
     * @return
     */
    DmsDoctorBillsResult listRegistrationBill(DmsDoctorBillsParam dmsDoctorBillsParam);

    /**
     * 根据医生的名字模糊查询
     * @param name
     * @return
     */
    List<DmsStaffResult> selectStaffByName(String name);

    /**
     * 添加关注
     * @param dmsDoctorBillsParam
     * @return
     */
    int attentionStaff(DmsDoctorBillsParam dmsDoctorBillsParam);

    /**
     * 描述：查询科室Id查询可预约挂号医生信息
     */
    List<DmsNowDoctorRegistrationResult> listNowDoctorRegistration(@RequestParam("deptId")  Long deptId,
                                                                   @RequestParam("thedate") String thedate);

    /**
     *  根据openID获取当前患者已预约的信息列表
     */
    List<BookingInformationResult> listPatientConvention(@RequestParam("openID")  String openID);

    /**
     *  根据预约id，取消预约接口，更改预约信息状态
     */
    List<BookingInformationResult> listPatientConventionUpdate(@RequestParam("Id") Long Id);

}
