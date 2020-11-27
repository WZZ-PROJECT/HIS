<template>
  <div>
    <van-nav-bar
      fixed
      :placeholder="true"
      left-arrow
      title="用户绑定"
      @click-left="$router.go(-1)"
    />
    <van-form @submit="onSubmit">
      <van-field
        v-model="idcard"
        name="idcard"
        label="身份证号码："
        placeholder="身份证号码"
        :rules="[{ pattern: handleIdCard, message: '请填写正确的身份证号码' }]"
      />
      <van-field
        v-model="username"
        name="username"
        label="姓名"
        placeholder="姓名"
        :rules="[{ required: true, message: '请填写姓名' }]"
      />
      <van-field
        v-model="address"
        name="address"
        label="地址"
        placeholder="地址"
        :rules="[{ required: true, message: '请填写地址' }]"
      />
      <van-field
        v-model="phone"
        name="phone"
        label="手机号"
        placeholder="手机号"
        :rules="[{ pattern: handlePhone, message: '请填写正确的手机号' }]"
      />
      <div style="margin: 16px">
        <van-button round block type="primary" native-type="submit">
          注册
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      idcard: "",
      username: "",
      address: "",
      phone: "",
      handleIdCard: /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/,
      handlePhone: /^1[3-9]\d{9}$/,
    };
  },
  methods: {
    onSubmit(values) {
      const { idcard, username, address, phone } = this;
      let identificationNo = idcard;
      let birth =
        identificationNo.substring(6, 10) +
        "-" +
        identificationNo.substring(10, 12) +
        "-" +
        identificationNo.substring(12, 14);
      let result = identificationNo.substring(16, 17);
      let gender = "";
      if (result % 2 != 0) {
        gender = "0";
      } else {
        gender = "1";
      }
      this.$toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
      });
      axios.post("/api/pmsPatient/insertBindPmsPatient", {
        identificationNo: identificationNo,
        openId: localStorage.openID,
        picture: "",
        name: username,
        homeAddress: address,
        phoneNo: phone,
        dateOfBirth: birth,
        gender: gender,
      }).then(res => {
        if (res.data.code === 200 && res.data.data) {
          this.$store.commit('setPatientId', res.data.data.id);
          this.$router.replace('/appointment/third');
        } else {
          this.$toast.fail(res.data.message)
        }
      })
      .finally(() => {
        this.$toast.clear();
      });
    },
  },
};
</script>

<style>
</style>