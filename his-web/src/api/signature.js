import request from '@/utils/request'

// 查询
export function getSignatureList(data) {
  return request({
    url: '/signature/select',
    method: 'post',
    params: {
      pageSize:data.limit,
      pageNum:data.page
    },
    data
  })
}
export function getAllDep(){
  return request({
    url: '/dept/selectAll',
    method: 'get'
  })
}

// 创建签章
export function createSignature(data){
  return request({
    url: '/signature/create',
    method: 'post',
    data
  })
}

export function deleteSignature(ids){
  return request({
    url: '/signature/delete',
    method: 'post',
    params: {ids}
  })
}

//更新签章
export function updateSignature(data){
  return request({
    url: '/signature/update/'+data.id,
    method: 'post',
    data
  })
}

//查询签章类型
export function selectType(){
  return request({
    url: '/signature/selectType/',
    method: 'get',
  })
}

//查询正在应用的签章
export function selectAll(){
  return request({
    url: '/signature/selectAll/',
    method: 'get',
  })
}
