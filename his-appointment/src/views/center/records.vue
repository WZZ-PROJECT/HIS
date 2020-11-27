<template>
  <div class="records-page">
    <list-wrap :source="dataSource">
      <ul>
        <li v-for="(item, index) in dataSource" :key="index">
          <p class="record-name">
            <span class="elli">{{ item.name }}</span>
            <!-- <span v-if="item.status === -1" style="float: right">未开立</span>
            <span v-else-if="item.status === 0" style="float: right">已作废</span> -->
            <span v-if="item.status === 1" style="float: right">未缴费</span>
            <span v-else-if="item.status === 2" style="float: right">未登记</span>
            <span v-else-if="item.status === 3" style="float: right">已登记</span>
            <span v-else-if="item.status === 4" style="float: right">已完成</span>
          </p>
          <p><span class="elli">病历号</span>{{ item.medicalRecordNo }}</p>
          <p><span class="elli">看诊时间</span>{{ item.attendanceDate }}</p>
          <p><span class="elli">总金额</span>{{ item.amount }} 元</p>
          <p><span class="elli">开立时间</span>{{ item.createTime }}</p>
          <p>
            <span class="elli">项目类型</span>
            <span v-if="item.type == 1">检查</span>
            <span v-else-if="item.type == 2">检验</span>
            <span v-else-if="item.type == 3">处置</span>
            <span v-else-if="item.type == 4">草药处方</span>
            <span v-else-if="item.type == 5">成药处方</span>
          </p>
        </li>
      </ul>
    </list-wrap>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      dataSource: null
    };
  },
  mounted() {
    this.request();
  },
  methods: {
    request() {
      axios({
        url: "/api/fee/listCharge",
        data: {
          openID: localStorage.openID,
          patientId: this.$store.state.patientId,
        },
        method: "POST",
      }).then((res) => {
        if (res.data.code === 200) {
          this.dataSource = res.data.data || [];
        }
      });
    },
  }
}
</script>

<style lang="less">
.records-page {
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
      font-weight: bold;
    }
  }
  .record-name {
    height: 36px;
    line-height: 36px;
    border-bottom: 2px solid #666;
  }
}
</style>