<template>
  <div class="history-page">
    <list-wrap :source="dataSource">
      <van-collapse v-model="activeNames">
        <van-collapse-item
          v-for="(item, index) in dataSource"
          :key="index"
          class="history-item"
          :title="item.priliminaryDiseStrList"
          :name="index"
        >
          <p class="item-detail">
            <span>主要就诊</span>{{ item.priliminaryDiseStrList }}
          </p>
          <p class="item-detail"><span>主诉</span>{{ item.chiefComplaint }}</p>
          <p class="item-detail">
            <span>现治疗情况</span>{{ item.historyOfTreatment }}
          </p>
          <p class="item-detail">
            <span>现病史</span>{{ item.historyOfPresentIllness }}
          </p>
          <p class="item-detail"><span>既往史</span>{{ item.pastHistory }}</p>
          <p class="item-detail"><span>过敏史</span>{{ item.allergies }}</p>
        </van-collapse-item>
      </van-collapse>
    </list-wrap>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      dataSource: null,
      activeNames: [],
    };
  },
  mounted() {
    this.request();
  },
  methods: {
    request() {
      const patientId = this.$store.state.patientId
      axios({
        url:
          "/api/caseHistory/queryCaseHistory?patientId=" + patientId,
        method: "GET",
      }).then((res) => {
        this.dataSource = res.data.data.dmsCaseHistoryList || [];
      });
    },
  },
};
</script>

<style lang="less">
.history-page {
  text-align: left;
  font-size: 14px;
  .history-item {
    .van-cell__title span {
      text-align: left !important;
    }
    .item-detail {
      span {
        display: inline-block;
        width: 30%;
      }
    }
  }
}
</style>