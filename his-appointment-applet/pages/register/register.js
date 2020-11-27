import WxValidate from '../../utils/WxValidate'
let validate = null;
let app =  getApp();
Page({
  data: {
    idcard: '',
    username: '',
    address: '',
    phone: ''
  },
  listenerIdInput: function (e) {
    this.data.idcard = e.detail.value
  },
  listenerNameInput: function (e) {
    this.data.username = e.detail.value
  },
  listenerAddrInput: function (e) {
    this.data.address = e.detail.value
  },
  listenerPhoneInput: function (e) {
    this.data.phone = e.detail.value
  },
  showModal(error) {
    wx.showModal({
      content: error.msg,
      showCancel: false,
    })
  },
  submitForm(e) {
    const params = e.detail.value
    if (!validate.checkForm(params)) {
      const error = validate.errorList[0]
      this.showModal(error)
      return false
    }
    this.submitInfo(params);
  },
  submitInfo(params) {
    let form = params;
    let identificationNo = this.data.idcard
    let username = this.data.username
    let address = this.data.address
    let phone = this.data.phone
    let birth = identificationNo.substring(6, 10) + "-" + identificationNo.substring(10, 12) + "-" + identificationNo.substring(12, 14)
    let result = identificationNo.substring(16, 17)
    let gender = ''
    if (result % 2 != 0) {
      gender = '0'
    } else {
      gender = '1'
    }
    wx.showLoading();
    wx.request({
      method: 'post',
      url: app.url + '/pmsPatient/insertBindPmsPatient',
      data: {
        identificationNo: identificationNo,
        openId: wx.getStorageSync('APP_OPENID'),
        picture: '',
        name: username,
        homeAddress: address,
        phoneNo: phone,
        dateOfBirth: birth,
        gender: gender,
      },
      success: (res) => {
        wx.setStorageSync('APP_PATIENT_ID', res.data.data.id);
        wx.reLaunch({
          url: '/pages/my/my',
        });
      },
      complete: wx.hideLoading
    })
  },
  // onShow() {
  //   wx.hideHomeButton && wx.hideHomeButton();
  // },
  onLoad() {
    const rules = {
      idcard: {
        required: true,
        idcard: true
      },
      username: {
        required: true,
      },
      address: {
        required: true,
      },
      phone: {
        required: true,
        tel: true
      },

    }
    const messages = {
      username: {
        required: '请输入姓名',
      },
      idcard: {
        required: '请输入身份证',
      },
      phone: {
        required: '请输入手机号',
      },
      address: {
        required: '请输入地址',
      },

    }
    validate = new WxValidate(rules, messages)
  },
})