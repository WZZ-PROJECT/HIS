<template>
  <div>
    <van-cell-group style="padding: 50px 0">
      <van-field v-model="value" label="预存金额" placeholder="请输入预存金额" />
    </van-cell-group>
    <van-button type="primary" block @click="handlePay">支付</van-button>
    <!-- <van-button type="primary" block @click="test">支付</van-button> -->
  </div>
</template>

<script>
import axios from "axios";
import wx from "weixin-js-sdk";

export default {
  data() {
    return {
      value: "",
    };
  },
  // mounted() {
  //   this.getSignature();
  // },
  methods: {
    // getSignature() {
    //   const timestamp = new Date().getTime();
    //   const noncestr = this.randomString(16);
    //   const params = {
    //     timestamp,
    //     noncestr,
    //     url: window.location.href,
    //   };
    //   axios
    //     .get("/api/getPackage/getSignature", {
    //       params,
    //       header: {
    //         "Content-Type": "application/json",
    //       },
    //     })
    //     .then((res) => {
    //       wx.config({
    //         debug: false,
    //         appId: "wx5413df4415f5b4eb", // 必填，公众号的唯一标识
    //         timestamp, // 必填，生成签名的时间戳
    //         nonceStr: noncestr, // 必填，生成签名的随机串
    //         signature: res.data.data, // 必填，签名
    //         jsApiList: ["chooseWXPay"], // 必填，需要使用的JS接口列表
    //       });
    //     });
    // },
    handlePay() {
      const value = this.value.trim();
      if (isNaN(value) || !value) {
        this.$toast.fail('请输入正确的金额');
        return;
      }
      axios
        .get("/api/getPackage/unifiedOrder", {
          params: {
            money: value,
            openid: localStorage.APP_OPEN_ID,
          },
        })
        .then((res) => {
          if (res.data.data.result_code === "SUCCESS") {
            const { prepay_id, out_trade_no } = res.data.data;
            this.requestPaySign(prepay_id, value, out_trade_no);
          }
        });
    },
    requestPaySign(id, value, trade_no) {
      const timeStamp = new Date().getTime();
      const nonceStr = this.randomString(16);
      axios
        .get("/api/getPackage/getSignType", {
          params: {
            timeStamp,
            nonceStr,
            prepay_id: id,
          },
          header: {
            "Content-type": "application/json",
          },
        })
        .then((res) => {
          this.wxPay(timeStamp, nonceStr, id, res.data.data, value, trade_no);
        });
    },
    wxPay(timestamp, nonceStr, id, paySign, money, out_trade_no) {
      const that = this;
      const patientid = this.$store.state.patientId;
      this.$wx.chooseWXPay({
        timestamp,
        nonceStr,
        package: "prepay_id=" + id,
        signType: "MD5",
        paySign,
        success: function(t) {
          that.$toast.success('充值成功');
          axios.post('/api/registration/WxProgramResults', {
            patientid,
            out_trade_no,
            openid: localStorage.openID
          })
        },
      });
    },
    test() {
      axios.post('/api/registration/WxProgramResults', {
        patientid: 23,
        out_trade_no: 'xTjezyMzv7m6IhvewwFXQ1Ln1SPCeElf',
        openid: localStorage.openID
      })
    },
    randomString(t) {
      t = t || 32;
      for (
        var e = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678",
          a = e.length,
          n = "",
          i = 0;
        i < t;
        i++
      )
        n += e.charAt(Math.floor(Math.random() * a));
      return n;
    },
  },
};
</script>

<style>
</style>