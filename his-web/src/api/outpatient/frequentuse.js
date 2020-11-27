import request from '@/utils/request'
export function selectByType(data) {
  return request({
    url: '/frequentUsed/selectByType',
    method: 'post',
    params: {
      staffId:data.staffId,
      selectType:data.selectType
    },
  })
}

export function addfre(data) {
  return request({
    url: '/frequentUsed/add',
    method: 'post',
    params: {
      staffId:data.staffId,
      addType:data.addType,
      addId:data.addId
    },
  })
}


export function delfre(data) {
  return request({
    url: '/frequentUsed/delete',
    method: 'post',
    params: {
      staffId:data.staffId,
      deleteType:data.deleteType,
      deleteId:data.deleteId
    },
  })
}

export function addInform(data) {
  return request({
    url: '/frequentUsed/addInform',
    method: 'post',
    data
  })
}

export function showInformList(data) {
  return request({
    url: '/frequentUsed/selectInform',
    method: 'post',
    data
  })
}

export function deleteInform(data) {
  return request({
    url: '/frequentUsed/deleteInform',
    method: 'post',
    params: {
      frequentId:data
    },
  })
}

export function updateInform(data) {
  return request({
    url: '/frequentUsed/updateInform',
    method: 'post',
    data
  })
}
