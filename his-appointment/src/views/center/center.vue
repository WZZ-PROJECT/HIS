<template>
  <div>
    <div class="user-info-wrap" @click="$router.push('/center/user')">
      <div class="user-picture">
        <!-- <img src="https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIwqun0jT7zWzkckum2s2qsD8vtjWy9lPJdYYgibMGEBmRwQib8KDXSrTv52Osz7JtkTff8crB7ZrFA/132" alt=""> -->
        <img :src="userInfo.headimgurl" alt="">
        <span class="elli">{{ userInfo.nickname }}</span>
      </div>
      <i class="van-icon van-icon-arrow van-cell__right-icon"></i>
    </div>
    <van-cell-group style="text-align: left">
      <van-cell title="预约记录" is-link to="/center/orders" />
      <van-cell title="关注医生" is-link to="/center/doctors" />
      <van-cell title="历史病历" is-link to="/center/history" />
      <van-cell title="缴费记录" is-link to="/center/records" />
      <van-cell title="预存费用" is-link to="/center/prestore" />
    </van-cell-group>
  </div>
</template>

<script>
import Tab from "@/components/Tab";

export default {
  components: {
    Tab,
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo || JSON.parse(localStorage.APP_USER_INFO || '{}')
    },
    patientId() {
      return this.$store.state.patientId
    }
  },
  beforeRouteLeave(to, from, next) {
    if (to.path.indexOf('center') > -1) {
      const openid = localStorage.openID;
      if (!openid) {
        const that = this;
        this.$dialog.alert({
          title: "提示",
          message: "尚未获取微信授权，请重新登录",
        });
        return;
      }
      if (!this.patientId) {
        this.$dialog.confirm({
          title: "提示",
          message: "尚未绑定身份信息，是否绑定？",
        })
          .then(() => {
            next({
              path: '/login',
              query: {
                redirect: encodeURIComponent(from.fullPath)
              }
            })
          })
          .catch(() => {});
      } else next();
    } else next();
  }
};
</script>

<style lang="less">
.user-info-wrap {
  padding: 10px 16px;
  display: flex;
  align-items: center;
  .user-picture {
    display: flex;
    align-items: center;
    flex: 1;
    img {
      width: 100px;
      height: 100px;
      border-radius: 50%;
    }
    span {
      padding-left: 45px;
      padding-right: 20px;
      text-align: left;
      width: 0;
      flex: 1;
      font-size: 14px;
    }
  }
  .user-placeholder {
    flex: 1;
  }
}
</style>