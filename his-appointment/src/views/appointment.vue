<template>
  <div>
    <tab name="appointment">
      <router-view></router-view>
    </tab>
  </div>
</template>

<script>
import Tab from '@/components/Tab';

export default {
  components: {
    Tab,
  },
};
</script>

<style lang="less">
</style>