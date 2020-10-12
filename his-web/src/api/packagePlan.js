import request from '@/utils/request'
export function SetmealListRule(data) {
  return request({
    url: '/setmeal/SetmealListRule',
    method: 'post',
    params:data
  })
}

export function generateSetMealSkd(data) {
  return request({
    url: '/setmeal/generateSetMealSkd',
    method: 'post',
    params:data
  })
}

export function createSetmealRule(data) {
  return request({
    url: '/setmeal/createSetmealRule',
    method: 'post',
    data
  })
}

export function getSetmealRuleDetail(id) {
  return request({
    url: '/setmeal/getSetmealRuleDetail',
    method: 'post',
    params: {
      ruleId:id
    }
  })
}

export function updateSetmealRule(data) {
  return request({
    url: '/setmeal/updateSetmealRule/'+data.ruleid,
    method: 'post',
    data
  })
}

export function deleteSetmealRule(data) {
  return request({
    url: '/setmeal/deleteSetmealRule',
    method: 'post',
    params:{
      ids:data
    }
  })
}

export function listSetmealSkd(data) {
  return request({
    url: '/setmeal/listSetmealSkd',
    method: 'post',
    data
  })
}

export function selectSetmeal(data){
  return request({
    url: '/setmeal/selectSetmeal',
    data,
    params:{
      pageSize:100000,
      pageNum:1
    },
    method: 'post',
  })
}
