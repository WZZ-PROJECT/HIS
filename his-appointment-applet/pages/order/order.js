// pages/order.js
import Dialog  from '../../miniprogram_npm/@vant/weapp/dialog/dialog';

const app = getApp()
Page({
  /**
   * 页面的初始数据
   */
  data: {
    name: 'mzDoctor',
    role: '门诊医生',
    src: 'http://shandongciguang.oss-cn-beijing.aliyuncs.com/1601019313149.JPG',
    dept: '',
    patientName: '',
    price: '',
    date: '',
    time: '',
    options: null,
    showNotice: false,
    department: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const { staffId, date, time, patientId, deptId, urlfrom } = options;
    const that = this;
    wx.showLoading();
    wx.request({
      method: 'post',
      url: app.url + '/staff/listRegistrationBill',
      data: {
        patientId,
        staffId
      },
      success(res) {
        if (res.data.code === 200 && res.data.data) {
          that.setData({
            dept: res.data.data.deptName,
            name: res.data.data.staffName,
            role: res.data.data.title,
            price: res.data.data.amount,
            patientName: res.data.data.patientName,
            card: res.data.data.card,
            src: res.data.data.picture,
            date,
            options,
            time: time ? decodeURIComponent(time) : ''
          });
        } else {
          wx.showToast({
            title: '请求超时',
            icon: 'none',
            duration: 3500,
          });
        }
      },
      fail() {
        wx.showToast({
          title: '请求超时',
          icon: 'none',
          duration: 3500,
        });
      },
      complete: wx.hideLoading
    });
    this.setData({
      showNotice: true,
      department: {
        deptId,
        date,
        staffId,
        urlfrom
      }
    });
  },
  handlePay() {
    const { price: rechargeMoney, options, time } = this.data;
    const that = this;

    // const { dept: deptName, department: { deptId, date, staffId, urlfrom } } = that.data;
    // wx.reLaunch({
    //   url: `/pages/appiontment/appiontment?from=order&deptId=${deptId}&deptName=${deptName}&date=${date}&staffId=${staffId}&urlfrom=${urlfrom}`,
    // });
    // return;

    wx.showLoading();
    wx.request({
      method: 'get',
      url: app.url + '/getPackage/getPackage',
      data: {
        openId: wx.getStorageSync('APP_PAY_OPEN_ID'),
        rechargeMoney
      },
      success: res => {
        let data = res.data.data
        wx.requestPayment({
          timeStamp: data.timeStamp,
          nonceStr: data.nonceStr,
          package: data.package,
          signType: data.signType,
          paySign: data.paySign,
          success() {
            wx.showLoading();
            wx.request({
              method: 'post',
              url: app.url + '/registration/programCreateRegistration',
              data: {
                skdId: options.skdId,
                deptId: options.deptId,
                attendanceDate: options.date,
                patientId: options.patientId,
                amount: rechargeMoney,
                time,
                out_trade_no: data.out_trade_no
              },
              success(res) {
                if (res.data.data === 1) {
                  // wx.showToast({
                  //   title: res.data.message,
                  //   icon: 'success',
                  //   duration: 3000,
                  // });
                  const { dept: deptName, department: { deptId, date, staffId, urlfrom } } = that.data;
                  Dialog.alert({
                    title: '提示',
                    message: res.data.message,
                  }).then(() => {
                    wx.reLaunch({
                      url: `/pages/appiontment/appiontment?from=order&deptId=${deptId}&deptName=${deptName}&date=${date}&staffId=${staffId}&urlfrom=${urlfrom}`,
                    });
                  });
                } else {
                  wx.showToast({
                    title: res.data.message,
                    icon: 'none',
                    duration: 3000,
                  });
                }
              },
              complete: wx.hideLoading
            })
          },
          fail: function (res) {
            wx.hideLoading();
            console.log("用户取消支付")
            console.log(res)
          }
        })
      },
      complete: wx.hideLoading
    })
  },
  handleNoticeOpen() {
    this.setData({
      showNotice: true
    });
  },
  handleNoticeClose() {
    this.setData({
      showNotice: false,
    });
  }
})