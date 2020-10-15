<template>
  <div>
    <div class="user-info-wrap">
      <div class="user-picture">
        <img src="https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIwqun0jT7zWzkckum2s2qsD8vtjWy9lPJdYYgibMGEBmRwQib8KDXSrTv52Osz7JtkTff8crB7ZrFA/132" alt="">
        <span>.</span>
      </div>
      <div class="user-placeholder"></div>
      <i class="van-icon van-icon-arrow van-cell__right-icon"></i>
    </div>
    <van-cell-group style="text-align: left">
      <van-cell title="预约记录" is-link url="/center/orders" />
      <van-cell title="关注医生" is-link url="/center/doctors" />
      <van-cell title="历史病历" is-link url="/center/history" />
      <van-cell title="缴费记录" is-link url="/center/records" />
      <van-cell title="预存费用" is-link url="/center/prestore" />
    </van-cell-group>
  </div>
</template>

<script>
import Tab from "@/components/Tab";

export default {
  components: {
    Tab,
  },
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
    img {
      width: 90px;
    }
    span {
      font-size: 14px;
    }
  }
  .user-placeholder {
    flex: 1;
  }
}
</style>