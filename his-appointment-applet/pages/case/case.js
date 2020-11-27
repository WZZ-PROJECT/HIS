// pages/case/case.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    index: 0,
    status: -1,
    caseList: null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    //根据openId查询patientId
    wx.request({
      method: 'get',
      url: app.url + '/caseHistory/queryCaseHistory?patientId=' + wx.getStorageSync('APP_PATIENT_ID'),
      success: res => {
        this.setData({
          caseList: res.data.data.dmsCaseHistoryList || []
        })
      }
    })
  },
  onShow: function (options) {

  },

  //原本没有upStatus这个字段，所以默认值为false
  upDown(event) {
    var index = event.currentTarget.dataset['index'];
    if (this.data.status == index) {
      this.data.status = -1
    } else {
      this.data.status = index
    }
    this.setData({
      status: this.data.status
    })
  },
})