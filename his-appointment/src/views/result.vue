<template>
  <div class="search-result-page">
    <div>
      <!-- <van-nav-bar title="搜索" left-arrow @click-left="onReturn" /> -->
      <form action="/">
        <van-search
          v-model="value"
          placeholder="请输入医生名称"
          show-action
          @search="onSearch"
          @cancel="onCancel"
        />
      </form>
    </div>
    <hr />
    <div>
      <div style="text-align: left; padding: 0 12px">
        <label
          class="list-title"
          >相关医生</label
        >
        <list-wrap :source="list" description="暂无">
          <ul>
            <li v-for="(item, index) in list" :key="index" @click="toPatientPage(item.staffId, item.deptId)">
              <doctor-card :info="item"/>
              <!-- <div
                class="doctor-info"
                @click="toPatientPage(item.staffId, item.deptId)"
              >
              <doctor-card :info="item" @click="toPatientPage(item.staffId, item.deptId)"/>
                <div class="doctor-picture">
                  <img
                    v-if="item.picture"
                    :src="item.picture"
                    :alt="item.staffName"
                  />
                  <img v-else src="../assets/img/picture.png" alt="" />
                </div>
                <div class="doctor-detail">
                  <p class="name van-ellipsis">
                    {{ item.staffName }} <span>{{ item.title }}</span>
                  </p>
                  <p class="introduce van-multi-ellipsis--l2">
                    {{ item.advantages }}
                  </p>
                </div>
              </div> -->
            </li>
          </ul>
          <!-- <table class="myTable2">
            <tr v-for="(item,index) in list" :key="index" @click="toPatientPage(item.staffId, item.deptId)">
              <td><a href="javascript:;" style="color: #2c3e50">{{item.staffName}}</a></td>
              <td>{{item.title}}</td>
              <td>{{item.deptName}}</td>
            </tr>
          </table> -->
        </list-wrap>
      </div>
    </div>
  </div>
</template>
<script>
import DoctorCard from '@/components/DoctorCard';
import { selectStaffByName } from "../api/modules/queryStaffByName";

export default {
  name: "index",
  components: {
    DoctorCard
  },
  data() {
    DoctorCard
    return {
      value: "",
      list: null,
      name: this.$route.query.name,
      staffId: "",
    };
  },
  created() {
    const name = this.$route.query.name;
    this.value = name;
    selectStaffByName(this.value).then((res) => {
      this.list = res.data.data;
    });
  },
  methods: {
    onReturn() {
      this.$router.go(-1);
    },
    onSearch(val) {
      this.list = null;
      let name = this.value;
      this.$router.replace({
        path: "/appointment/result",
        query: {
          name: val,
        },
      });
      selectStaffByName(name).then((res) => {
        this.staffId = res.data.data.staffId;
        this.list = res.data.data;
      });
    },
    onCancel() {
      this.value = "";
      this.$router.push("/appointment/third");
    },
    toPatientPage(staffId, deptId) {
      this.$router.push({
        path: "/appointment/patientPage",
        query: {
          staffId,
          deptId,
        },
      });
    },
  },
};
</script>
<style lang="less">
.search-result-page {
  li {
    padding: 10px;
    border-radius: 8px;
    margin-bottom: 10px;
    box-shadow: 0 0 10px #ededed;
  }
  .list-title {
    display: inline-block;
    font-size: 16px;
    font-weight: bold;
    line-height: 2em;
    margin-bottom: 10px;
  }
}
</style>
