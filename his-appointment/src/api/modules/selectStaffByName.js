import request from 'axios';
export const selectStaffByName = (data) => {
      return request({
        url: '/api/staff/selectStaffByName?name='+data,
        method: 'get',
      })
    }