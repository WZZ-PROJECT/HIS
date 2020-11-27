<template>
  <div>
    <img v-if="url" :src="url" :alt="alt" />
    <img v-else src="../assets/img/picture.png" alt="" />
  </div>
</template>

<script>
export default {
  props: {
    url: {
      type: String,
      required: true
    },
    alt: {
      type: String,
      default: 'image'
    }
  }
}
</script>

<style scoped>
div {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
  img {
    max-width: 100%;
    max-height: 100%;
  }
</style>