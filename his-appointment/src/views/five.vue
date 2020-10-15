<template>
  <div class="order-info-page">
    <!-- <van-nav-bar title="请确认预约信息" left-arrow @click-left="$router.go(-1)" /> -->

    <hr />
    <div class="font">
      <van-row>
        <van-col span="8" style="text-align: left">
          <br />
          <span style="font-size: 1.2rem; font-weight: bold">{{ name }}</span>
          <br />
          <span style="display: inline-block; margin-top: 0.2rem">{{
            role
          }}</span>
        </van-col>
        <van-col span="8">
          <van-image
            width="6rem"
            height="6rem"
            :src="src"
            style="margin-left: 8rem"
          />
        </van-col>
      </van-row>
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
    <a href="javascript:;" @click="showNotice = true">
      <h5
        style="
          text-align: left;
          color: #b61717;
          margin-left: 1rem;
          margin-bottom: 0.5rem;
        "
      >
        预约即同意 《预约须知》
      </h5>
    </a>
    <hr />
    <van-button type="primary" @click="toPay()" block round
      >确认预约并支付</van-button
    >
    <van-overlay :show="showNotice" @click="showNotice = false" />
    <transition name="fadeIn">
      <div class="notice-modal" v-if="showNotice">
        <label>预约须知</label>
        <div class="middle">
          <p>☆请在预约时间之前携带身份证完成就诊卡/医保卡实名认证后取号</p>
          <p>☆如需取消预约，请在就诊日前一日16点30分之前操作</p>
        </div>
        <van-button round color="#F3CA4B" @click="showNotice = false"
          >我知道了</van-button
        >
      </div>
    </transition>
  </div>
</template>
<script>
import wx from "weixin-js-sdk";
import { listRegistrationBill } from "../api/modules/listRegistrationBill";
import { pay } from "../api/modules/pay";

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
      showNotice: false,
    };
  },
  created() {
    let staffId = this.$route.query.staffId;
    this.date = this.$route.query.date;
    this.time = this.$route.query.time;
    let obj = {
      patientId: localStorage.patientId,
      staffId: staffId,
    };

    listRegistrationBill(obj).then((res) => {
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
    toPay() {
      const { skdId, deptId, date: attendanceDate } = this.$route.query;
      if (!skdId || !deptId) {
        return;
      }
      const toast = this.$toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
      });
      pay({
        skdId,
        deptId,
        attendanceDate,
        name: localStorage.patientId,
        amount: this.price,
        time: this.time,
      }).then((res) => {
        this.$toast.clear();
        if (res.data.data === 1) {
          this.$toast.success(res.data.message);
          this.$router.replace("/appointment/third");
        } else {
          this.$toast.fail(res.data.message);
        }
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
  .font {
    margin-left: 1rem;
  }
  .footer {
    background-color: #b0b0b0;
    text-align: left;
    padding-bottom: 2rem;
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
    padding: 1rem;
    width: 90vw;
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
    /* height: 30rem; */
    margin-top: 0.5rem;
    margin-bottom: 0.5rem;
    overflow: auto;
    padding: 0.5rem;
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
