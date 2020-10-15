package com.neu.his.cloud.service.sms.service.impl;


import com.neu.his.cloud.service.sms.dto.dms.DmsNonDrugResult;
import com.neu.his.cloud.service.sms.dto.sms.*;
import com.neu.his.cloud.service.sms.mapper.*;
import com.neu.his.cloud.service.sms.model.*;
import com.neu.his.cloud.service.sms.dto.sms.SmsSetmealResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsSetmealRuleResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsSetmealSkdRuleItemResult;
import com.neu.his.cloud.service.sms.dto.sms.SmsSetmealSkdRuleParam;
import com.neu.his.cloud.service.sms.mapper.*;
import com.neu.his.cloud.service.sms.model.*;
import com.neu.his.cloud.service.sms.model.DmsNonDrugExample.Criteria;
import com.neu.his.cloud.service.sms.service.SmsSetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SmsSetmealServiceImpl implements SmsSetmealService {
    @Resource
    private DmsNonDrugMapper dmsNonDrugMapper;
    @Resource
    private SmsDeptMapper smsDeptMapper;

    @Resource
    private SmsSetmealSkdRuleMapper smsSetmealSkdRuleMapper;

    @Resource
    private SmsSetmealSkdRuleItemMapper smsSetmealSkdRuleItemMapper;

    @Resource
    private SmsStaffMapper smsStaffMapper;

    @Resource
    private SmsSkdMapper smsSkdMapper;

    @Resource
    private SmsSetmealSkdMapper smsSetmealSkdMapper;

    public List<SmsSetmealResult> selectSetmeal(DmsNonDrugResult dmsNonDrugResult, Integer pageSize, Integer pageNum) {
        DmsNonDrugExample example = new DmsNonDrugExample();
        Criteria criteria = example.createCriteria();
        List<SmsSetmealResult> list = new ArrayList();
        criteria.andIsSetmealEqualTo(1);
        if (dmsNonDrugResult.getDeptId() != null) {
            criteria.andDeptIdEqualTo(dmsNonDrugResult.getDeptId());
        }

        List<DmsNonDrug> results = this.dmsNonDrugMapper.selectByExample(example);
        results.forEach((dmsNonDrug) -> {
            SmsSetmealResult smsSetmealResult = new SmsSetmealResult();
            SmsDept dept = this.smsDeptMapper.selectByPrimaryKey(dmsNonDrug.getDeptId());
            smsSetmealResult.setDept(dept);
            BeanUtils.copyProperties(dmsNonDrug, smsSetmealResult);
            list.add(smsSetmealResult);
        });
        return list;
    }

    /**
     * 新增医疗套餐
     *
     * @param param
     * @return
     */
    @Override
    public int createSetmealRule(SmsSetmealSkdRuleParam param) {

        SmsSetmealSkdRule smsSetmealSkdRule = new SmsSetmealSkdRule();
        BeanUtils.copyProperties(param,smsSetmealSkdRule);

        //为排班规则添加状态
        smsSetmealSkdRule.setStatus(1);
        smsSetmealSkdRule.setOperateTime(new Date());

        //添加排班规则
        int skdRule = smsSetmealSkdRuleMapper.insert(smsSetmealSkdRule);

        if (skdRule > 0) {

            //根据操作员id和操作时间查询排版规则的id
            SmsSetmealSkdRuleExample example = new SmsSetmealSkdRuleExample();
            example.createCriteria().andOperatorIdEqualTo(param.getOperatorId());
            example.setOrderByClause("operate_time desc");
            List<SmsSetmealSkdRule> list = smsSetmealSkdRuleMapper.selectByExample(example);
            Long id = list.get(0).getId();

            //遍历排班项并全部插入
            AtomicInteger sumOfRuleItem = new AtomicInteger();
            param.getSmsSetmealSkdRuleItemParams().stream().filter(ruleItemParam -> list.size()>0).forEach(ruleItemParam -> {
                SmsSetmealSkdRuleItem skdRuleItem = new SmsSetmealSkdRuleItem();
                BeanUtils.copyProperties(ruleItemParam,skdRuleItem);
                skdRuleItem.setStatus(1);
                skdRuleItem.setSkRuleId(id);
                sumOfRuleItem.getAndAdd(smsSetmealSkdRuleItemMapper.insert(skdRuleItem));
            });

            return skdRule + sumOfRuleItem.get();
        }

        return 0;
    }

    /**
     * 生成排班SKD
     *
     * @param ruleIds
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public int generateSetMealSkd(List<Long> ruleIds, Date startDate, Date endDate) {
        if(endDate.compareTo(startDate) >= 0){
            //设置排版规则id与科室id对应
            SmsSetmealSkdRuleExample example = new SmsSetmealSkdRuleExample();
            example.createCriteria().andIdIn(ruleIds);
            List<SmsSetmealSkdRule> smsSkdRuleList = smsSetmealSkdRuleMapper.selectByExample(example);
            Map<Long, Long> ruleIdToDeptIdMap = new Hashtable<>();
            smsSkdRuleList.stream().filter(skdRule -> smsSkdRuleList.size()>0).forEach(skdRule ->{
                ruleIdToDeptIdMap.put(skdRule.getId(),skdRule.getDeptId());
            });

            //调用SmsSkdRuleItemDao查询所有ruleIds的排班项，并合并为一个Map
            SmsSetmealSkdRuleItemExample ruleItemExample = new SmsSetmealSkdRuleItemExample();
            ruleItemExample.createCriteria().andSkRuleIdIn(ruleIds).andStatusEqualTo(1);
            List<SmsSetmealSkdRuleItem> ruleItemList = smsSetmealSkdRuleItemMapper.selectByExample(ruleItemExample);
            Map<Long, List<SmsSetmealSkdRuleItem>> deptIdToItemListMap = new Hashtable<>();
            //生成一个nonDrugIdList
            List<Long> nonDrugIdList = new ArrayList<>();
            for (SmsSetmealSkdRuleItem ruleItem : ruleItemList) {
                List<SmsSetmealSkdRuleItem> currentItemList;
                if (deptIdToItemListMap.containsKey(ruleIdToDeptIdMap.get(ruleItem.getSkRuleId()))) {
                    currentItemList = deptIdToItemListMap.get(ruleItem.getSkRuleId());
                    //判断该staff是否已经存在
                    for(SmsSetmealSkdRuleItem item : currentItemList){
                        if(item.getNonDrugId() == item.getNonDrugId()){
                            return 0;
                        }
                    }
                } else {
                    currentItemList = new ArrayList<>();
                }
                currentItemList.add(ruleItem);
                nonDrugIdList.add(ruleItem.getNonDrugId());
                deptIdToItemListMap.put(ruleIdToDeptIdMap.get(ruleItem.getSkRuleId()),currentItemList);
            }

            //没有重复，则遍历排班记录表，根据Date\nonDrugId删除排班记录
            SmsSetmealSkdExample smSetmealSkdExample = new SmsSetmealSkdExample();
            smSetmealSkdExample.createCriteria().andDateBetween(startDate,endDate).andNonDrugIdIn(nonDrugIdList);
            int deleteSetmealSkdCount = smsSetmealSkdMapper.deleteByExample(smSetmealSkdExample);

            //遍历起止时间,判断每一天为周几，取出所有itemList中的daysOfWeek，如果为周i，则取出第2(i-1)位和第2(i-1)+1位。如果为0，则continue，如果为1，则插入skd
            int insertSkdCount = 0;
            List<Date> listDate = getDatesBetweenTwoDate(startDate, endDate);
            for(Date today : listDate){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                int week = calendar.get(Calendar.DAY_OF_WEEK) - 2;  //此时week，周日为-1
                if(week == -1){
                    week = 6;       //将周日设置为6
                }
                //遍历map
                for(Long currentDeptId : deptIdToItemListMap.keySet()){
                    List<SmsSetmealSkdRuleItem> currentItemList = deptIdToItemListMap.get(currentDeptId);
                    //遍历同一科室id下Item
                    for(SmsSetmealSkdRuleItem ruleItem : currentItemList){
                        String noon = ruleItem.getDaysOfWeek();
                        char morning = noon.charAt(2*week);
                        char afternoon = noon.charAt(2*week + 1);

                        if(morning == '1'){
                            //添加上午值班信息
                            SmsSetmealSkd skd = new SmsSetmealSkd();
                            skd.setDate(today);
                            skd.setStatus(1);
                            skd.setRemain(ruleItem.getSkLimit());
                            skd.setNoon(0);
                            skd.setNonDrugId(ruleItem.getNonDrugId());
                            skd.setDeptId(currentDeptId);
                            skd.setSkLimit(ruleItem.getSkLimit());
                            insertSkdCount += smsSetmealSkdMapper.insert(skd);
                        }
                        if(afternoon == '1'){
                            //添加下午值班信息
                            SmsSetmealSkd skd = new SmsSetmealSkd();
                            skd.setDate(today);
                            skd.setStatus(1);
                            skd.setRemain(ruleItem.getSkLimit());
                            skd.setNoon(1);
                            skd.setNonDrugId(ruleItem.getNonDrugId());
                            skd.setDeptId(currentDeptId);
                            skd.setSkLimit(ruleItem.getSkLimit());
                            insertSkdCount += smsSetmealSkdMapper.insert(skd);
                        }
                    }
                }
            }
            return insertSkdCount;
        }
        return 0;
    }

    /**
     * 根据排班规则ID查询排班详情
     *
     * @param ruleId
     * @return
     */
    @Override
    public SmsSetmealRuleResult getSetmealRuleDetail(Long ruleId) {
        SmsSetmealSkdRule rule = smsSetmealSkdRuleMapper.selectByPrimaryKey(ruleId);
        if (!StringUtils.isEmpty(rule)) {
            SmsSetmealRuleResult ruleResult = new SmsSetmealRuleResult();
            BeanUtils.copyProperties(rule,ruleResult);

            //查询科室名称
            String deptName = smsDeptMapper.selectByPrimaryKey(ruleResult.getId()).getName();
            if (!StringUtils.isEmpty(deptName)) {
                ruleResult.setDeptName(deptName);

                //设置排班项
                List<SmsSetmealSkdRuleItemResult> result = new ArrayList<>();
                SmsSetmealSkdRuleItemExample example = new SmsSetmealSkdRuleItemExample();
                example.createCriteria().andSkRuleIdEqualTo(ruleResult.getId());

                List<SmsSetmealSkdRuleItem> itemslist = smsSetmealSkdRuleItemMapper.selectByExample(example);
                for(SmsSetmealSkdRuleItem item : itemslist){
                    SmsSetmealSkdRuleItemResult skdRuleItem = new SmsSetmealSkdRuleItemResult();
                    BeanUtils.copyProperties(item,skdRuleItem);
                    //获取并设置医生name
                    DmsNonDrug dmsNonDrug = dmsNonDrugMapper.selectByPrimaryKey(skdRuleItem.getNonDrugId());
                    skdRuleItem.setNonDrugName(dmsNonDrug.getName());
                    result.add(skdRuleItem);

                }

                ruleResult.setSmsSetmealSkdRuleItemResult(result);
                return ruleResult;
            }
        }
        return null;
    }

    /**
     * 更新排班规则
     *
     * @param id
     * @param param
     * @return
     */
    @Override
    public int updateSetmealRule(Long id, SmsSetmealSkdRuleParam param) {
        List<Long> deleteList = new ArrayList<>();
        deleteList.add(id);

        //根据id删除
        int deleteCount = deleteSetmealRule(deleteList);

        //插入新排版规则
        int insertCount = createSetmealRule(param);
        return deleteCount + insertCount;
    }

    //根据起止日期生成日期List
    public List<Date> getDatesBetweenTwoDate(Date start, Date end){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        List<Date> dateList = new ArrayList<>();
        while(calendar.getTime().compareTo(end) <= 0){
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return dateList;
    }


    @Override
    public List<SmsSetmealRuleResult> selectRuleByDept(Long deptId) {
        //获取科室名
        SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(deptId);
        if(StringUtils.isEmpty(smsDept)){
            return null;
        }

        //查询一个科室的所有排班规则(status为1)
        SmsSetmealSkdRuleExample smsSetmealSkdRuleExample = new SmsSetmealSkdRuleExample();
        smsSetmealSkdRuleExample.createCriteria().andDeptIdEqualTo(deptId).andStatusEqualTo(1);
        List<SmsSetmealSkdRule> SmsSetmealSkdRuleList = smsSetmealSkdRuleMapper.selectByExample(smsSetmealSkdRuleExample);
        if(CollectionUtils.isEmpty(SmsSetmealSkdRuleList)){  //如果为空，则不需要接下来的数据封装
            return null;
        }
        List<SmsSetmealRuleResult> SmsSetmealRuleResultList = new ArrayList<>();
        for(SmsSetmealSkdRule smsSetmealSkdRule : SmsSetmealSkdRuleList){
            SmsSetmealRuleResult SmsSetmealRuleResult = new SmsSetmealRuleResult();
            BeanUtils.copyProperties(smsSetmealSkdRule,SmsSetmealRuleResult);
            //设置科室名
            SmsSetmealRuleResult.setDeptName(smsDept.getName());
            //设置操作姓名
            SmsStaff operator=smsStaffMapper.selectByPrimaryKey(smsSetmealSkdRule.getOperatorId());
            if(null!=operator){
                SmsSetmealRuleResult.setOperatorName(operator.getUsername());
            }
            //设置排班项（暂不用）
            SmsSetmealRuleResultList.add(SmsSetmealRuleResult);
        }
        return SmsSetmealRuleResultList;
    }

    @Override
    public int deleteSetmealRule(List<Long> ids) {

        //删除Rule
        SmsSetmealSkdRuleExample smsSetmealSkdRuleExample=new SmsSetmealSkdRuleExample();
        smsSetmealSkdRuleExample.createCriteria().andIdIn(ids);
        int SmsSetmealSkdRule=smsSetmealSkdRuleMapper.deleteByExample(smsSetmealSkdRuleExample);

        //删除RuleItem
        SmsSetmealSkdRuleItemExample smsSetmealSkdRuleItemExample=new SmsSetmealSkdRuleItemExample();
        smsSetmealSkdRuleItemExample.createCriteria().andIdIn(ids);
        int smsSetmealSkdRuleItemCount=smsSetmealSkdRuleItemMapper.deleteByExample(smsSetmealSkdRuleItemExample);

        return SmsSetmealSkdRule + smsSetmealSkdRuleItemCount;
    }

    @Override
    public List<SmsSetMealSkdResult> listSetmealSkd(SmsSetmealSkdParam smsSetmealSkdParam) {

        SmsSetmealSkdExample smsSetmealSkdExample = new SmsSetmealSkdExample();
        SmsSetmealSkdExample.Criteria criteria = smsSetmealSkdExample.createCriteria();
        criteria.andStatusEqualTo(1);

        Date parse=null;
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            parse= simpleDateFormat.parse("0000-00-00 00:00:00");
        }catch (Exception e){
            e.printStackTrace();
        }

        if(smsSetmealSkdParam.getNonDrugId() != null){
            criteria.andNonDrugIdEqualTo(smsSetmealSkdParam.getNonDrugId());
        }
        if(smsSetmealSkdParam.getStartDate() != null &&smsSetmealSkdParam.getStartDate().compareTo(parse)!=0){
            criteria.andDateGreaterThanOrEqualTo(smsSetmealSkdParam.getStartDate());
        }
        if(smsSetmealSkdParam.getEndDate() != null){
            criteria.andDateLessThanOrEqualTo(smsSetmealSkdParam.getEndDate());
        }
        if(smsSetmealSkdParam.getNoon() != null){
            criteria.andNoonEqualTo(smsSetmealSkdParam.getNoon());
        }
        if(smsSetmealSkdParam.getDeptId() != null){
            criteria.andDeptIdEqualTo(smsSetmealSkdParam.getDeptId());
        }

        smsSetmealSkdExample.setOrderByClause("date desc");

        List<SmsSetmealSkd> smsSetmealSkdList = smsSetmealSkdMapper.selectByExample(smsSetmealSkdExample);
        List<SmsSetMealSkdResult> smsSetMealSkdResultList = new ArrayList<>();
        for (SmsSetmealSkd smsSetmealSkd : smsSetmealSkdList) {
            SmsSetMealSkdResult smsSetMealSkdResult = new SmsSetMealSkdResult();
            BeanUtils.copyProperties(smsSetmealSkd,smsSetMealSkdResult);

            //封装部门名称
            SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(smsSetmealSkd.getDeptId());
            smsSetMealSkdResult.setDeptName(smsDept.getName());

            //封装套餐姓名
            DmsNonDrug dmsNonDrug = dmsNonDrugMapper.selectByPrimaryKey(smsSetmealSkd.getNonDrugId());
            smsSetMealSkdResult.setNonDrugName(dmsNonDrug.getName());

            smsSetMealSkdResultList.add(smsSetMealSkdResult);
        }
        if (!StringUtils.isEmpty(smsSetmealSkdParam.getNonDrugName())) {
            List<SmsSetMealSkdResult> list = new ArrayList<>();
            smsSetMealSkdResultList.stream().filter(smsSetMealSkdResult -> smsSetMealSkdResult.getNonDrugName().contains(smsSetmealSkdParam.getNonDrugName())).forEach(smsSetMealSkdResult -> {
                list.add(smsSetMealSkdResult);
            });
            return list;
        }
        return smsSetMealSkdResultList;
    }
}
