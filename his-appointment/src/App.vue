<template>
  <vuescroll :ops="options">
    <div id="app" v-full>
      <keep-alive>
        <router-view v-if="$route.meta.keep" />
      </keep-alive>
      <router-view v-if="!$route.meta.keep"></router-view>
    </div>
  </vuescroll>
</template>

<script>
import vuescroll from 'vuescroll';

export default {
  name: "App",
  components: {
    vuescroll
  },
  directives: {
    full(el) {
      el.style.minHeight = document.documentElement.clientHeight + 'px'
    }
  },
  data() {
    return {
      options: {
        scrollPanel: {
          scrollingX: false
        },
        bar: {
          background: '#21bb9d',
          size: '4px'
          // disable: true
        }
      },
    }
  }
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: 100%;
  overflow-x: hidden;
  overflow-y: auto;
}
.section-title {
  padding: 8px 12px;
  font-size: 14px;
  text-align: left;
  font-weight: bold;
}
</style>
