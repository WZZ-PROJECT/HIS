<template>
  <div class="introduce-page">
    <div class="doctor-info">
      <div class="doctor-picture">
        <img v-if="src" :src="src" :alt="name"/>
        <img v-else src="../assets/img/picture.png" alt="">
      </div>
      <div class="doctor-detail">
        <p class="name van-ellipsis">
          {{ name }}
        </p>
        <p class="role">{{ role }}</p>
      </div>
      <van-button
        type="primary"
        size="normal"
        class="btn"
        @click="attention(att)"
      >
        {{ att == 0 ? "+关注" : "已关注" }}</van-button
      >
    </div>
    <div class="introduce-section" style="text-align: left">
      <label>医生擅长</label>
      <div style="color: #b0b0b0">{{ advantages }}</div>
    </div>
    <div class="introduce-section" style="text-align: left">
      <label>医生简介</label>
      <div style="color: #b0b0b0">{{ description }}</div>
    </div>
  </div>
</template>
<script>
import { getStaffList } from "../api/modules/selectStaffById";
import { attentionStaff } from "../api/modules/attentionStaff";
export default {
  components: {},
  data() {
    return {
      date: "",
      src: "",
      name: "",
      role: "",
      dept: "",
      advantages: "",
      description: "",
      att: "",
      staffId: this.$route.query.staffId,
    };
  },
  computed: {
    patientId() {
      return this.$store.state.patientId || -1;
    },
  },
  created() {
    const toast = this.$toast.loading({
      duration: 0, // 持续展示 toast
      forbidClick: true,
    });
    getStaffList(this.staffId, this.patientId).then((res) => {
      this.name = res.data.data.staffName;
      this.role = res.data.data.title;
      this.dept = res.data.data.deptName;
      this.advantages = res.data.data.advantages;
      this.description = res.data.data.description;
      this.att = res.data.data.state;
      this.src = res.data.data.picture;
    })
      .finally(() => {
        this.$toast.clear();
      });
  },
  methods: {
    onReturn() {
      history.back();
    },
    attention(state) {
      if (state !== 0) return;
      const openid = localStorage.openID;
      if (!openid) {
        const that = this;
        this.$dialog.alert({
          title: "提示",
          message: "尚未获取微信授权，请重新登录",
        });
        return;
      }
      if (!this.$store.state.patientId) {
        const that = this;
        this.$dialog.confirm({
          title: "提示",
          message: "尚未绑定身份信息，是否绑定？",
          beforeClose(action, done) {
            if (action === "confirm") {
              done();
              that.$router.replace({
                path: "/login",
                query: {
                  redirect: encodeURIComponent(that.$route.fullPath),
                },
              });
            } else {
              done();
            }
          },
        });
        return;
      }
      let obj = {
        staffId: this.staffId,
        patientId: this.patientId,
      };
      attentionStaff(obj).then((res) => {
        if (res.data.data === "关注成功") {
          this.att = "已关注";
          this.$toast("关注成功");
        }
      });
    },
    formatDate(date) {
      return `${date.getMonth() + 1}月${date.getDate()}日`;
    },
    onConfirm(date) {
      this.show = false;
      this.date = this.formatDate(date);
      this.message = true;
    },
  },
};
</script>
<style lang="less">
.introduce-page {
  padding: 0 15px;
  .van-button {
    height: 32px;
  }
  .doctor-info {
    height: 115px;
    padding: 20px 0;
    display: flex;
    align-items: center;
    text-align: left;
  }
  .doctor-picture {
    width: 100px;
    height: 115px;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    border-radius: 5px;
    img {
      max-width: 100%;
      max-height: 100%;
    }
  }
  .doctor-detail {
    flex: 1;
    padding-left: 10px;
    width: 0;
    height: 75px;
    .name {
      font-size: 20px;
      font-weight: bold;
      color: #181818;
      line-height: 1em;
    }
    .role {
      margin-top: 8px;
      font-size: 16px;
      font-weight: normal;
      color: #646464;
    }
  }
  .introduce-section {
    font-size: 14px;
    label {
      font-size: 16px;
      color: #434343;
      font-weight: bold;
    }
    div {
      padding: 10px 0;
      text-indent: 2em;
      line-height: 1.5em;
    }
  }
}
</style>
