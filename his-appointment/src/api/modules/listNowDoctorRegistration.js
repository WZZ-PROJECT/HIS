import request from 'axios';
export const listNowDoctorRegistration = ( params ) => {
    console.log(params)
      return request({
        url: '/api/staff/listNowDoctorRegistration',
        method: 'get',
        params
      })
    }