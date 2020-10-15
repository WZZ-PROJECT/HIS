import request from 'axios';
export const selectStaffByName = (name) => {
      return request({
        url: '/api/staff/selectStaffByName?name='+name,
        method: 'get',
      })
    }