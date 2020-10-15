import request from 'axios';
export const select = () => {
      return request({
        url: '/api/dept/selectAll',
        method: 'get',
       
      })
    }