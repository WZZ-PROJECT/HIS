// pages/payFee/payFee.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    index: 0,
    status: -1,
    patientId: '',
    resultList: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //根据openId查询patientId
    wx.request({
      method: 'post',
      url: app.url + '/fee/listCharge',
      data: {
        openId: wx.getStorageSync('APP_OPENID'),
        patientId: wx.getStorageSync('APP_PATIENT_ID'),
        // patientId:16,
      },
      success: res => {
        this.setData({
          resultList: res.data.data
        })
      },
      fail(err) {
        console.log(err)
      }
    })
  },
  changeStatus: function (event) {
    var index = event.currentTarget.dataset['index'];
    if (index == this.data.status) {
      index = -1
    }
    this.data.status = index
    this.setData({
      status: index
    })
  }
})