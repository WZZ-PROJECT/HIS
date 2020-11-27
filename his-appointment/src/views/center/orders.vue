<template>
  <div class="orders-page">
    <list-wrap :source="dataSource">
      <ul>
        <li v-for="(item, index) in dataSource" :key="index">
          <p class="record-name">
            <span class="elli">{{ item.staffName }}</span>
            <span v-if="item.status == 1" style="float: right">未看诊</span>
            <span v-else-if="item.status == 2" style="float: right"
              >待缴费</span
            >
            <span v-else-if="item.status == 3" style="float: right">诊毕</span>
            <span v-else style="float: right">已取消</span>
          </p>
          <p><span class="elli">就诊科室</span>{{ item.deptName }}</p>
          <p><span class="elli">就诊医生</span>{{ item.staffName }}</p>
          <p><span class="elli">就诊时间</span>{{ item.date }}</p>
          <p><span class="elli">就诊人</span>{{ item.patientName }}</p>
          <p><span class="elli">就诊费用</span>{{ item.amount }}元</p>
          <div class="cancel-btn" v-if="item.status === 1 || item.status === 2">
            <van-button class="cancel-button" type="warning" @click="cancel(item)">退号</van-button>
          </div>
        </li>
      </ul>
    </list-wrap>
  </div>
</template>

<script>
import axios from "axios";
import moment from 'moment';

export default {
  data() {
    return {
      dataSource: null,
    };
  },
  mounted() {
    this.request();
  },
  methods: {
    request() {
      this.dataSource = null;
      axios({
        url: "/api/staff/listPatientConvention?openID=" + localStorage.openID,
        method: "GET",
      }).then((res) => {
        if (res.data.code === 200) {
          this.dataSource = res.data.data || [];
          this.dataSource = this.dataSource.reverse();
        }
      });
    },
    cancel(item) {
      const toast = this.$toast.loading({
        forbidClick: true,
        duration: 0
      });
      axios.post('/api/fee/refundRegistrationCharge', {
        operatorId: this.$store.state.patientId,
        registrationId: item.id
      }).then((res) => {
        this.$toast(res.data.message);
        if (res.data.data === 1) {
          this.request();
        }
      })
    }
  },
};
</script>

<style lang="less">
.orders-page {
  text-align: left;
  font-size: 14px;
  ul {
    padding: 0 10px;
  }
  li {
    box-sizing: border-box;
    padding: 0 10px;
    margin-bottom: 10px;
    border: 1px solid #666;
    border-radius: 4px;
  }
  p {
    display: flex;
    height: 34px;
    line-height: 34px;
    span:first-child {
      width: 0;
      flex: 1;
    }
  }
  .record-name {
    height: 36px;
    line-height: 36px;
    border-bottom: 2px solid #666;
  }
  .cancel-btn {
    display: flex;
    justify-content: flex-end;
    padding-bottom: 10px;
  }
  .cancel-button {
    height: 30px !important;
    padding: 0 30px !important;
  }
}
</style>