// pages/record/record.js
const app = getApp()
Page({
  /**
   * 页面的初始数据
   */
  data: {
      resultList: null,
      yuyue:""
  },
  cancel : function(event) {
    const that = this;
    wx.showModal({
      title: '提示',
      content: '是否退号？',
      success(res) {
        if (res.confirm) {
          let id = event.currentTarget.dataset.gid
          wx.showLoading();
          wx.request({
            method:'post',
            url: app.url+'/fee/refundRegistrationCharge',
            data: {
              operatorId: wx.getStorageSync('APP_PATIENT_ID'),
              registrationId: id
            },               
            success: res => {
              if (res.data.data === 1) {
                that.requestList();
              }
              wx.showToast({ title: res.data.message, icon: 'none' });
            },
            complete: wx.hideLoading
          })
        }
      }
    })
  },
  requestList() {
    let openID = wx.getStorageSync('APP_OPENID')
    this.setData({
      resultList: null
    });
    wx.request({
      method:'get',
      url: app.url + '/staff/listPatientConvention?openID='+openID,
      success: res => {
        const list = res.data.data || []
        this.setData({
          resultList: list.reverse()
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.requestList();
  },
})