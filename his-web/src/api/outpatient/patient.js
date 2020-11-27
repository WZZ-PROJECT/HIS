import request from '@/utils/request'
export function getPatientList(data) {
  return request({
    url: '/diagnosisPatient/refreshPatient',
    method: 'post',
    params: {
      staffId:data
    }
  })
}

export function queryPeople(data) {
  return request({
    url: '/diagnosisPatient/queryPeople',
    method: 'post',
    data
  })
}

export function bindPatient(registrationId,staffId) {
  return request({
    url: '/diagnosisPatient/bindPatient',
    method: 'get',
    params: {
      registrationId:registrationId,
      staffId:staffId
    }
  })
}

export function startDiagnosis(registrationId) {
  return request({
    url: '/diagnosisPatient/startDiagnosis',
    method: 'get',
    params: {
      registrationId:registrationId,
    }
  })
}
//读卡
export function selectPatientByIdNo(data) {
  return request({
    url: '/diagnosisPatient/selectPatientByIdNo',
    method: 'post',
    params: {
      identificationNo:data
    }
  })
}

//查询病人信息用于查看病例信息
export function selectPeopleByRegistrationId(data) {
  return request({
    url: '/diagnosisPatient/selectPeopleByRegistrationId',
    method: 'post',
    params: {
      registrationId:data
    }
  })
}
