import request from '@/utils/request'
export function login(data) {
  return request({
    url: '/staff/login',
    method: 'post',
    data
  })
}

export function create(data) {
  return request({
    url: '/staff/create',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/staff/update/'+data.id,
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/staff/info',
    method: 'get'
  })
}

export function logout() {
  return Promise.resolve()
}

export function select(data){
  return request({
    url: 'staff/select',
    data,
    method: 'post',
  })
}

export function getUserList(data){
  return request({
    url: 'staff/select',
    data,
    method: 'post',
  })
}

export function getUserListAll(){
  return request({
    url: 'staff/selectAll',
    method: 'get',
  })
}

export function checkPassword(data){
  return request({
    url: 'staff/checkPassword',
    method: 'post',
    data
  })
}

export function updatePassword(data){
  return request({
    url: 'staff/updatePassword',
    method: 'post',
    data
  })
}

//阿里云上传附件
export const uploadFileAli = (file,callback) => {
  let OSS = require('ali-oss')
  let client = new OSS({
    region: 'oss-cn-beijing',
    accessKeyId: 'LTAI4GEuRodq2mqRe8pigqT2',
    accessKeySecret: 'DB6pN1VRizFzU62qdza9o80bu8B3pN',
    bucket: 'shandongciguang'
  });

  const tmpcnt = file.name.lastIndexOf(".")
  const exname = file.name.substring(tmpcnt + 1)

  let storeAs = new Date().getTime() + '.' + exname;//图片名称
  client.multipartUpload(storeAs,file).then(result =>{
    const imgurl = result.res.requestUrls[0].split('?')[0];//返回图片地址
    callback(file,imgurl);
  }).catch(function (err) {

  });
}


export function selectMaintenanceParam(){
  return request({
    url: '/staff/selectMaintenanceParam',
    method: 'post',
  })
}

export function updateMaintenanceParam(data){
  return request({
    url: '/staff/updateMaintenanceParam',
    method: 'post',
    data
  })
}

