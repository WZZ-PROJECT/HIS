package com.neu.his.cloud.service.sms.service.impl;

import com.neu.his.cloud.service.sms.dto.sms.SmsSignatureParam;
import com.neu.his.cloud.service.sms.dto.sms.SmsSignatureResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsSignatureTypeResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsWorkloadResult;
import com.neu.his.cloud.service.sms.mapper.*;
import com.neu.his.cloud.service.sms.model.*;
import com.neu.his.cloud.service.sms.service.SmsSignatureService;
import com.neu.his.cloud.service.sms.service.SmsWorkloadService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 签章
 */
@Service
public class SmsSignatureServiceImpl implements SmsSignatureService {


    @Resource
    private SmsSignatureMapper mapper;

    @Resource
    private SmsSignatureTypeMapper typeMapper;
    /**
     * 新增签章
     *
     * @param signatureParam
     * @return
     */
    @Override
    public int create(SmsSignatureParam signatureParam) {

        SmsSignature model = new SmsSignature();
        //禁用
        model.setState(2);
        BeanUtils.copyProperties(signatureParam, model);
        return mapper.insert(model);

    }

    /**
     * 删除签章
     *
     * @param ids
     * @return
     */
    @Override
    public int delete(List<Long> ids) {
        SmsSignatureExample example = new SmsSignatureExample();
        example.createCriteria().andIdIn(ids);
        return mapper.deleteByExample(example);
    }

    /**
     * 更新签章，启用禁用操作
     *
     * @param id
     * @param signatureParam
     * @return
     */
    @Override
    public int update(Long id, SmsSignatureParam signatureParam) {
        SmsSignature model = new SmsSignature();
        BeanUtils.copyProperties(signatureParam, model);
        model.setId(id);
        return mapper.updateByPrimaryKeySelective(model);
    }

    /**
     * 查询签章（分页）
     *
     * @param signatureParam
     * @return
     */
    @Override
    public List<SmsSignatureResult> select(SmsSignatureParam signatureParam) {

        SmsSignatureExample example = new SmsSignatureExample();

        //签章编码
        if(!StringUtils.isEmpty(signatureParam.getCode())){
            example.createCriteria().andCodeEqualTo(signatureParam.getCode());
        }

        //是否按编码、名称、分类、类型查询
        if(!StringUtils.isEmpty(signatureParam.getName())){
            example.createCriteria().andNameLike("%" + signatureParam.getName() + "%");
        }

        if(signatureParam.getType() != null){
            example.createCriteria().andTypeEqualTo(signatureParam.getType());
        }

        //返回数据包装成Result
        example.setOrderByClause("id desc");
        List<SmsSignature> smsSignatures = mapper.selectByExample(example);
        List<SmsSignatureResult> result = new ArrayList<>();
        smsSignatures.forEach(smsSignature -> {
            SmsSignatureType smsSignatureType = typeMapper.selectByPrimaryKey((long) smsSignature.getType());
            SmsSignatureResult smsSignatureResult = new SmsSignatureResult();
            BeanUtils.copyProperties(smsSignature,smsSignatureResult);
            smsSignatureResult.setTypeName(smsSignatureType.getName());
            result.add(smsSignatureResult);
        });
        return result;
    }

    /**
     * 查询正在应用的签章
     *
     * @return
     */
    @Override
    public SmsSignatureResult selectAll() {
        SmsSignatureExample example = new SmsSignatureExample();
        example.createCriteria().andStateEqualTo(1);
        List<SmsSignature> smsSignatures = mapper.selectByExample(example);
        SmsSignatureType smsSignatureType = typeMapper.selectByPrimaryKey((long) smsSignatures.get(0).getType());
        SmsSignatureResult result = new SmsSignatureResult();
        if(!CollectionUtils.isEmpty(smsSignatures)){
            BeanUtils.copyProperties(smsSignatures.get(0),result);
            result.setTypeName(smsSignatureType.getName());
            return result;
        }
        return  result;
    }

    /**
     * 查询正在应用的签章类型
     *
     * @return
     */
    @Override
    public List<SmsSignatureTypeResult> selectType() {
        SmsSignatureTypeExample typeExample = new SmsSignatureTypeExample();
        typeExample.createCriteria().andStatusEqualTo(1);
        List<SmsSignatureType> smsSignatureTypes = typeMapper.selectByExample(typeExample);
        List<SmsSignatureTypeResult> list = new ArrayList<>();
        smsSignatureTypes.forEach(smsSignatureType -> {
            SmsSignatureTypeResult result = new SmsSignatureTypeResult();
            BeanUtils.copyProperties(smsSignatureType,result);
            list.add(result);
        });
        return list;
    }
}
