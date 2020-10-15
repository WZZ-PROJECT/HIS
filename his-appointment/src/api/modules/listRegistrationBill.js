import request from 'axios';
export const listRegistrationBill = (data) => {
    return request({
        url: '/api/staff/listRegistrationBill',
        method: 'post',
        data
    })
}