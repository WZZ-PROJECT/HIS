// pages/detail/detail.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    patient: '',
    picture: '',
  },
  handleEdit() {
    wx.navigateTo({
      url: '/pages/edit/edit',
    })
  },
  cancel: function (event) {
    const that = this;
    let identificationNo = event.currentTarget.dataset.gid
    wx.showModal({
      title: '提示',
      content: '是否解除绑定？',
      success(res) {
        if (res.confirm) {
          wx.showLoading();
          wx.request({
            method: 'post',
            url: app.url + '/staff/bindPatient',
            data: {
              "identificationNo": identificationNo,
              "openId": '',
              "picture": that.data.picture
            },

            success: () => {
              wx.removeStorageSync('APP_PATIENT_ID');
              wx.reLaunch({
                url: '/pages/my/my',
              });
            },
            complete: wx.hideLoading
          })
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onShow: function () {
    const openId = wx.getStorageSync('APP_OPENID');
    const userInfo = wx.getStorageSync('APP_USER_INFO');
    this.setData({
      openId,
      userInfo
    })
    wx.request({
      method: 'get',
      url: app.url + '/diagnosisPatient/getPatient?openId=' + openId,
      success: res => {
        this.setData({
          patient: res.data.data
        })
      }
    })
  },
})