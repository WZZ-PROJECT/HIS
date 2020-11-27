<template>
  <div class="index">
    <label>疫情防控期来院就诊通知</label>
    <div class="middle">
    <pre>
      尊敬的患者朋友:
      为进一步落实国家及省、市卫健委对新型冠状病毒感染性肺炎疫情防控工作的部署，特向所有来我院门诊就诊的患者及家属做以下告知:
      1、所有来我院门诊就诊的患者、患者家属和陪护人员均需戴口罩，配合我院工作人员进行体温监测和对您健康相关状况的问询。
      2、如您有发热和近期外出史，请直接到发热门诊就诊，不要在本中心内随意停留或探视。
      3、经预检分诊台查出的发热患者，需由预检分诊台人员将您护送至发热门诊就诊，不得随意更改就诊路线。
      4、感冒及发热轻症患者，请先通过我院微信公众号“ 新型冠状病毒肺炎防治专线”进行线上咨询，如有必要再去医院发热门诊就诊。
      5、门诊就诊时，尽量减少陪同人员，限一名陪同人员，其他人员建议在院外等候，如陪同人员有发热、流感症状，不要来院就诊。
      6、所有就诊者，如果您近14天去过疫区，或接触过疫区回来的人员，需在家自行隔离14天后，确认没有异常再来院就诊。
      7、如果您仅仅是到了复查时间，目前无不适症状，本次预约复查可以后延，视疫情控制情况调整时间。
      8、就诊离院后，建议更换口罩，进行规范的手消毒，随身携带物品同时做消毒处理。
      <span style="color: red">温馨提示：</span>请您仔细阅读以上告知，实名就诊，切勿隐瞒任何病史，以免被追究法律责任!为了您和家人的健康，请务必配合我院工作人员的引导。由此带来不便，敬请您谅解!
    </pre>
    </div>
    <van-button type="primary" round @click="toThird()">我知道了</van-button>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  data() {
    return {
      url: "/appiontment/third"
    };
  },
  methods: {
    toThird() {
      const path = localStorage.APP_FROM === "applet" ? '/appiontment/third' : '/information'
      this.$router.push({
        path,
      });
    },
  },
};
</script>
<style>
.index {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.section-title {
  padding: 8px 12px;
  font-size: 14px;
  text-align: left;
  font-weight: bold;
}
.search-result-list {
  display: flex;
  justify-content: space-around;
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
.department-list {
  padding: 0 15px;
  margin-top: 10px;
}
.index {
  text-align: left;
  padding: 20px 15px;
}
.index label {
  color: #f3ca4b;
  font-size: 16px;
  font-weight: bold;
}
.index button {
  float: right;
}
.middle {
  border: 1px solid #999999;
  flex: 1;
  margin: 15px 0;
  overflow: auto;
  padding: 10px;
}
pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  text-align: left;
  color: #070707;
  font-size: 14px;
  line-height: 1.5em;  
  font-family: "Avenir", Helvetica, Arial, sans-serif;
}
</style>