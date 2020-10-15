<template>
  <div>
    <!-- <van-nav-bar
      :title="$route.query.deptName"
      left-arrow
      @click-left="onReturn"
    /> -->

    <van-cell
      title="选择预约日期"
      :value="date"
      @click="show = true"
      style="text-align: left"
    />
    <van-calendar v-model="show" @confirm="onConfirm" />

    <div>
      <van-row style="padding: 10px 12px">
        <van-col span="12" style="text-align: left">
          <div>{{ date }}坐诊医生</div>
        </van-col>
        <van-col span="4"></van-col>
        <div>
          <van-col span="8">
            <van-switch v-model="checked" size="1rem" @click="fil()" />只看有号
          </van-col>
        </div>
      </van-row>
      <br />

      <div class="footer" v-for="(item1, index) in resultList" :key="index">
        <van-row>
          <van-col span="6">
            <van-image width="80" height="80" :src="item1.picture" />
          </van-col>
          <van-col span="17" offset="1">
            <van-row>
              <van-col span="8" style="font-size: 1.1rem; font-weight: bold">{{
                item1.staffName
              }}</van-col>
              <van-col
                span="8"
                style="font-size: 0.9rem; margin-right: 10rem"
                >{{ item1.title }}</van-col
              >
            </van-row>
            <van-row>
              <van-col span="24">
                <div
                  class="van-multi-ellipsis--l2"
                  style="font-size: 0.9rem; margin-top: 0.4rem"
                >
                  {{ item1.advantages }}
                </div>
              </van-col>
            </van-row>
          </van-col>
        </van-row>
        <van-row
          gutter="10"
          style="font-size: 0.8rem; margin-top: 0.8rem; align-items: center"
          type="flex"
        >
          <van-col span="7" v-if="message"
            >{{ time }}{{ item1.noon == 0 ? "上午" : "下午" }}</van-col
          >

          <van-col span="3">{{ item1.amount }}￥</van-col>
          <van-col span="7"
            >已约{{ item1.skLimit - item1.remain }}/总数{{
              item1.skLimit
            }}</van-col
          >
          <van-col span="7">
            <button
              :disabled="item1.remain == 0 ? true : false"
              :class="item1.remain == 0 ? 'btnGrey' : 'btnOrange'"
              @click="yuyue(item1.noon, item1.skdId)"
            >
              {{ item1.remain == 0 ? "约满" : "预约" }}
            </button>
          </van-col>
        </van-row>
        <van-popup
          v-model="flag"
          position="bottom"
          :overlay="true"
          closeable
          :style="{ height: '80%', width: '100%' }"
        >
          <label style="display: inline-block; padding: 10px 0;text-align: center;width: 100%">选择要预约的时间段</label>
          <br />
          <div style="margin-left: 0.1rem"></div>
          <div style="margin-left: 1rem; text-align: left" v-if="popup1">
            <table class="myTable">
              <van-row>
                <van-col
                  span="8"
                  v-for="(item2, index) in timeArr1"
                  :key="index"
                  style="text-align: center; margin-bottom: 9px"
                >
                  <van-button
                    round
                    color="grey"
                    @click="toFive(item2, item1.staffId)"
                    >{{ item2 }}</van-button
                  >
                </van-col>
              </van-row>
            </table>
          </div>
          <div style="margin-left: 1rem; text-align: left" v-if="popup2">
            <table class="myTable">
              <van-row>
                <van-col
                  span="8"
                  v-for="(item2, index) in timeArr1"
                  :key="index"
                  style="text-align: center; margin-bottom: 9px"
                >
                  <van-button
                    round
                    color="grey"
                    @click="toFive(item2, item1.staffId)"
                    >{{ item2 }}</van-button
                  >
                </van-col>
              </van-row>
            </table>
          </div>
        </van-popup>
      </div>
      <!-- <van-popup v-model="flag" position="bottom" :overlay="true">
        <van-datetime-picker
          type="time"
          :filter="filter"
          @cancel="flag = false"
          @confirm="handleDateConfirm"
        />
      </van-popup> -->
      <div
        class="empty-content"
        v-if="resultList && resultList.length === 0"
      ></div>
    </div>
  </div>
</template>
<script>
import { listNowDoctorRegistration } from "../api/modules/listNowDoctorRegistration";
import { TimeDifference } from "../api/modules/TimeDifference";
import moment from 'moment';

export default {
  components: {},
  data() {
    return {
      timeArr1: [],
      date: moment().format('YYYY-MM-DD'),
      time: moment().format('YYYY-MM-DD'),
      show: false,
      flag: false,
      message: false,
      checked: false,
      popup1: true,
      popup2: false,
      src: "",
      price: "",
      resultList: [],
      qresultList: [],
      staffId: "",
      deptId: "",
      state: 1,
      deptName: this.$route.query.deptName,
      skdId: ''
    };
  },
  created() {
    this.deptId = this.$route.query.deptId;
    this.$route.meta.title = this.$route.query.deptName;
    this.request();
  },
  methods: {
    request() {
      const toast = this.$toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
      });
      listNowDoctorRegistration({
        deptId: this.deptId,
        thedate: this.date,
      }).then((res) => {
        this.qresultList = res.data.data;
        this.resultList = res.data.data;
        this.$toast.clear();
      });
    },
    filter(type, options) {
      if (type === "minute") {
        return options.filter((option) => option % 5 === 0);
      }
      return options;
    },
    fil() {
      this.resultList = [];
      if (this.state === 1) {
        this.qresultList.forEach((item, index) => {
          if (item.remain >= 1) {
            this.resultList.push(item);
          }
        });
        this.state = 2;
        return;
      }
      if (this.state === 2) {
        this.resultList = this.qresultList;
        this.state = 1;
        return;
      }
    },
    onReturn() {
      history.back();
    },
    toFive(time, staffId) {
      this.flag = false;
      this.popup1 = false;
      this.popup2 = false;
      this.$nextTick(() => {
        this.$router.push({
          path: "/appointment/five",
          query: {
            skdId: this.skdId,
            staffId,
            date: this.date,
            time,
            patientId: localStorage.patientId,
            deptId: this.deptId
          },
        });
      });
    },
    yuyue(noon, skdId) {
      this.skdId = skdId;
      if (!this.date) {
        this.$toast('请选择预约日期');
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
        this.timeArr1 = res.data.data;
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
    onConfirm(date) {
      this.show = false;
      this.date = moment(date).format('YYYY-MM-DD');
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
  },
};
</script>
<style scoped>
.footer {
  background-color: rgb(255, 255, 255);
  text-align: left;
  margin: 0 0.6rem;
  border: 2px solid rgb(241, 235, 235);
  padding: 0.7rem 0.5rem 0.7rem 0.2rem;
  margin-bottom: 10px;
}
.btn {
  margin-top: 7rem;
}
.btnOrange {
  border: none;
  outline: none;
  background-color: yellow;
  border-radius: 15px;
  padding: 0.3rem 1rem;
}
.btnGrey {
  border: none;
  outline: none;
  background-color: grey;
  border-radius: 15px;
  padding: 0.3rem 1rem;
}
</style>
