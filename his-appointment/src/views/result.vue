<template>
  <div>
    <div>
      <!-- <van-nav-bar title="搜索" left-arrow @click-left="onReturn" /> -->
      <form action="/">
        <van-search
          v-model="value"
          placeholder="请输入搜索关键词"
          show-action
          @search="onSearch"
          @cancel="onCancel"
        />
      </form>
    </div>
    <hr />
    <div>
      <div style="text-align: left;padding: 0 12px">
        <label
          for
          style="font-size: 0.9rem;color: #b0b0b0;margin-bottom: 0.4rem; display: inline-block;"
        >相关医生</label>
        <table class="myTable2">
          <tr v-for="(item,index) in list " :key="index" @click="toPatientPage(item.staffId, item.deptId)">
            <td><a href="javascript:;" style="color: #2c3e50">{{item.staffName}}</a></td>
            <td>{{item.title}}</td>
            <td>{{item.deptName}}</td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>
<script>
import { selectStaffByName } from "../api/modules/queryStaffByName";
export default {
  name: "index",
  components: {},
  data() {
    return {
      value: '',
      list: '',
      name: this.$route.query.name,
      staffId:''
    };
  },
  created() {
    const name = this.$route.query.name;
    this.value = name
    selectStaffByName(this.value).then((res) => {
      this.list = res.data.data;
    });
  },
  methods: {
    onReturn() {
      this.$router.go(-1);
    },
    onSearch(val) {
      let name = this.value
      this.$router.replace({
        path: '/appointment/result',
        query: {
          name: val
        }
      });
      selectStaffByName(name).then((res) => {
        this.staffId = res.data.data.staffId
        this.list = res.data.data
      });
    },
    onCancel() {
      this.value = "";
      this.$router.push("/appointment/third")
    },
    toPatientPage(staffId, deptId) {
      this.$router.push({
        path: '/appointment/patientPage',
        query: {
          staffId,
          deptId
        }
      })
    }
  },
};
</script>
<style scoped>
.myTable2 {
  width: 100%;
}
.myTable2 tr {
  text-align: center;
}
</style>
