import request from 'axios';
export const getStaffList = (staffId,patientId) => {
      return request({
        url: '/api/staff/selectStaffById?id='+staffId+'&patientId='+patientId,
        method: 'get',
      })
    }