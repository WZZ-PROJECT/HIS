import request from '@/utils/request'
export function getMechlist(data) {
  return request({
    url: '/DmsMechanicItemRecord/listByDept',
    method: 'post',
    params:{
      deptId:data.deptId,
      pageSize:data.pageSize,
      pageNum:data.pageNum,
      name:data.name
    }
  })
}

export function log(data) {
  return request({
    url: '/DmsMechanicItemRecord/log',
    method: 'post',
    params:{
      itemRecordId:data.id,
      logStaffId:data.logStaffId
    }
  })
}

export function uploadResult(data) {
  return request({
    url: '/DmsMechanicItemRecord/uploadResult',
    method: 'post',
    params:{
      id:data.id,
      executeStaffId:data.executeStaffId,
      checkResult:data.checkResult,
      resultImgUrlList:data.resultImgUrlList
    }
  })
}

// 收费员处
export function listByDeptChange(data) {
  return request({
    url: '/DmsMechanicItemRecord/listByDeptChange',
    method: 'post',
    data
  })
}
// 退费
export function refundCharge(data) {
  return request({
    url: '/fee/refundCharge',
    method: 'post',
    data
  })
}
