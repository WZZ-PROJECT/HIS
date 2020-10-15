<template>
  <div>
    <!-- <van-nav-bar
      title="医生主页"
      left-arrow
      style="height: 46px; margin-top: 0.5rem"
      @click-left="onReturn"
    /> -->
    <div class="footer">
      <van-row type="flex" justify="space-between">
        <van-col span="10">
          <div style="margin-left: 1rem">
            <div style="margin-top: 1rem">
              <span style="font-size: 1.3rem; font-weight: bold">{{
                name
              }}</span>
              <span style="font-size: 0.8rem; color: #adadad">{{ role }}</span>
            </div>
            <div style="font-size: 0.8rem; color: #adadad">{{ dept }}</div>
          </div>
        </van-col>
        <van-col span="6" style="margin-left: 5rem">
          <div style="margin-top: 0.3rem">
            <van-image width="4.5rem" height="4.5rem" :src="src" />
          </div>
        </van-col>
      </van-row>
      <van-row
        type="flex"
        justify="space-between"
        style="padding: 0.3rem 1rem 0"
      >
        <van-col span="6">
          <a
            @click="toPatientIntroduce()"
            style="color: #f1c232; font-size: 0.8rem"
            >医生介绍</a
          >
        </van-col>
        <van-col span="6" style="text-align: center">
          <button
            style="
              background-color: #f1c232;
              font-size: 0.8rem;
              border: none;
              outlin: none;
              padding: 0.3rem 1rem;
              border-radius: 16px;
              color: #fff;
            "
            @click="attention(att)"
          >
            {{ att == 0 ? "+关注" : "已关注" }}
          </button>
        </van-col>
      </van-row>
      <van-row>
        <div
          class="van-ellipsis"
          style="
            font-size: 0.8rem;
            margin: 0.3rem 1rem 0.3rem 1rem;
            color: #adadad;
          "
        >
          {{ specialty }}
        </div>
      </van-row>
    </div>
    <hr />
    <div style="margin-left: 1rem; text-align: left">
      <label style="font-size: 1.5rem">预约挂号</label>
    </div>

    <van-row>
      <van-col span="12">
        <div
          style="margin-left: 1rem"
          v-for="(item, index) in skdAmList"
          :key="index"
        >
          <div style="font-size: 0.8rem; line-height: 30px">
            <b>{{ item.date }} {{ item.week }}</b>
          </div>
          <van-button
            round
            :disabled="item.remain == 0 ? false : true"
            :class="item.remain == 0 ? 'btnOrange' : 'btnGrey'"
            @click="yuyue(item.noon, item.skdId, item.date)"
          >
            <div>
              <label :class="item.remain == '0' ? 'orange' : 'grey'"
                >{{ item.noon == 0 ? "上午" : "下午" }}
                {{ item.remain == 0 ? "预约" : "约满" }}
                {{ item.amount }}¥</label
              >
            </div>
          </van-button>
        </div>
      </van-col>

      <van-col span="12">
        <div
          style="margin-left: 1rem"
          v-for="(item, index) in skdPmList"
          :key="index"
        >
          <div style="font-size: 0.8rem; line-height: 30px">
            <b>{{ item.date }} {{ item.week }}</b>
          </div>
          <van-button
            round
            :disabled="item.remain == 0 ? false : true"
            :class="item.remain == 0 ? 'btnOrange' : 'btnGrey'"
            @click="yuyue(item.noon, item.skdId, item.date)"
          >
            <div>
              <label :class="item.remain == '0' ? 'orange' : 'grey'"
                >{{ item.noon == 0 ? "上午" : "下午" }}
                {{ item.remain == 0 ? "预约" : "约满" }}
                {{ item.amount }}¥</label
              >
            </div>
          </van-button>

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
            <div style="text-align: left" v-if="popup1">
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
                      @click="toFive(item2, item.date)"
                      >{{ item2 }}</van-button
                    >
                  </van-col>
                </van-row>
              </table>
            </div>
            <div style="text-align: left" v-if="popup2">
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
                      @click="toFive(item2, item.date)"
                      >{{ item2 }}</van-button
                    >
                  </van-col>
                </van-row>
              </table>
            </div>
          </van-popup>
          <!-- <van-popup v-model="popup1" position="bottom" :overlay="true">
            <van-datetime-picker
              type="time"
              :filter="filter"
              @cancel="popup1 = false"
              @confirm="handleDateConfirm"
            />
          </van-popup>
          <van-popup v-model="popup2" position="bottom" :overlay="true">
            <van-datetime-picker
              type="time"
              :filter="filter"
              @cancel="popup2 = false"
              @confirm="handleDateConfirm"
            />
          </van-popup> -->
        </div>
      </van-col>
    </van-row>
  </div>
</template>
<script>
import { getDoctorAndSkd } from "../api/modules/getDoctorAndSkd";
import { attentionStaff } from "../api/modules/attentionStaff";
import { TimeDifference } from "../api/modules/TimeDifference";

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
      patientId: localStorage.patientId,
      skdList: {},
      skdAmList: [],
      skdPmList: [],
      timeArr1: [],
      flag: false,
      popup1: true,
      popup2: false,
      att: "",
      skdId: '',
      date: ''
    };
  },
  created() {
    getDoctorAndSkd(this.staffId, this.patientId).then((res) => {
      console.log(res.data.data);

      this.name = res.data.data.staffName;
      this.role = res.data.data.title;
      this.dept = res.data.data.deptName;
      this.specialty = res.data.data.advantages;
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
      for (let i = 0; i < this.skdList.length; i++) {
        if (this.skdList[i].noon == 0) {
          this.skdAmList.push(this.skdList[i]);
        } else {
          this.skdPmList.push(this.skdList[i]);
        }
      }
    });
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
    toFive(time, date) {
      this.$router.push({
        path: "/appointment/five",
        query: {
          skdId: this.skdId,
          staffId: this.staffId,
          patientId: this.patientId,
          time: time,
          date: this.date,
          deptId: this.$route.query.deptId
        },
      });
    },
    yuyue(noon, skdId, ruletime) {
      this.date = ruletime;
      this.flag = true;
      this.skdId = skdId;
      if (noon == 1) {
        this.popup1 = false;
        this.popup2 = true;
      } else {
        this.popup1 = true;
        this.popup2 = false;
      }
      TimeDifference(skdId, ruletime, noon).then((res) => {
        this.timeArr1 = res.data.data;
      });
    },
    attention(state) {
      let obj = {
        staffId: this.staffId,
        patientId: this.patientId,
      };
      attentionStaff(obj).then((res) => {
        if (res.data.data === "关注成功") {
          this.att = "已关注";
          this.$toast('关注成功')
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
<style scoped>
.footer {
  text-align: left;
}
td button {
  width: 63%;
}
.btnOrange {
  border: 0.12rem solid orange;
}
.btnGrey {
  border: 0.12rem solid grey;
}
.orange {
  color: orange;
}
.grey {
  color: grey;
}
.myTable {
  width: 100%;
}
</style>
