<template>
  <div class="doctor-detail-page">
    <div class="doctor-info-section">
      <div class="doctor-detail">
        <p class="name">{{ name }}</p>
        <p class="role">{{ role }}</p>
        <p class="dept van-multi-ellipsis--l3">擅长{{ specialty }}</p>
      </div>
      <div class="doctor-image">
        <div class="picture" @click="toPatientIntroduce()">
          <img v-if="src" :src="src" :alt="name"/>
          <img v-else src="../assets/img/picture.png" alt="">
        </div>
        <van-button type="primary" @click="attention(att)">
          {{ att == 0 ? "+关注" : "已关注" }}
        </van-button>
      </div>
    </div>
    <div class="doctor-introduce-section">
      <a @click="toPatientIntroduce()" style="color: #f1c232">医生介绍 ></a>
      <div style="color: #adadad" class="van-multi-ellipsis--l3">
        {{ introduce }}
      </div>
    </div>
    <h3>预约挂号</h3>
    <van-row v-for="i in skdDateList" :key="i">
      <van-col
        class="order-list"
        span="12"
        v-for="(item, index) in skdSet[i]"
        :key="index"
      >
        <div class="week-span" style="line-height: 30px">
          <span>{{ i }} {{ item.week }}</span>
        </div>
        <van-button
          round
          :disabled="!!item.remain"
          :class="item.remain === 0 ? 'btnOrange' : 'btnGrey'"
          @click="yuyue(item.noon, item.skdId, i, item)"
          v-if="!item.empty"
        >
          <div>
            <label :class="item.remain === 0 ? 'orange' : 'grey'"
              >{{ item.noon === 0 ? "上午" : "下午" }}
              {{ item.remain === 0 ? "预约" : "约满" }}
              ¥{{ item.amount }}</label
            >
          </div>
        </van-button>
        <van-button disabled round class="btnGrey" v-else>
          <div><label class="grey">{{ item.noon === 0 ? "上午" : "下午" }} 暂无排班</label></div>
        </van-button>
      </van-col>
    </van-row>
    <van-popup
      v-model="flag"
      position="bottom"
      :overlay="true"
      closeable
      :style="{ height: '80%', width: '100%' }"
      @closed="timeArr1 = null"
    >
      <label
        class="popup-title"
        style="
          display: inline-block;
          padding: 10px 0;
          text-align: center;
          width: 100%;
        "
        >选择要预约的时间段</label
      >
      <br />
      <list-wrap :source="timeArr1">
        <div style="text-align: left">
          <table class="myTable" v-if="timeArr1 && timeArr1.length">
            <van-row>
              <van-col
                span="8"
                v-for="(item2, index) in timeArr1"
                :key="index"
                style="text-align: center; margin-bottom: 9px"
              >
                <van-button
                  plain
                  round
                  type="primary"
                  class="time-lable-btn"
                  @click="toFive(item2, staffId)"
                  >{{ item2 }}</van-button
                >
              </van-col>
            </van-row>
          </table>
        </div>
      </list-wrap>
    </van-popup>
  </div>
</template>

<script>
import moment from "moment";
import { getDoctorAndSkd } from "../api/modules/getDoctorAndSkd";
import { attentionStaff } from "../api/modules/attentionStaff";
import { TimeDifference } from "../api/modules/TimeDifference";
import { compareTime } from "@/utils/common";
import wx from "weixin-js-sdk";
import qs from "qs";
import axios from "axios";

export default {
  components: {},
  data() {
    return {
      src: "",
      name: "",
      role: "",
      specialty: "",
      price: "",
      dept: "",
      staffId: this.$route.query.staffId,
      skdList: {},
      skdAmList: [],
      skdPmList: [],
      timeArr1: null,
      flag: false,
      popup1: true,
      popup2: false,
      att: "",
      skdId: "",
      date: "",
      introduce: '',
      skdDateList: [],
      skdSet: [],
      item: {},
    };
  },
  computed: {
    patientId() {
      return this.$store.state.patientId || -1;
    },
  },
  mounted() {
    const toast = this.$toast.loading({
      duration: 0, // 持续展示 toast
      forbidClick: true,
    });
    getDoctorAndSkd(this.staffId, this.patientId)
      .then((res) => {
        this.$toast.clear();
        this.name = res.data.data.staffName;
        this.role = res.data.data.title;
        this.dept = res.data.data.deptName;
        this.specialty = res.data.data.advantages;
        this.introduce = res.data.data.description;
        this.skdList = res.data.data.skdList;
        this.att = res.data.data.state;
        this.src = res.data.data.picture;
        if (
          this.skdList === undefined ||
          this.skdList === null ||
          this.skdList.length === 0
        ) {
          this.$toast("未获取到排班信息！");
          return;
        }
        // for (let i = 0; i < this.skdList.length; i++) {
        //   if (this.skdList[i].noon == 0) {
        //     this.skdAmList.push(this.skdList[i]);
        //   } else {
        //     this.skdPmList.push(this.skdList[i]);
        //   }
        // }
        const dataSet = {};
        for (let i = 0, length = this.skdList.length; i < length; i++) {
          const cur = this.skdList[i];
          const date = cur.date;
          if (dataSet[date]) {
            const type = cur.noon,
              item = dataSet[date][0];
            if (item.noon < type) {
              dataSet[date].push(cur);
            } else {
              dataSet[date].unshift(cur);
            }
          } else {
            dataSet[date] = [cur];
          }
        }
        this.skdDateList = Object.keys(dataSet).sort((a, b) =>
          moment(a).isBefore(moment(b)) ? -1 : 1
        );
        for (let i = 0, length = this.skdDateList.length; i < length; i++) {
          const list = dataSet[this.skdDateList[i]];
          if (list.length < 2) {
            const { date, week, noon } = list[0];
            console.log(week)
            if (noon === 0) {
              list.push({
                date,
                week,
                empty: true,
                noon: 1
              });
            } else {
              list.unshift({
                date,
                week,
                empty: true,
                noon: 0
              });
            }
          }
        }
        this.skdSet = dataSet;
      })
  },
  methods: {
    filter(type, options) {
      if (type === "minute") {
        return options.filter((option) => option % 5 === 0);
      }
      return options;
    },
    onReturn() {
      history.back();
    },
    toFive(time, staffId) {
      const that = this;
      const openid = localStorage.openID;
      if (!openid) {
        const that = this;
        this.$dialog.alert({
          title: "提示",
          message: "尚未获取微信授权，请重新登录",
        });
        return;
      }
      const patientId = this.patientId;
      if (patientId === -1) {
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
      const query = {
        skdId: this.skdId,
        staffId: this.staffId,
        deptId: this.$route.query.deptId,
        date: this.date,
        time,
        patientId,
        urlfrom: 'detail'
      };
      const toast = this.$toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
      });
      axios
        .post("/api/registration/queryCreateRegistration", {
          skdId: parseInt(query.skdId),
          attendanceDate: query.date,
          patientId: query.patientId,
        })
        .then((res) => {
          if (res.data.data === 4) {
            this.$toast.clear();
            this.flag = false;
            this.popup1 = false;
            this.popup2 = false;
            if (process.env.NODE_ENV !== "development") {
              try {
                wx.miniProgram.getEnv(function (res) {
                  console.log(res);
                  //获取当前环境
                  if (res.miniprogram) {
                    // 在微信小程序中
                    wx.miniProgram.navigateTo({
                      url: "/pages/order/order?" + qs.stringify(query),
                    });
                  } else {
                    that.$nextTick(() => {
                      that.$router.push({
                        path: "/appointment/five",
                        query,
                      });
                    });
                  }
                });
              } catch (e) {
                this.$nextTick(() => {
                  this.$router.push({
                    path: "/appointment/five",
                    query,
                  });
                });
              }
            } else {
              // this.$nextTick(() => {
              //   this.$router.push({
              //     path: "/appointment/five",
              //     query,
              //   });
              // });
              // wx.miniProgram.navigateTo({
              //   url: "/pages/order/order?" + qs.stringify(query),
              // });
            }
          } else {
            this.$toast.fail(res.data.message);
          }
        });
    },
    yuyue(noon, skdId, ruletime, item) {
      this.item = item;
      this.flag = true;
      this.date = ruletime;
      this.skdId = skdId;
      TimeDifference(skdId, ruletime, noon).then((res) => {
        if (ruletime === moment().format("YYYY-MM-DD")) {
          const now = new Date().getTime();
          this.timeArr1 = res.data.data.filter((item) => {
            const [h, m] = item.split(":");
            return compareTime(h, m, now);
            // return true
          });
        } else {
          this.timeArr1 = res.data.data || [];
        }
      });
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

    toPatientIntroduce() {
      this.$router.push({
        path: "/appointment/patientIntroduce",
        query: {
          staffId: this.staffId,
          patientId: this.patientId,
        },
      });
    },
    handleDateConfirm() {
      this.popup1 = false;
      this.popup2 = false;
      this.$nextTick(() => {
        this.$router.push("/appointment/five");
      });
    },
  },
};
</script>

<style lang="less">
.doctor-detail-page {
  text-align: left;
  .time-lable-btn {
    height: 32px !important;
    border-width: 2px !important;
  }
  .doctor-info-section {
    padding: 20px 15px 0;
    height: 135px;
    display: flex;
    .van-button {
      color: #fff !important;
      height: 28px !important;
    }
    .doctor-detail {
      flex: 1;
      .name {
        font-size: 22px;
        font-weight: bold;
        color: #272727;
      }
      .role, .dept {
        margin-top: 5px;
        font-size: 14px;
        color: #9A9A9A;
      }
      .dept {
        height: 4.5em;
        line-height: 1.5em;
      }
    }
    .doctor-image {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      .picture {
        width: 80px;
        height: 90px;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-bottom: 15px;
        img {
          max-width: 100%;
          max-height: 100%;
        }
      }
    }
  }
  .doctor-introduce-section {
    padding: 0 15px;
    font-size: 16px;
    a {
      display: inline-block;
      margin-bottom: 10px;
    }
    div {
      line-height: 1.5em;
      height: 4.5em;
      text-indent: 2em;
      font-size: 14px;
    }
  }
  h3 {
    margin: 12px 0;
    padding: 0 15px;
    font-size: 16px;
  }
  td button {
    width: 63%;
  }
  .btnOrange {
    border: 2px solid @green;
  }
  .btnGrey {
    border: 2px solid grey;
  }
  .orange {
    color: @green;
  }
  .grey {
    color: grey;
  }
  .myTable {
    width: 100%;
  }
  .order-list {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .van-button {
      padding: 0 !important;
      width: 135px;
      height: 36px !important;
    }
    .week-span {
      font-size: 14px;
      font-weight: bold;
    }
  }
  .popup-title {
    font-size: 16px;
  }
}
</style>
