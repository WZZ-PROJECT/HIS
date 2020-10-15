<template>
  <div class="notice-modal">
    <label>预约须知</label>
    <div class="middle">
      <pre>
为方便您早日就医康复，请您认真阅读预约就诊须知：

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
    </pre
      >
    </div>
    <van-button round color="#F3CA4B" @click="handleClose">我知道了</van-button>
  </div>
</template>
<script>
export default {
  methods: {
    handleClose() {
      this.$emit('close')
    },
  },
};
</script>
<style>
.notice-modal {
  text-align: left;
  padding: 1rem;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  background-color: #fff;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}
.notice-modal label {
  color: #f3ca4b;
  font-size: 16px;
  font-weight: bold;
}
.notice-modal button {
  float: right;
}
.middle {
  border: 1px solid #999999;
  flex: 1;
  /* height: 30rem; */
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
}
</style>