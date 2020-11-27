<template>
  <div class="dept-page">
    <!-- <van-swipe class="date-picker" :loop="false" :show-indicators="false">
      <van-swipe-item>
        <ul>
          <li
            v-for="(i, index) in dateArr.slice(0, 7)"
            :key="index"
            @click="onConfirm(i.date)"
          >
            <span>{{ i.today ? "今日" : i.day }}</span>
            <span :class="{ active: i.date === date }">{{ i.dd }}</span>
          </li>
        </ul>
      </van-swipe-item>
      <van-swipe-item>
        <ul>
          <li
            v-for="(i, index) in dateArr.slice(7)"
            :key="index"
            @click="onConfirm(i.date)"
          >
            <span>{{ i.today ? "今日" : i.day }}</span>
            <span :class="{ active: i.date === date }">{{ i.dd }}</span>
          </li>
        </ul>
      </van-swipe-item>
    </van-swipe> -->
    <vuescroll :ops="options" ref="scroller">
      <ul class="date-picker">
        <li
          v-for="(i, index) in dateArr"
          :key="index"
          @click="onConfirm(i.date, index)"
        >
          <span>{{ i.today ? "今日" : i.day }}</span>
          <span :class="{ active: i.date === date }">{{ i.dd }}</span>
        </li>
      </ul>
    </vuescroll>
    <div style="text-align: left">
      <div class="date-arrangement">
        <span>{{ date }}坐诊医生</span>
        <div></div>
        <div>
          <van-switch v-model="checked" size="18px" />
          <span>只看有号</span>
        </div>
      </div>
      <list-wrap :source="resultList" description="暂无排班信息">
        <div class="doctor-arrangement-list">
          <ul>
            <li v-for="(item1, index) in resultList" :key="index">
              <doctor-card :info="item1" @click.native="handleDetail(item1.staffId)"/>
              <!-- <div class="doctor-info" @click="handleDetail(item1.staffId)">
                <div class="doctor-picture">
                  <img
                    v-if="item1.picture"
                    :src="item1.picture"
                    :alt="item1.staffName"
                  />
                  <img v-else src="../assets/img/picture.png" alt="" />
                </div>
                <div class="doctor-detail">
                  <p class="name van-ellipsis">
                    {{ item1.staffName }} <span>{{ item1.title }}</span>
                  </p>
                  <p class="introduce van-multi-ellipsis--l2">
                    {{ item1.advantages }}
                  </p>
                </div>
              </div> -->
              <div class="doctor-arrangement" v-if="item1.type !== 'doctor'">
                <span class="time">
                  {{ item1.noon == 0 ? "上午" : "下午" }}
                </span>
                <span class="fee">¥{{ item1.amount }}</span>
                <span class="count">
                  已约 <span>{{ item1.skLimit - item1.remain }}</span> /
                  总数<span>{{ item1.skLimit }}</span>
                </span>
                <van-button
                  :disabled="item1.remain === 0 ? true : false"
                  :color="item1.remain === 0 ? 'grey' : '#21bb9d'"
                  @click="yuyue(item1.noon, item1.skdId, item1.staffId)"
                >
                  {{ item1.remain === 0 ? "约满" : "预约" }}
                </van-button>
              </div>
              <div class="doctor-arrangement" v-else>
                <span class="fee"></span>
                <van-button disabled color="grey">暂无排班</van-button>
              </div>
            </li>
          </ul>
        </div>
      </list-wrap>
    </div>
    <van-popup
      v-model="flag"
      position="bottom"
      :overlay="true"
      closeable
      :style="{ height: '80%', width: '100%' }"
      @closed="timeArr1 = null"
    >
      <label
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
          <van-row>
            <van-col
              span="8"
              v-for="(item2, index) in timeArr1"
              :key="index"
              style="text-align: center; margin-bottom: 9px"
            >
              <van-button class="time-lable-btn" type="primary" plain round @click="toFive(item2, staffId)">{{
                item2
              }}</van-button>
            </van-col>
          </van-row>
        </div>
      </list-wrap>
    </van-popup>
  </div>
</template>
<script>
// import { Swipe, SwipeItem } from "vant";
import vuescroll from 'vuescroll';
import { listNowDoctorRegistration } from "../api/modules/listNowDoctorRegistration";
import { TimeDifference } from "../api/modules/TimeDifference";
import { compareTime } from "@/utils/common";
import DoctorCard from '@/components/DoctorCard'
import moment from "moment";
import wx from "weixin-js-sdk";
import qs from "qs";
import axios from "axios";

export default {
  components: {
    // Swipe,
    // SwipeItem,
    vuescroll,
    DoctorCard
  },
  data() {
    return {
      options: {
        vuescroll: {
          mode: 'slide'
        },
        scrollPanel: {
          scrollingY: false,
          speed: 500
        },
        bar: {
          disable: true
        }
      },
      timeArr1: null,
      today: moment().format("YYYY-MM-DD"),
      date: '',
      time: moment().format("YYYY-MM-DD"),
      show: false,
      flag: false,
      message: false,
      checked: false,
      popup1: true,
      popup2: false,
      src: "",
      price: "",
      resultList: [],
      backList: [],
      staffId: "",
      deptId: "",
      state: 1,
      deptName: this.$route.query.deptName,
      skdId: "",
      moment,
      dateArr: [],
    };
  },
  computed: {
    patientId() {
      return this.$store.state.patientId;
    },
  },
  created() {
    const { deptId, date } = this.$route.query;
    this.deptId = deptId;
    this.date = date ? date : this.today;
    // this.$route.meta.title = this.$route.query.deptName;
    this.request();
  },
  mounted() {
    function getWeek(date) {
      // 参数时间戳
      let week = moment(date).day();
      switch (week) {
        case 1:
          return "周一";
        case 2:
          return "周二";
        case 3:
          return "周三";
        case 4:
          return "周四";
        case 5:
          return "周五";
        case 6:
          return "周六";
        case 0:
          return "周日";
      }
    }
    const dateArr = this.dateArr;
    const now = moment().startOf('day');
    dateArr.push({
      date: now.format("YYYY-MM-DD"),
      dd: now.format("D"),
      day: getWeek(now),
      today: true,
    });
    for (let i = 1; i <= 13; i++) {
      const date = moment(now).add(i, "d");
      const obj = {
        date: date.format("YYYY-MM-DD"),
        dd: date.format("D"),
        day: getWeek(date),
      };
      dateArr.push(obj);
    }
    if (this.$route.query.date) {
      this.$nextTick(() => {
        const len = Math.ceil(Math.abs(now.diff(moment(this.date), 'days', true)));
        this.handleScrollChange(len);
      })
    }
    // document.addEventListener("visibilitychange", function () {
    //   if (document.visibilityState == "visible") {
    //       console.log('visible');
    //   }
    //   if (document.visibilityState == "hidden") {
    //       console.log('hidden');
    //     }
    //   })
  },
  methods: {
    request() {
      this.resultList = null;
      listNowDoctorRegistration({
        deptId: this.deptId,
        thedate: this.date,
      }).then((res) => {
        const list = [].concat(
          res.data.data.dmsNowDoctorRegistrationResultList
        );
        const staffId = list.map((i) => i.staffId);
        for (let i = 0, len = res.data.data.stafflist.length; i < len; i++) {
          const cur = res.data.data.stafflist[i];
          if (staffId.indexOf(cur.id) < 0) {
            list.push({
              type: "doctor",
              staffId: cur.id,
              picture: cur.picture,
              staffName: cur.name,
              title: cur.title,
              advantages: cur.advantages,
            });
          }
        }
        this.resultList = list;
        this.backList = list.slice();
      });
    },
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
      const openid = localStorage.openID;
      const that = this;
      if (!openid) {
        const that = this;
        this.$dialog.alert({
          title: "提示",
          message: "尚未获取微信授权，请重新登录",
        });
        return;
      }
      const patientId = this.patientId;
      if (!patientId) {
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
        staffId,
        date: this.date,
        time,
        patientId,
        deptId: this.deptId,
        urlfrom: 'fourth'
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
                    this.$nextTick(() => {
                      this.$router.push({
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
              wx.miniProgram.navigateTo({
                url: "/pages/order/order?" + qs.stringify(query),
              });
            }
          } else {
            this.$toast.fail(res.data.message);
          }
        });
    },
    yuyue(noon, skdId, staffId) {
      this.staffId = staffId;
      this.skdId = skdId;
      if (!this.date) {
        this.$toast("请选择预约日期");
        return;
      }
      this.flag = true;
      if (noon == 0) {
        this.popup1 = false;
        this.popup2 = true;
      } else {
        this.popup1 = true;
        this.popup2 = false;
      }
      let ruletime = this.date;
      TimeDifference(skdId, ruletime, noon).then((res) => {
        if (res.data.code === 200) {
          if (ruletime === moment().format("YYYY-MM-DD")) {
            const now = new Date().getTime();
            this.timeArr1 = res.data.data.filter((item) => {
              const [h, m] = item.split(":");
              return compareTime(h, m, now);
            });
          } else {
            this.timeArr1 = res.data.data || [];
          }
        }
      });
    },
    handleDateConfirm() {
      this.flag = false;
      this.popup1 = false;
      this.popup2 = false;
      this.$nextTick(() => {
        this.$router.push("/appointment/five");
      });
    },
    formatDate(date) {
      return `${date.getMonth() + 1}月${date.getDate()}日`;
    },
    handleScrollChange(index) {
      this.$refs.scroller.scrollTo(
        {
          x: 100 / 7 * index + '%'
        },
        350
      );
    },
    onConfirm(date, index) {
      this.handleScrollChange(index);
      this.show = false;
      this.date = date;
      this.message = true;
      this.time = this.date;
      this.request();
      // listNowDoctorRegistration({
      //   deptId: this.deptId,
      //   thedate: this.date
      // }).then((res) => {
      //   this.qresultList = res.data.data;
      //   this.resultList = res.data.data;
      // });
    },
    getWeek(date) {
      // 参数时间戳
      let week = moment(date).day();
      switch (week) {
        case 1:
          return "周一";
        case 2:
          return "周二";
        case 3:
          return "周三";
        case 4:
          return "周四";
        case 5:
          return "周五";
        case 6:
          return "周六";
        case 0:
          return "周日";
      }
    },
    handleDetail(staffId) {
      this.$router.push({
        path: "/appointment/patientPage",
        query: {
          staffId,
        },
      });
    },
  },
  watch: {
    checked(val) {
      if (val) {
        this.resultList = this.backList.filter(
          (item) => item.remain >= 1 && item.type !== "doctor"
        );
      } else {
        this.resultList = this.backList.slice();
      }
    },
  },
};
</script>
<style lang="less">
.dept-page {
  .time-lable-btn {
    height: 32px !important;
    border-width: 2px !important;
  }
  .van-tabs__wrap {
    position: relative;
    z-index: 99;
  }
  .van-tabs__line {
    width: 60px !important;
    background-color: @blue !important;
  }
  .van-swipe {
    overflow: visible !important;
    overflow-x: hidden;
  }
  .footer {
    background-color: rgb(255, 255, 255);
    text-align: left;
    border: 2px solid rgb(241, 235, 235);
    margin-bottom: 10px;
  }
  .btnOrange {
    border: none;
    outline: none;
    background-color: yellow;
    border-radius: 15px;
  }
  .btnGrey {
    border: none;
    outline: none;
    background-color: grey;
    border-radius: 15px;
  }
  .date-picker {
    padding-bottom: 10px;
    // box-shadow: 0 3px 10px #f3f3f3;
    display: flex;
    width: 750px;
    li {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: center;
      span {
        &:first-child {
          padding: 8px 0;
          color: #999;
          font-size: 14px;
        }
        &:last-child {
          color: #313131;
          font-size: 20px;
          font-weight: bold;
          border-radius: 50%;
          width: 40px;
          height: 40px;
          line-height: 40px;
          background: transparent;
        }
        &.active {
          background: @green;
          color: #fff;
          box-shadow: 0 0 10px @green;
        }
      }
    }
  }
  .date-arrangement {
    padding: 5px 10px 10px;
    display: flex;
    align-items: center;
    font-size: 14px;
    color: #303030;
    font-weight: bold;
    div:first-of-type {
      flex: 1;
    }
    div:last-child {
      display: flex;
      align-items: center;
      span {
        margin-left: 5px;
      }
    }
  }
  .doctor-arrangement-list {
    margin-top: 5px;
    padding: 0 12px;
    li {
      padding: 10px;
      border-radius: 8px;
      margin-bottom: 10px;
      box-shadow: 0 0 10px #ededed;
    }
    .doctor-arrangement {
      display: flex;
      height: 30px;
      line-height: 30px;
      font-size: 14px;
      text-align: center;
      .time {
        display: inline-block;
        width: 60px;
        color: #181818;
        font-weight: bold;
      }
      .fee {
        flex: 1;
        color: orange;
        font-weight: bold;
      }
      .count {
        flex: 3;
      }
      .van-button {
        height: 30px !important;
      }
    }
  }
}
</style>
