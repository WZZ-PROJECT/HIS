<template>
  <div class="information-page">
    <tab name="information" class="iframe-wrap" v-full>
      <keep-alive>
        <iframe src="https://www.cgjiankang.com" frameborder="0"></iframe>
      </keep-alive>
    </tab>
  </div>
</template>

<script>
import Tab from '@/components/Tab';

export default {
  components: {
    Tab,
  },
  directives: {
    full(el) {
      el.style.minHeight = document.documentElement.clientHeight + 'px'
    }
  },
};
</script>

<style lang="less">
.information-page {
  width: 100%;
  height: 100%;
  .iframe-wrap {
    box-sizing: border-box;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  iframe {
    width: 100%;
    height: 0;
    flex: 1;
    margin: 0;
    padding: 0;
  }
  text-align: left;
  ul {
    padding: 15px 12px 0;
  }
  li {
    margin-bottom: 12px;
    border-radius: 8px;
    display: flex;
    .item-picture {
      width: 100px;
      height: 100px;
      display: flex;
      align-items: center;
      justify-content: center;
      // border-radius: 6px;
      // overflow: hidden;
    }
    .item-info {
      padding-left: 10px;
      width: 0;
      flex: 1;
    }
    img {
      max-width: 100%;
      max-height: 100%;
    }
    .name {
      font-size: 16px;
      line-height: 28px;
    }
    .expert, .detail {
      font-size: 14px;
    }
    .expert {
      width: 100%;
      line-height: 24px;
    }
    .detail {
      height: 40px;
      line-height: 20px;
      overflow: hidden;
    }
  }
}
</style>