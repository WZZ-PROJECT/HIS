import request from 'axios';
export const getHistoryDoctorList = (patientId) => {
  return request({
    url: '/api/staff/listHistoryDoctor?patientId='+patientId,
    method: 'get',
  })
}