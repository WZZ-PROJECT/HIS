// pages/pay/pay.js
const app = getApp()
Page({
  data: {
    money: '',
  },
  /**
   * 生命周期函数--监听页面加载
   */
  money: function (e) {
    this.setData({
      money: e.detail.value
    })
  },

  //事件处理函数
  toPay: function () {
    let money = this.data.money
    wx.getSetting({
      success(res) {
        if (res.authSetting['scope.userInfo']) {
          wx.login({
            success: re => {
              // 发送 res.code 到后台换取 openId, sessionKey, unionId
              wx.request({
                method: 'POST',
                url: app.url + '/staff/getOpenId?code=' + re.code,
                success: e => {
                  wx.request({
                    method: 'get',
                    url: app.url + '/getPackage/getPackage',
                    data: {
                      openId: e.data.data.openid,
                      rechargeMoney: money
                    },
                    success: res => {
                      let data = res.data.data
                      wx.requestPayment({
                        timeStamp: data.timeStamp,
                        nonceStr: data.nonceStr,
                        package: data.package,
                        signType: data.signType,
                        paySign: data.paySign,
                        success: function (res) {
                          wx.request({
                            method: 'post',
                            url: app.url + '/registration/WxProgramResults',
                            data: {
                              // timeStamp: data.timeStamp,
                              // nonceStr: data.nonceStr,
                              // packages: data.package,
                              // signType: data.signType,
                              // paySign: data.paySign,
                              // amount: money,
                              patientid: wx.getStorageSync('APP_PATIENT_ID'),
                              out_trade_no: data.out_trade_no,
                              openid: wx.getStorageSync('APP_OPENID')
                            },
                            success: function (res) {
                              wx.showToast({
                                title: '充值成功！',
                                icon: 'none', //如果要纯文本，不要icon，将值设为'none'
                                duration: 2000
                              })
                            }
                          })
                        },
                        fail: function (res) {
                          console.log("用户取消支付")
                          console.log(res)
                        },
                        complete: function (res) {
                          console.log("调用支付失败")
                          console.log(res)
                        }
                      })
                    }
                  })
                }
              })
            }
          })
        }
      }
    })
  },
})