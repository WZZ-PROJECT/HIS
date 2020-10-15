import request from 'axios';
export const pay = (data) => {
  return request({
    url: '/api/registration/programCreateRegistration',
    method: 'post',
    data
  })
}
