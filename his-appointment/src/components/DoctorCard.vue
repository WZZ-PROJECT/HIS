<template>
  <div class="doctor-info">
    <div class="doctor-picture">
      <image-wrap :url="info.picture" :alt="info.staffName"/>
      <!-- <img v-if="info.picture" :src="info.picture" :alt="info.staffName" />
      <img v-else src="../assets/img/picture.png" alt="" /> -->
    </div>
    <div class="doctor-detail">
      <p class="name van-ellipsis">
        {{ info.staffName }} <span>{{ info.title }}</span>
      </p>
      <p class="introduce van-multi-ellipsis--l2">
        {{ info.advantages }}
      </p>
    </div>
  </div>
</template>

<script>
import ImageWrap from './ImageWrap';

export default {
  props: {
    info: {
      type: Object,
      required: true
    }
  },
  components: {
    ImageWrap
  }
};
</script>

<style lang="less" scoped>
.doctor-info {
  display: flex;
  .doctor-picture {
    width: 60px;
    height: 75px;
    overflow: hidden;
    border-radius: 5px;
  }
  .doctor-detail {
    flex: 1;
    padding-left: 20px;
    width: 0;
    height: 75px;
    .name {
      font-size: 18px;
      font-weight: bold;
      color: #181818;
      height: 1.2em;
      line-height: 1.2em;
      span {
        font-size: 14px;
        font-weight: normal;
        color: #646464;
      }
    }
    .introduce {
      margin-top: 8px;
      height: 3em;
      line-height: 1.5em;
      color: #a1a1a1;
    }
  }
}
</style>