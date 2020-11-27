// pages/attention/attention.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    resultList: null,
    web: app.globalData.web,
    patientId: wx.getStorageSync('APP_PATIENT_ID'),
    staffId: '',
    deptId: '',
    showDetail: false,
    time: new Date().getTime(),
    openId: '',
    staffId: ''
  },
  cancel: function (event) {
    const that = this;
    wx.showModal({
      title: '提示',
      content: '是否取消关注？',
      success(res) {
        if (res.confirm) {
          wx.showLoading();
          const staffId = event.currentTarget.dataset.gid
          const patientId = wx.getStorageSync('APP_PATIENT_ID')
          wx.request({
            method: 'post',
            url: app.url + '/pmsPatient/stopFollow',
            data: {
              patientId,
              staffId
            },
            success: res => {
              if (res.data) {
                wx.request({
                  method: 'get',
                  url: app.url + '/pmsPatient/queryStaffByOpenId?openId=' + wx.getStorageSync('APP_OPENID'),
                  success: res => {
                    that.setData({
                      resultList: res.data.data
                    })
                  },
                  complete: wx.hideLoading
                })
              }
            },
            fail: wx.hideLoading
          })
        }
      }
    })

  },
  handleDoctorDetailClick: function (event) {
    this.setData({
      staffId: event.currentTarget.dataset.staffid,
      deptId: event.currentTarget.dataset.deptid
    }, () => {
      this.setData({
        showDetail: true
      });
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    let openId = wx.getStorageSync('APP_OPENID')
    wx.request({
      method: 'get',
      url: app.url + '/pmsPatient/queryStaffByOpenId?openId=' + openId,
      success: res => {
        this.data.resultList = res.data.data
        this.setData({
          resultList: this.data.resultList
        })
      }
    })

  },
  onShow() {
    this.setData({
      openId: wx.getStorageSync('APP_OPENID'),
    })
  },
})