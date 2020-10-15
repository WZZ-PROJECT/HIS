<template>
  <div>
    <!-- <van-nav-bar title="医生介绍" left-arrow @click-left="onReturn" /> -->
    <hr />
    <div>
      <div style="padding: 0 1rem;text-align: left;">
        <van-row>
          <van-col span="6">
            <van-image width="5rem" height="5rem" :src="src" />
          </van-col>
          <van-col span="12" offset="1" style="margin-top:1rem;">
            <van-row span="24">
              <span style="font-size: 1.3rem;font-weight: bold;">{{name}}</span>
              <span style="font-size: 0.8rem;">{{role}}</span>
            </van-row>
            <van-row span="24">
              <span style="font-size: 0.8rem;">{{dept}}</span>
            </van-row>
          </van-col>
          <van-col span="5">
            <van-button
              type="info"
              size="normal"
              class="btn"
              round
              color="#f1c232"
              style="margin-top:1rem;font-size:0.8rem;height: 2rem;"
              @click="attention()"
            > {{att==0?"+关注":"已关注"}}</van-button>
          </van-col>
        </van-row>
      </div>
    </div>
    <hr />
    <div style="text-align: left;padding: 0 0.4rem;">
      <label>医生擅长</label>
      <div style="margin-top:0.5rem;font-size:0.9rem;color: #b0b0b0;">{{advantages}}</div>
    </div>
    <div style="margin-top:0.5rem; text-align: left;padding: 0 0.4rem;">
      <label>医生简介</label>
      <div
        style="margin-top:1rem;margin-top:0.5rem;font-size:0.9rem;color: #b0b0b0;"
      >{{description}}</div>
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
      date: '',
      src: "",
      name: '',
      role: '',
      dept: '',
      advantages: '',
      description: '',
      att:'',
      staffId:this.$route.query.staffId,
      patientId: localStorage.patientId,
    };
  },
  created() {
    
    getStaffList(this.staffId,this.patientId).then((res) => {
      this.name = res.data.data.staffName
      this.role = res.data.data.title
      this.dept = res.data.data.deptName
      this.advantages = res.data.data.advantages
      this.description = res.data.data.description
      this.att = res.data.data.state
      this.src = res.data.data.picture
    });
  },
  methods: {
      onReturn() {
      history.back()
    },
    attention() {
      let obj = {
        staffId: this.staffId,
        patientId: this.patientId,
      };
      attentionStaff(obj).then((res) => {
        if (res.data.data === "关注成功") {
          this.att = '已关注'
          this.$toast('关注成功')
        }
      });
    },

    toFive() {
      this.$router.push("/appointment/five");
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
<style scoped>
</style>
