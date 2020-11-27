import Vue from 'vue';
import wx from "weixin-js-sdk";
import axios from 'axios';
import { randomString } from './common';

const timestamp = new Date().getTime();
const noncestr = randomString(16);
const params = {
  timestamp,
  noncestr,
  url: window.location.href,
};
let time = 0;

const wxInit = function() {
  if (time > 1) return
  time += 1;
  axios
    .get("/api/getPackage/getSignature", {
      params,
      header: {
        "Content-Type": "application/json",
      },
    })
    .then((res) => {
      wx.config({
        debug: false,
        appId: "wx5413df4415f5b4eb", // 必填，公众号的唯一标识
        timestamp, // 必填，生成签名的时间戳
        nonceStr: noncestr, // 必填，生成签名的随机串
        signature: res.data.data, // 必填，签名
        jsApiList: ["chooseWXPay"], // 必填，需要使用的JS接口列表
      });
      wx.ready(() => {
        Vue.prototype.$wx = wx;
      });
      wx.error(wxInit);
    });
}

if (process.env.NODE_ENV !== 'development') {
  wx.miniProgram.getEnv(function (res) {
    //获取当前环境
    if (!res.miniprogram) {
      wxInit();
    }
  });
}