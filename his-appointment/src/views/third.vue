<template>
  <div>
    <!-- <van-nav-bar title="济南市慈光" left-arrow @click-left="onReturn" /> -->
    <!-- <notice v-if="showNotice" @close="handleNoticeClose" /> -->
    <van-search
      v-model="value"
      placeholder="请输入搜索关键词"
      show-action
      @search="onSearch"
      @cancel="onCancel"
    />

    <div v-if="firstFlag">
      <div v-if="flag">
        <ul class="search-result-list">
          <li v-for="(item, index) in historyDoctorList" :key="index" @click="toPatientPage(item.staffId, item.deptId)">
            <van-image class="list-item-image" :src="item.picture"/>
            <p>
              <span>{{ item.staffName }}</span>
              <span>{{ item.deptName }}</span>
            </p>
          </li>
          <li class="flex-empty"></li>
          <li class="flex-empty"></li>
          <li class="flex-empty"></li>
          <li class="flex-empty"></li>
          <li class="flex-empty"></li>
        </ul>
      </div>
      <!-- <label class="label-title">
        <b>选择预约就诊科室</b>
      </label> -->
      <p class="section-title">选择预约就诊科室</p>
      <ul class="department-list">
        <li
          class="list-item"
          v-for="item in deptList"
          :key="item.id"
          @click="toFourth(item.id, item.name)"
        >
          <p>
            <span>{{ item.name }}</span>
            <!-- <span>{{ item.address || "地址" }}</span> -->
          </p>
          <van-icon name="play-circle" color="#786e6e" size="40" />
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
import { getHistoryDoctorList } from "../api/modules/historyDoctorList";
import { select } from "../api/modules/deptList";
import Notice from '@/components/Notice';

export default {
  components: {
    Notice
  },
  data() {
    return {
      src: "",
      deptList: "",
      value: "",
      firstFlag: true,
      flag: false,
      historyDoctorList: "",
      patientId: localStorage.patientId,
      showNotice: true
    };
  },
  created() {
    getHistoryDoctorList(this.patientId).then((res) => {
      if (res.data.data != null) {
        this.flag = true;
        this.historyDoctorList = res.data.data;
      } else {
        this.flag = false;
      }
    });

    select().then((res) => {
      this.deptList = res.data.data
    });
  },

  methods: {
    handleNoticeClose() {
      this.showNotice = false;
    },
    onReturn() {
      history.back();
    },
    toFourth(deptId,deptName) {
      console.log(deptName)
      this.$router.push({
        path: "/appointment/fourth",
        query: {
          deptId: deptId,
          patientId: this.patientId,
          deptName: deptName,
        },
      });
    },
    onCancel() {
      this.value = "";
    },
    onSearch() {
      let name = this.value;
      this.$router.push({
        path: "/appointment/result",
        query: {
          name: this.value,
          name: name,
          patientId: this.patientId,
        },
      });
    },

    toPatientPage(staffId, deptId) {
      this.$router.push({
        path: "/appointment/patientPage",
        query: {
          staffId: staffId,
          patientId: this.patientId,
          deptId
        },
      });
    },
  },
};
</script>
<style scoped>
.department-list {
  padding: 0 15px;
  margin-top: 10px;
}
.list-item {
  box-sizing: border-box;
  padding: 0 12px;
  display: flex;
  margin-bottom: 10px;
  height: 70px;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  border: 1px solid gainsboro;
  background: #fafafa;
  box-shadow: 0 0 10px 10px #fafafa;
}
.list-item p {
  width: 0;
  padding: 5px 0;
  height: 60px;
  flex: 1;
  text-align: left;
  display: flex;
  align-items: center;
}
.list-item p span {
  max-width: 100%;
  overflow: hidden;
}
.list-item p span:first-child {
  display: inline-block;
  height: 50px;
  font-size: 16px;
  font-weight: bold;
  display: flex;
  align-items: center;
}
/* .list-item p span:last-child {
  display: inline-block;
  line-height: 30px;
} */
.search-result-list {
  display: flex;
  padding: 0 12px;
  justify-content: space-between;
  flex-wrap: wrap;
}
.search-result-list li {
  margin-bottom: 10px;
  width: 45%;
  height: 85px;
  border: 1px solid gainsboro;
  border-radius: 10px;
  display: flex;
  overflow: hidden;
}
.search-result-list li p {
  padding-left: 5px;
  width: 0;
  flex: 5;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.search-result-list li p span:first-child {
  font-size: 16px;
  margin-bottom: 5px;
}
.search-result-list li p span:last-child {
  font-size: 14px;
}
.list-item-image {
  flex: 4;
  display: flex;
  align-items: center;
  justify-content: center;
}
.list-item-image img {
  max-width: 100%;
  max-height: 100%;
  width: auto;
  height: auto;
}
.font {
  margin-left: 1rem;
  border: 1px solid gainsboro;
  border-radius: 30px;
}
.footer {
  border: 0.3vw solid gainsboro;
  border-radius: 35px;
}
.btn {
  margin-top: 1rem;
  width: 1.7rem;
  height: 1.7rem;
  border-radius: 50%;
  border: none;
}
.label-title {
  display: flex;
  margin-left: 1rem;
  font-size: 4vw;
}
</style>
