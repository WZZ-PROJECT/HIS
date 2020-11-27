// pages/ts/login/authorize-login.js
const GET_USER_INFO = 'getUserInfo:ok'
const RdWXBizDataCrypt = require('../../utils/RdWXBizDataCrypt.js');
const app = getApp()
Page({
  data: {
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    code: ''
  },
  // 用户授权信息
  bindgetuserinfo(res) {
    //拿到用户信息
    if (res.detail.errMsg === GET_USER_INFO) {
      this.saveUserInfo(res.detail);
    } else {
      const errMsg = res.detail.errMsg === 'getUserInfo:fail auth deny' ? '需要通过授权才能继续，请重新点击并授权' : res.detail.errMsg
      wx.showModal({
        title: '提示',
        content: errMsg,
        showCancel: false
      })
    }
  },
  saveUserInfo(user) {
    const code = this.data.code;
    wx.showLoading();
    wx.setStorage({
      key: 'APP_USER_INFO',
      data: user.userInfo,
      success: () => {
        this.requestOpenId(code, user.encryptedData, user.iv);
      },
    })
  },
  requestOpenId(code, encryptedData, iv) {
    wx.request({
      method: 'POST',
      url: app.url + '/staff/getOpenId?code=' + code,
      success: e => {
        if (e.data.code === 200) {
          //后台生成openID后判断患者表是否存在openID
          const vaild = new RdWXBizDataCrypt(app.globalData.AppID, e.data.data.session_key)
          let unionid;
          if (e.data && e.data.data.unionid) {
            unionid = e.data.data.unionid
          } else {
            unionid = vaild.decryptData(encryptedData, iv).unionId
          }
          this.reqeustPatientDetail(unionid)
          wx.setStorageSync('APP_PAY_OPEN_ID', e.data.data.openid);
        } else {
          wx.hideLoading();
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '请求超时',
          icon: 'none',
          duration: 3000,
        });
      },
    })
  },
  reqeustPatientDetail(openId) {
    wx.setStorageSync('APP_OPENID', openId);
    wx.request({
      method: 'get',
      url: app.url + '/pmsPatient/patientByOpenId?openId=' + openId,
      success: res => {
        var data = res.data.data
        if (data != null) {
          wx.setStorageSync('APP_PATIENT_ID', data.id);
        }
        wx.request({
          method: 'POST',
          url: app.url + '/staff/isBandtoWechat?openId=' + openId,
          success: e => {
            //后台生成openID后判断患者表是否存在openID，存在：
            if (e.data) {
              wx.navigateBack();
            } else {
              wx.showModal({
                title: '提示',
                content: '尚未绑定个人信息，是否绑定？',
                success(res) {
                  if (res.confirm) {
                    wx.navigateTo({
                      url: '/pages/register/register',
                    });
                  } else {
                    wx.navigateBack();
                  }
                }
              });
            }
          },
          fail() {
            wx.showToast({
              title: '请求超时',
              icon: 'none',
              duration: 3000,
            });
          },
          complete: wx.hideLoading
        })
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '请求超时',
          icon: 'none',
          duration: 3000,
        });
      },
    })
  },
  onLoad() {
    const that = this;
    wx.login({
      success: ({
        code
      }) => {
        this.setData({
          code
        });
        wx.getSetting({
          success(res) {
            if (res.authSetting['scope.userInfo']) {
              wx.getUserInfo({
                success: user => {
                  that.saveUserInfo(user);
                }
              })
            }
          }
        })
      }
    })
  }
})