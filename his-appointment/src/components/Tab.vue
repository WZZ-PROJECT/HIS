<template>
  <div class="tab-page-header">
    <template v-if="!$route.meta.hideLayout">
      <van-nav-bar
        fixed
        :placeholder="true"
        :title="$route.query.deptName || $route.meta.title"
        
      >
        <template slot="left">
          <i v-if="showLeftArrow" @click="handleLeftClick" class="van-icon van-icon-arrow-left van-nav-bar__arrow"></i>
          <i v-else-if="showHomeBtn" @click="handleHomeClick" class="van-icon van-icon-home-o"></i>
        </template>
      </van-nav-bar>
    </template>
    <slot />
    <template v-if="!$route.meta.hideLayout">
      <van-tabbar
        fixed
        :placeholder="true"
        v-model="page"
        v-show="fromApplet"
        :before-change="beforeTabChange"
      >
        <van-tabbar-item name="information">
          <span>动态</span>
          <template #icon="props">
            <img
              v-if="props.active"
              src="../assets/img/information-active.png"
              alt=""
            />
            <img v-else src="../assets/img/information.png" alt="" />
          </template>
        </van-tabbar-item>
        <van-tabbar-item name="appointment">
          <span>预约</span>
          <template #icon="props">
            <img
              v-if="props.active"
              src="../assets/img/home-active.png"
              alt=""
            />
            <img v-else src="../assets/img/home.png" alt="" />
          </template>
        </van-tabbar-item>
        <van-tabbar-item name="center">
          <span>我的</span>
          <template #icon="props">
            <img
              v-if="props.active"
              src="../assets/img/person-active.png"
              alt=""
            />
            <img v-else src="../assets/img/person.png" alt="" />
          </template>
        </van-tabbar-item>
      </van-tabbar>
    </template>
  </div>
</template>

<script>
export default {
  props: ["name"],
  data() {
    return {
      page: "appointment",
    };
  },
  computed: {
    fromApplet() {
      return localStorage.APP_FROM !== "applet";
    },
    showLeftArrow() {
      return (
        !this.$route.meta.hideBack && this.$route.query.appfrom !== "applet"
      );
    },
    showHomeBtn() {
      return this.$route.query.appfrom === "applet";
    }
  },
  created() {
    this.page = this.name;
  },
  methods: {
    beforeTabChange(v) {
      if (v != this.name) {
        this.$router.replace(`/${v}`);
      }
    },
    handleLeftClick() {
      if (this.showLeftArrow) this.$router.go(-1);
    },
    handleHomeClick() {
      this.$router.push('/appointment/third')
    }
  },
};
</script>

<style lang="less">
.page-header-fixed {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
}
.van-tabbar-item--active {
  color: @green !important;
}
.tab-page-header {
  .van-icon {
    font-size: 20px;
  }
}
</style>