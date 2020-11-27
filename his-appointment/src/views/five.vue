<template>
  <div class="order-info-page">
    <hr />
    <div class="doctor-info">
      <div>
        <span>{{ name }}</span>
        <span>{{ role }}</span>
      </div>
      <div>
        <img v-if="src" :src="src" :alt="name" />
        <img v-else src="../assets/img/picture.png" alt="" />
      </div>
    </div>
    <hr />
    <div>
      <van-field v-model="dept" label="就诊科室" readonly />
      <!-- <van-field v-model="address" label="科室地点" readonly /> -->
      <van-field v-model="patientName" label="就诊人" readonly />
      <van-field v-model="price" label="诊疗费" readonly />
      <van-field v-model="date" label="就诊日期" readonly />
      <van-field v-model="time" label="就诊时间" readonly />
    </div>
    <hr />
    <!-- <div>
      <van-field v-model="card" label="卡号" readonly />
    </div> -->
    <a href="javascript:;" class="modal-btn" @click="showNotice = true">
      <h5 style="text-align: left; color: #b61717">预约即同意 《预约须知》</h5>
    </a>
    <hr />
    <van-button type="primary" @click="handlePay" block round
      >确认预约并支付</van-button
    >
    <van-overlay :show="showNotice" @click="showNotice = false" />
    <transition name="fadeIn">
      <div class="notice-modal" v-if="showNotice">
        <label>预约须知</label>
        <div class="middle">
          <pre>
      尊敬的患者及家属朋友，为方便您早日就医康复，请您认真阅读预约就诊须知:

      一、预约挂号须知

      1、您可预约2周内的专家号。

      2、节假日后第一天门诊号源于节前最后一个工作日下午15点停止预约。

      二、 预约实名制

      预约就诊采取实名制注册预约，请您如实提供患者的真实姓名、有效身份证证件号、地址、手机号码。（注意事项：预约信息为就诊患者的实名信息）

      三、预约挂号

      1、预约成功后，请于就诊当日携带患者本人身份证等有效证件（医保患者带社保卡)，到济南市槐荫区经七路586号新泉城大厦21楼（国际医学中心）；

      2、预约挂号时线上支付挂号费用；

      3、中心专家号就诊顺序按照预约时间为准，提前10分钟即可，避免就诊排队等待时间过长；

      4、请您保持手机畅通，防止遗漏已经预约成功的专家停诊通知。

      四、取消预约

      中心预约号源有限，预约成功后请您按时就诊。如不能按时就诊，请于24小时之前通过微信小程序或电话进行退号退费。24小时之内退号不退费。

      五、预约方式
      
      我中心预约就诊三种方法：

      1. 微信小程序预约（24小时预约）

      2. 电话预约400-102-5678（09：00-12:00、13:00 -16:30）

      3. 现场预约（周一至周日09：00-12:00、13:00 -16:30），切勿重复预约。

    </pre
          >
        </div>
        <div
          class="modal-confirm-btn"
          @click="showNotice = false"
        >
          <div>我知道了</div>
        </div>
        <!-- <van-button round color="#F3CA4B" @click="showNotice = false"
          >我知道了</van-button
        > -->
      </div>
    </transition>
  </div>
</template>
<script>
import wx from "weixin-js-sdk";
import { listRegistrationBill } from "../api/modules/listRegistrationBill";
import { pay } from "../api/modules/pay";
import { randomString } from "@/utils/common";
import axios from "axios";

// import cloudbase from "@cloudbase/js-sdk";
export default {
  data() {
    return {
      src: "",
      name: "",
      role: "",
      address: "",
      dept: "",
      address: "",
      price: "",
      date: "",
      time: "",
      patientName: "",
      card: "",
      showNotice: true,
    };
  },
  created() {
    let staffId = this.$route.query.staffId;
    this.date = this.$route.query.date;
    this.time = this.$route.query.time;
    let obj = {
      patientId: this.$store.state.patientId,
      staffId: staffId,
    };
    const toast = this.$toast.loading({
      duration: 0, // 持续展示 toast
      forbidClick: true,
    });
    listRegistrationBill(obj).then((res) => {
      this.$toast.clear();
      this.dept = res.data.data.deptName;
      this.name = res.data.data.staffName;
      this.role = res.data.data.title;
      this.price = res.data.data.amount;
      this.patientName = res.data.data.patientName;
      this.card = res.data.data.card;
      this.src = res.data.data.picture;
    });
  },
  methods: {
    // toPay() {
    //   const { skdId, deptId, date: attendanceDate } = this.$route.query;
    //   if (!skdId || !deptId) {
    //     return;
    //   }
    //   const toast = this.$toast.loading({
    //     duration: 0, // 持续展示 toast
    //     forbidClick: true,
    //   });
    //   pay({
    //     skdId,
    //     deptId,
    //     attendanceDate,
    //     name: this.$store.state.patientId,
    //     amount: this.price,
    //     time: this.time,
    //   }).then((res) => {
    //     this.$toast.clear();
    //     if (res.data.data === 1) {
    //       this.$toast.success(res.data.message);
    //       this.$router.replace("/appointment/third");
    //     } else if (res.data.data === 0) {
    //       this.$toast.fail("挂号失败");
    //     } else if (res.data.data === 3) {
    //       this.$toast.fail("同一天同一个医生已经挂过号了");
    //     }
    //   });
    // },
    handlePay() {
      const value = this.price;
      const toast = this.$toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
      });
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
          } else {
            this.$toast.fail(res.data.data.message);
          }
        });
    },
    requestPaySign(id, value, trade_no) {
      const timeStamp = new Date().getTime();
      const nonceStr = randomString(16);
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
      this.$toast.clear();
      try {
        this.$wx.chooseWXPay({
          timestamp,
          nonceStr,
          package: "prepay_id=" + id,
          signType: "MD5",
          paySign,
          success: function (t) {
            const { skdId, deptId, date: attendanceDate } = that.$route.query;
            const toast = that.$toast.loading({
              duration: 0, // 持续展示 toast
              forbidClick: true,
            });
            axios
              .post("/api/registration/programCreateRegistration", {
                skdId,
                deptId,
                attendanceDate,
                patientId: patientid,
                amount: money,
                time: that.time,
                out_trade_no,
              })
              .then((res) => {
                if (res.data.data === 1) {
                  that.$toast.success(res.data.message);
                  that.$router.replace("/appointment/third");
                } else {
                  that.$toast.fail(res.data.message);
                }
              });
          },
        });
      } catch (e) {
        this.$toast.clear();
      }
    },
    test() {
      axios
        .post("/api/registration/WxProgramResults", {
          patientid: 23,
          out_trade_no: "2aCVx2DRUWQnYVYJrsL9iBhBXRfmAl0Q",
          openid: localStorage.openID,
        })
        .then((res) => {
          that.$toast.clear();
        });
    },
  },
};
</script>
<style lang="less">
.fadeIn-enter-active,
.fadeIn-leave-active {
  transition: opacity 0.3s;
}
.fadeIn-enter,
.fadeIn-leave-to {
  opacity: 0;
}
.order-info-page {
  .modal-confirm-btn {
    height: 44px !important;
    line-height: 44px;
    background: @green;
    border-radius: 22px;
    font-size: 14px;
    text-align: center;
    color: #fff;
  }
  .doctor-info {
    padding: 10px 0;
    margin-bottom: 8px;
    display: flex;
    height: 100px;
    text-align: left;
  }
  .doctor-info div:first-child {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  .doctor-info div:last-child {
    width: 90px;
    height: 100px;
    padding-right: 25px;
    align-items: flex-end;
    display: flex;
    justify-content: center;
    align-items: center;
    img {
      max-width: 100%;
      max-height: 100%;
    }
  }
  .doctor-info div span {
    padding-left: 20px;
  }
  .doctor-info div span:first-of-type {
    font-size: 20px;
    font-weight: bold;
  }
  .doctor-info div span:last-of-type {
    font-size: 14px;
  }
  .modal-btn {
    text-align: left;
    display: block;
    padding-left: 15px;
    font-size: 14px;
    h5 {
      margin-bottom: 0;
    }
  }
  .notice-modal-wrap {
    width: 90vw;
    position: fixed;
    top: 50%;
    left: 50%;
    background: #fff;
    z-index: 999;
  }
  .notice-modal {
    text-align: left;
    padding: 20px 15px;
    width: 90vw;
    max-height: 70vh;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translateX(-50%) translateY(-50%);
    box-sizing: border-box;
    background-color: #fff;
    z-index: 1000;
    display: flex;
    flex-direction: column;
    p {
      font-size: 14px;
      text-indent: 1em;
      line-height: 1.5em;
    }
  }
  .notice-modal label {
    color: #f3ca4b;
    font-size: 16px;
    font-weight: bold;
  }
  .notice-modal button {
    float: right;
  }
  .middle {
    border: 1px solid #999999;
    flex: 1;
    margin: 15px 0;
    overflow: auto;
    padding: 5px 10px;
  }
  pre {
    white-space: pre-wrap;
    word-wrap: break-word;
    text-align: left;
    color: #070707;
    font-size: 14px;
  }
}
</style>
