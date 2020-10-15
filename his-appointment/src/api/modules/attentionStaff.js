import request from 'axios';
export const attentionStaff = (data) => {
      return request({
        url: '/api/staff/attentionStaff',
        method: 'post',
        data
      })
    }