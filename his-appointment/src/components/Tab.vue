<template>
  <div>
    <van-nav-bar
      :title="$route.meta.title || '请填写标题'"
      :left-arrow="!$route.meta.hideBack"
      @click-left="$router.go(-1)"
    />
    <keep-alive>
      <router-view v-if="$route.meta.keep" />
    </keep-alive>
    <router-view v-if="!$route.meta.keep"></router-view>
    <van-tabbar v-model="page" v-if="hasPatientId" :before-change="beforeTabChange">
      <van-tabbar-item name="appointment">
        <span>预约</span>
        <template #icon="props">
          <img v-if="props.active" src="../assets/img/home-active.png" alt="">
          <img v-else src="../assets/img/home.png" alt="">
        </template>
      </van-tabbar-item>
      <van-tabbar-item name="center">
        <span>我的</span>
        <template #icon="props">
          <img v-if="props.active" src="../assets/img/person-active.png" alt="">
          <img v-else src="../assets/img/person.png" alt="">
        </template>
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
export default {
  props: ['name'],
  data() {
    return {
      page: 'home',
    };
  },
  computed: {
    hasPatientId() {
      return !localStorage.patientId
    }
  },
  created() {
      this.page = this.name
  },
  methods: {
      beforeTabChange(v) {
          if (v != this.page) {
              this.$router.replace(`/${v}`)
          }
      }
  }
};
</script>

<style>
</style>