// pages/my/my.js
//获取应用实例
const GET_USER_INFO = 'getUserInfo:ok';
const USER_NOT_LOGIN = 'USER_NOT_LOGIN';
const PATIENT_NOT_BIND = 'PATIENT_NOT_BIND';
const PATIENT_BIND = 'PATIENT_BIND';
const RdWXBizDataCrypt = require('../../utils/RdWXBizDataCrypt.js');
const app = getApp()
Page({
  data: {
    userInfo: null,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    code: '',
    loginState: false,
    hasBind: USER_NOT_LOGIN
  },
  /**
   * 生命周期函数--监听页面显示
   */
  handleLogin() {
    wx.navigateTo({
      url: '../login/login'
    })
  },
  handleNavigate(event) {
    const url = event.target.dataset.url;
    if (!url) return;
    if (!this.data.loginState) {
      wx.showModal({
        title: '提示',
        content: '尚未登录，请登录',
        showCancel: false
      });
      return;
    }
    const patient = wx.getStorageSync('APP_PATIENT_ID');
    if (this.data.hasBind === USER_NOT_LOGIN) {
      const openid = wx.getStorageSync('APP_OPENID');
      openid && this.reqeustPatientDetail(openid, () => {
        wx.navigateTo({
          url
        });
      });
    } else if (this.data.hasBind === PATIENT_NOT_BIND) {
      wx.showModal({
        title: '提示',
        content: '尚未绑定个人信息，是否绑定？',
        success(res) {
          if (res.confirm) {
            wx.navigateTo({
              url: '/pages/register/register',
            });
          }
        }
      });
    } else {
      if (this.data.userInfo) {
        wx.navigateTo({
          url
        });
      } else {
        wx.showModal({
          title: '提示',
          content: '是否登录？',
          success(res) {
            if (res.confirm) {
              wx.navigateTo({
                url: '/pages/login/login',
              });
            }
          }
        });
      }
    }
  },
  bindgetuserinfo(res) {
    //拿到用户信息
    const that = this;
    if (res.detail.errMsg === GET_USER_INFO) {
      wx.showLoading();
      wx.login({
        success: ({
          code
        }) => {
          this.setData({
            code
          });
          wx.getUserInfo({
            success: user => {
              that.saveUserInfo(user);
            }
          })
        },
        fail: wx.hideLoading
      })
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
    this.setData({
      userInfo: user.userInfo,
    })
    wx.setStorage({
      key: 'APP_USER_INFO',
      data: user.userInfo,
      success: () => {
        this.setData({
          loginState: true
        });
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
          this.reqeustPatientDetail(unionid);
          wx.setStorageSync('APP_OPENID', unionid);
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
  reqeustPatientDetail(openId, callback) {
    const that = this;
    if (callback) {
      wx.showLoading();
    }
    wx.request({
      method: 'POST',
      url: app.url + '/staff/isBandtoWechat?openId=' + openId,
      success: e => {
        //后台生成openID后判断患者表是否存在openID，存在：
        if (e.data) {
          that.setData({
            hasBind: PATIENT_BIND
          });
          wx.request({
            method: 'get',
            url: app.url + '/pmsPatient/patientByOpenId?openId=' + openId,
            success: res => {
              var data = res.data.data
              if (data != null) {
                wx.setStorageSync('APP_PATIENT_ID', data.id);
                callback && callback();
              } else {
                wx.removeStorageSync('APP_PATIENT_ID');
              }
            },
            fail: () => {
              wx.showToast({
                title: '请求超时',
                icon: 'none',
                duration: 3000,
              });
              reject();
            },
            complete: wx.hideLoading
          })
        } else {
          wx.removeStorageSync('APP_PATIENT_ID');
          that.setData({
            hasBind: PATIENT_NOT_BIND
          });
          wx.hideLoading();
          wx.showModal({
            title: '提示',
            content: '尚未绑定个人信息，是否绑定？',
            success(res) {
              if (res.confirm) {
                wx.navigateTo({
                  url: '/pages/register/register',
                });
              }
            }
          });
        }
      },
      fail() {
        wx.hideLoading();
        wx.showToast({
          title: '请求超时',
          icon: 'none',
          duration: 3000,
        });
      },
    })
  },
  onShow() {
    const userInfo = wx.getStorageSync('APP_USER_INFO')
    const patientId = wx.getStorageSync('APP_PATIENT_ID')
    if (userInfo) {
      this.setData({
        loginState: true,
        userInfo: wx.getStorageSync('APP_USER_INFO'),
        hasBind: patientId ? PATIENT_BIND : USER_NOT_LOGIN
      })
    }
  }
  // onLoad() {
  //   const that = this;
  //   wx.login({
  //     success: ({
  //       code
  //     }) => {
  //       this.setData({
  //         code
  //       });
  //       wx.getSetting({
  //         success(res) {
  //           if (res.authSetting['scope.userInfo']) {
  //             wx.getUserInfo({
  //               success: user => {
  //                 that.saveUserInfo(user);
  //               }
  //             })
  //           }
  //         }
  //       })
  //     }
  //   })
  // }
})