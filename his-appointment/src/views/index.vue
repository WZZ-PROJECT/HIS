<template>

  <div class="index">
    <label>预约须知</label>
    <div class="middle">
      <pre>为方便您早日就医康复，请您认真阅读预约就诊须知：

一、预约时间范围

1、您可预约1周内的普通号、专家号、部分特需号。

2、每日19点投放新号源，每日下午15点停止预约次日号源。

3、节假日后第一天门诊号源于节前最后一个工作日下午15点停止预约。

二、预约实名制

预约就诊采取实名制注册预约，请您如实提供患者的真实姓名、有效证件号(身份证、军官证、护照、港澳通行证、台胞证)、性别、电话、手机号码。

三、预约取号

1、预约成功后，请于就诊当日携带患者本人身份证等有效证件（北京医保患者带社保卡）、短信凭证到门诊楼一层2至11号取号窗口取号，同时验证预约信息（核对与预约注册实名信息一致的本人有效证件），如验证不符则无法取号。

2、取号时间：上午就诊患者9:00-11:00取号，下午就诊患者14:00-16:00取号;过时未取号者，预约作废。

3、取号时现场支付医事服务费。

4、我院专家号就诊顺序按照挂号条序号由小到大排列，就诊次序以现场挂号条为准，完成预约挂号后建议患者按照取号时间来院取号就诊，避免取号和就诊排队等待时间过长。

5、保障手机接收短信畅通，防止遗漏已经预约成功的专家停诊通知。

四、取消预约

由于预约号源有限，预约成功后请按时就诊。如不能按时就诊，请最晚在就诊前一个工作日15:00前通过网站进行取消。就诊当日不得取消当日预约号。

五、爽约处理

1、预约成功后患者未能按时就诊且未办理取消预约号，则视为爽约。

2、一年内（自然年）累计爽约3次，取消本年度的预约就诊资格。

六、其它注意事项

1、我院预约就诊有多种方法，请只采用一种方法预约，切勿重复预约。

2、同一患者在同一就诊日最多可预约10个不同科室的号源。
    </pre>
  </div>
    <van-button round color="#F3CA4B" @click="toThird()">我知道了</van-button>
  </div>
</template>
<script>
export default {
  name: "index",
  created() {
    const { patientId } = this.$route.query;
    if (patientId) {
      localStorage.patientId = patientId
    } else {
      localStorage.removeItem('patientId')
    }
  },
  methods: {
    toThird() {
      this.$router.push({
        path: "/appointment/third",
        query: {
          patientId: this.patientId,
        },
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
.department-list {
  padding: 0 15px;
  margin-top: 10px;
}
.list-item {
  box-sizing: border-box;
  padding: 0 12px;
  display: flex;
  margin-bottom: 10px;
  height: 90px;
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
  height: 80px;
  flex: 1;
  text-align: left;
  /* display: flex;
  flex-direction: column;
  align-items: flex-start;
  text-align: left; */
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
.list-item p span:last-child {
  display: inline-block;
  line-height: 30px;
}
.index {
  text-align: left;
  margin: 1rem;
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
  margin-top: 0.5rem;
  margin-bottom: 0.5rem;
  overflow: auto;
  padding: 0.5rem;
}
pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  text-align: left;
  color: #070707;
  font-size: 14px;
  font-family: 黑体;
}
</style>