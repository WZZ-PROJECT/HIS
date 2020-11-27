<template>
  <div>
    <router-view></router-view>
  </div>
</template>

<script>
import axios from "axios";
export default {
  created() {
    const url = encodeURI(window.location.href);
    const { appfrom, state, code, openId } = this.$route.query;
    const loginState = this.$store.state.loginState;
    // 小程序关注医生进入(appfrom=applet)
    // 微信公众号授权登录(state=success)
    if (!localStorage.openID && !state && !appfrom && !openId) {
      window.location.href =
        `https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5413df4415f5b4eb&redirect_uri=${url}&response_type=code&scope=snsapi_userinfo&state=success#wechat_redirect`
    } else {
      if (appfrom) {
        localStorage.APP_FROM = appfrom === "applet" ? "applet" : "";
      }
      if (openId) {
        localStorage.setItem("openID", openId);
      }
      // if (patientId) {
      //   localStorage.setItem("patientId", patientId);
      // }
      if (state === "success") {
        // =>code
        // 微信公众号授权登录
        localStorage.APP_FROM = '';
        axios({
          url: "/api/staff/wxCallBack?code=" + code,
          method: "post",
          data: {
            code,
          },
        }).then((res) => {
          if (res.data.code === 200) {
            const { unionid: openId, openid } = res.data.data;
            this.$store.commit("setUserInfo", res.data.data);
            localStorage.setItem('APP_USER_INFO', JSON.stringify(res.data.data));
            localStorage.setItem("openID", openId);
            localStorage.setItem("APP_OPEN_ID", openid);
          }
        });
      }
    }
  },
};
</script>

<style lang="less">
.page-header-fixed {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
}
</style>