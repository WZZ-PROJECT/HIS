import request from '@/utils/request'
const baseurl = "http://localhost:8080/"
const url2 ="http://10.25.52.153:8080/"
// 挂号信息列表
export function listRegisteredPatient(data) {
  return request({
    url: '/fee/listRegisteredPatient',
    method: 'get',
    params: data
  })
}
//挂号
export function createRegistration(data) {
  return request({
    url: '/registration/createRegistration',
    method: 'post',
    data
  })
}

//充值
export function recharge(data) {
  return request({
    url: '/registration/recharge',
    method: 'post',
    data
  })
}

//退费
export function rollback(data) {
  return request({
    url: '/registration/rollback',
    method: 'post',
    data
  })
}

//查询上班医生
export function listDocBySkd(data) {
  return request({
    url: '/staff/listDocBySkd',
    method: 'post',
    data
  })
}
//退号
export function refundRegistrationCharge(data) {
  return request({
    url: '/fee/refundRegistrationCharge',
    method: 'post',
    data
  })
}
//根据挂号id返回未缴费列表
export function listChargeByRegistrationId(data) {
  return request({
    url: '/fee/listChargeByRegistrationId',
    method: 'get',
    params:{
      registrationId:data
    }
  })
}

//根据挂号id返回可退费列表
export function listRefundByRegistrationId(data) {
  return request({
    url: '/fee/listRefundByRegistrationId',
    method: 'get',
    params:{
      registrationId:data
    }
  })
}

//收费
export function charge(data) {
  return request({
    url: '/fee/charge',
    method: 'post',
    data
  })
}
//项目退费
export function refundCharge(data) {
  return request({
    url: '/fee/refundCharge',
    method: 'post',
    data
  })
}
//重打
export function reprintInvoice(data) {
  return request({
    url: '/invoice/reprintInvoice',
    method: 'GET',
    params:{
      newInvoiceNo:data.newInvoiceNo,
      oldInvoiceNo:data.oldInvoiceNo
    }
  })
}
//补打
export function supplementPrintInvoice(data) {
  return request({
    url: '/invoice/supplementPrintInvoice?newInvoiceNo='+data.newInvoiceNo+"&registrationId="+data.registrationId,
    method: 'GET',
  })
}
//排班列表
export function listTime(data) {
  return request({
    url: '/registration/TimeDifference?ruletime='+data.ruletime+'&skdId='+data.skdId+'&noon='+data.noon,
    method: 'get',
  })
}

//判断是否有用户
export function isAccount(data) {
  return request({
    url: '/registration/isAccount',
    method: 'post',
    params:{
      identificationNo:data.identificationNo
    }
  })
}

//修改病人信息
export function updateInformation(data) {
  return request({
    url: '/registration/updateInformation',
    method: 'post',
    data
  })
}

// 根据病人身份证号查询历史病人信息
export function listPatientByCardId(data) {
  return request({
    url: '/fee/listPatientByCardId',
    method: 'post',
    data
  })
}

//获得各个方式的退款金额
export function selectRefundResultsParam(data) {
  return request({
    url: '/registration/selectRefundResultsParam',
    method: 'post',
    data
  })
}
