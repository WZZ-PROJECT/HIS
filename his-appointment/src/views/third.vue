<template>
  <div>
    <van-search
      v-model="value"
      placeholder="请输入医生名称"
      show-action
      @search="onSearch"
      @cancel="onCancel"
    />

    <div v-if="firstFlag">
      <div v-if="flag">
        <ul class="search-result-list">
          <template v-for="(item, index) in historyDoctorList">
            <router-link
              tag="li"
              :to="{
                path: '/appointment/patientPage',
                query: {
                  staffId: item.staffId,
                  patientId,
                  deptId: item.deptId,
                },
              }"
              class="list-item"
              :key="index"
            >
              <van-image
                class="list-item-image"
                v-if="item.picture"
                :src="item.picture"
              />
              <img
                class="list-item-image"
                v-else
                src="../assets/img/picture.png"
                alt=""
              />
              <p>
                <span class="van-ellipsis">{{ item.staffName }}</span>
                <span class="van-multi-ellipsis--l2">{{ item.deptName }}</span>
              </p>
            </router-link>
          </template>
          <!-- <li
            v-for="(item, index) in historyDoctorList"
            :key="index"
            @click="toPatientPage(item.staffId, item.deptId)"
          >
            <van-image
              class="list-item-image"
              v-if="item.picture"
              :src="item.picture"
            />
            <img
              class="list-item-image"
              v-else
              src="../assets/img/picture.png"
              alt=""
            />
            <p>
              <span>{{ item.staffName }}</span>
              <span>{{ item.deptName }}</span>
            </p>
          </li> -->
          <li class="flex-empty"></li>
          <li class="flex-empty"></li>
          <li class="flex-empty"></li>
          <li class="flex-empty"></li>
          <li class="flex-empty"></li>
        </ul>
      </div>
      <p class="section-title">选择预约就诊科室</p>
      <ul class="department-list">
        <template v-for="item in deptList">
          <router-link
            tag="li"
            :to="{
              path: '/appointment/fourth',
              query: {
                deptId: item.id,
                patientId,
                deptName: item.name,
              },
            }"
            class="list-item"
            :key="item.id"
          >
            <p>
              <span>{{ item.name }}</span>
            </p>
            <van-icon name="play-circle" color="#786e6e" size="40" />
          </router-link>
        </template>
      </ul>
    </div>
  </div>
</template>
<script>
import { getHistoryDoctorList } from "../api/modules/historyDoctorList";
import { select } from "../api/modules/deptList";

export default {
  data() {
    return {
      src: "",
      deptList: "",
      value: "",
      firstFlag: true,
      flag: false,
      historyDoctorList: "",
      showNotice: true,
    };
  },
  computed: {
    patientId() {
      return this.$store.state.patientId;
    },
  },
  mounted() {
    if (this.patientId) {
      getHistoryDoctorList(this.patientId).then((res) => {
        if (res.data.data != null && res.data.code === 200) {
          this.flag = true;
          this.historyDoctorList = res.data.data;
        } else {
          this.flag = false;
        }
      });
    }
    select().then((res) => {
      this.deptList = res.data.data.filter((i) => i.name.indexOf("测试") < 0);
      // this.deptList = res.data.data;
    });
  },

  methods: {
    handleNoticeClose() {
      this.showNotice = false;
    },
    onReturn() {
      history.back();
    },
    toFourth(deptId, deptName) {
      console.log(deptName);
      this.$router.push({
        path: "/appointment/fourth",
        query: {
          deptId: deptId,
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
          deptId,
        },
      });
    },
    toDeptDetail(id) {
      // this.$router.push({
      //   path: '/appointment/deptDetail'
      // });
    },
  },
};
</script>
<style lang="less" scoped>
.department-list {
  padding: 0 15px;
  margin-top: 10px;
}
.department-list {
  p {
    align-items: center;
  }
  span {
    line-height: 2em !important;
  }
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
  line-height: 1.5em;
  font-size: 16px;
  font-weight: bold;
  /* display: flex;
  align-items: center; */
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
  padding: 0 5px;
  margin-bottom: 10px;
  width: 48%;
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
  height: 85px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.search-result-list li p span {
  display: inline-block;
  width: 100%;
}
.search-result-list li p span:first-child {
  font-size: 16px;
  margin-bottom: 5px;
}
.search-result-list li p span:last-child {
  font-size: 14px;
  line-height: 1.2em;
  height: 2.4em;
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
.footer {
  border: 0.3vw solid gainsboro;
  border-radius: 35px;
}
</style>
