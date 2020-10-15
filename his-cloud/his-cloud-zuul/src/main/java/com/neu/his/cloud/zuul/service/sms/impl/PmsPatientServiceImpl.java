package com.neu.his.cloud.zuul.service.sms.impl;

import com.neu.his.cloud.zuul.mapper.PmsPatientMapper;
import com.neu.his.cloud.zuul.model.PmsPatient;
import com.neu.his.cloud.zuul.model.PmsPatientExample;
import com.neu.his.cloud.zuul.service.sms.PmsPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PmsPatientServiceImpl implements PmsPatientService {

    @Resource
    PmsPatientMapper mapper;

    @Override
    public Boolean isBandtoWechat(String val) {
        PmsPatientExample example = new PmsPatientExample();
        example.createCriteria().andOpenIdEqualTo(val);
        List<PmsPatient> pmsPatients = mapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(pmsPatients)) {
          return true;
        }
        return false;
    }
}
