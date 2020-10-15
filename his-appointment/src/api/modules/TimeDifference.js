import request from 'axios';
export const TimeDifference = (skdId,ruletime,noon) => {
      return request({
        url: '/api/registration/TimeDifference?ruletime='+ruletime+'&skdId='+skdId+'&noon='+noon,
        method: 'get',
      })
    }