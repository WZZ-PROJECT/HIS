import request from 'axios';
export const getDoctorAndSkd = (staffId,patientId) => {
      return request({
        url: '/api/staff/listDoctor?staffId='+staffId+'&patientId='+patientId,
        method: 'get',
      })
    }