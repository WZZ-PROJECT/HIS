// pages/appiontment/appiontment.js
//获取应用实例
const app = getApp()
Page({
  data: {
    openId: '',
    patientId: '',
    web: app.globalData.web,
  },
  onLoad({ from, deptId, deptName, date, staffId, urlfrom }) {
    let url = app.globalData.web;
    const openId = wx.getStorageSync('APP_OPENID'),
      time = new Date().getTime();
    if (from === 'order') {
      if (urlfrom === 'fourth') {
        url += `/appointment/fourth?deptId=${deptId}&deptName=${deptName}&date=${date}&openId=${openId}&appfrom=applet&timestamp=${time}`
      } else if(urlfrom === 'detail') {
        url += `/appointment/patientPage?staffId=${staffId}&openId=${openId}&appfrom=applet&timestamp=${time}`
      }
    } else {
      url += `?openId=${openId}&appfrom=applet&timestamp=${time}`
    }
    this.setData({
      web: url
    });
  },
  // onShow() {
  //   this.setData({
  //     patientId: wx.getStorageSync('APP_PATIENT_ID'),
  //     openId: wx.getStorageSync('APP_OPENID'),
  //   })
  // },
})
