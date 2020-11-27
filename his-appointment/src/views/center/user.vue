<template>
  <div class="user-page">
    <div class="user-picture"><img :src="userInfo.headimgurl" /></div>
    <van-cell-group style="text-align: left">
      <van-cell title="昵称" :value="userInfo.nickname" />
      <van-cell title="绑定手机号" :value="patient.phoneNo" />
      <van-cell title="就诊人姓名" :value="patient.name" />
      <van-cell title="就诊人身份证号" :value="patient.identificationNo" />
      <van-cell
        title="预存金额"
        :value="isNaN(patient.blance) ? '' : patient.blance + '元'"
      />
    </van-cell-group>
    <div class="user-buttons">
      <van-button type="primary" @click="handleEdit">编辑</van-button>
      <!-- <van-button type="primary" @click="cancel">取消绑定</van-button> -->
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      patient: {
        phoneNo: "",
        name: "",
        identificationNo: "",
        blance: "",
      },
    };
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo || JSON.parse(localStorage.APP_USER_INFO || '{}')
    }
  },
  mounted() {
    const toast = this.$toast.loading({
      duration: 0, // 持续展示 toast
      forbidClick: true,
    });
    axios({
      url: "/api/diagnosisPatient/getPatient?openId=" + localStorage.openID,
      method: "GET",
    })
      .then((res) => {
        if (res.data.code === 200) {
          this.patient = res.data.data;
        }
      })
      .finally(() => {
        toast.clear();
      });
  },
  methods: {
    cancel() {
      const that = this;
      this.$dialog.confirm({
        title: "提示",
        message: "是否取消绑定？",
        beforeClose(action, done) {
          if (action === "confirm") {
            let identificationNo = that.patient.identificationNo;
            axios
              .post("/api/staff/bindPatient", {
                identificationNo,
                openId: "",
                picture: "",
              })
              .then((res) => {
                if (res.data.code == 200) {
                  localStorage.removeItem("patientId");
                  that.$store.commit('removePatientId');
                  that.$router.replace('/center');
                } else {
                  // this.$toast.
                }
              })
              .catch(() => {
                done();
              })
              .finally(() => {
                done();
              });
          } else {
            done();
          }
        },
      });
    },
    handleEdit() {
      this.$router.push('/center/edit');
    },
  },
};
</script>

<style lang="less">
.user-page {
  text-align: left;
  .user-picture {
    padding: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    img {
      width: 90px;
      height: 90px;
      border-radius: 50%;
    }
  }
  .user-buttons {
    margin-top: 50px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    .van-button {
      width: 100px !important;
    }
  }
}
</style>