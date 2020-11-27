import request from '@/utils/request'
export function submitPriliminaryDise(data) {
  return request({
    url: '/caseHistory/submitPriliminaryDise',
    method: 'post',

   /* params: {
      chiefComplaint:data.chiefComplaint,//主述
      historyOfPresentIllness:data.historyOfPresentIllness,//现病史
      historyOfTreatment:data.historyOfTreatment,//现治疗情况
      pastHistory:data.pastHistory,//既往史
      allergies:data.allergies,//过敏史
      healthCheckup:data.healthCheckup,//体格检查
      registrationId:data.registrationId,//
      priliminaryDiseStrList:data.priliminaryDiseStrList,
      priliminaryDiseIdList:data.priliminaryDiseIdList,
      startDate:data.startDate,
      name:data.name,
      gender:data.gender,
      ageStr:data.ageStr
    }*/
   data
  })
}


export function submitdefinite(data) {
  return request({
    url: '/caseHistory/submitDefiniteDise',
    method: 'post',
    data
  })
}
//诊毕
export function endDiagnosis(data) {
  return request({
    url: '/caseHistory/endDiagnosis',
    method: 'post',
    params:{
      registrationId:data
    }
  })
}
//根据挂号id查询已结束就诊的历史病历（病历首页显示各种串）
export function selectEndCaseHistoryByReg(data) {
  return request({
    url: '/caseHistory/selectEndCaseHistoryByReg/'+data,
    method: 'get'
  })
}

//根据挂号id查询未结束就诊的历史病历（只含有初诊信息）
export function getnonend(data) {
  return request({
    url: '/caseHistory/selectNotEndCaseHistoryByReg/'+data,
    method: 'get',
    params:{
      registrationId:data
    }
  })
}

//根据挂号id查询未结束就诊的历史病历（只含有确诊信息）
export function endgetnonend(data) {
  return request({
    url: '/caseHistory/selectMiddleCaseHistoryByReg/' + data,
    method: 'get',
    params: {
      registrationId: data
    }
  })
}

export function Clinical(data) {
  return request({
    url: '/caseHistory/selectClinicalCaseHistoryByReg/' + data,
    method: 'get',
    params: {
      registrationId: data
    }
  })
}

// 查看历史病历
export function selectEndCaseHistory(data,val) {
  return request({
    url: '/caseHistory/selectEndCaseHistory/' + data,
    method: 'get',
    params: {
      status:val
    }
  })
}
/*知情告知插入*/
export function insertFamiliarInform(data) {
  return request({
    url: '/caseHistory/insertFamiliarInform/',
    method: 'post',
    data
  })
}
/*知情告知删除*/
export function deleteFamiliarInform(data) {
  return request({
    url: '/caseHistory/deleteFamiliarInform/',
    method: 'post',
    data
  })
}
/*知情告知修改*/
export function updateFamiliarInform(data) {
  return request({
    url: '/caseHistory/updateFamiliarInform/',
    method: 'post',
    data
  })
}
/*知情告知查询*/
export function selectFamiliarInform(data) {
  return request({
    url: '/caseHistory/selectFamiliarInform/',
    method: 'post',
    data
  })
}
