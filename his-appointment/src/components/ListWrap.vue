<template>
  <div>
    <div v-if="source && source.length">
      <slot></slot>
    </div>
    <van-empty
      v-else-if="source && source.length === 0"
      :description="description"
    />
    <van-loading v-else class="page-loading-tip" size="48px"
      >加载中...</van-loading
    >
  </div>
</template>

<script>
export default {
  props: {
    source: {
      required: true,
    },
    description: {
      type: String,
      default: '暂无数据'
    }
  },
};
</script>

<style>
</style>