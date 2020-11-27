<template>
  <div class="doctor-page">
    <list-wrap :source="dataSource">
      <ul>
        <li v-for="(item, index) in dataSource" :key="index">
          <div class="doctor-info">
            <p class="doctor-name">{{ item.name }}</p>
            <p class="doctor-dept">{{ item.dept.name }}{{ item.dept.title }}</p>
            <span>医生介绍</span>
            <p class="doctor-introduce elli">{{ item.advantages }}</p>
          </div>
          <div class="doctor-image">
            <img v-if="item.picture" :src="item.picture" :alt="item.name"/>
            <img v-else src="../../assets/img/picture.png" alt="">
            <a href="javascript:;" @click="handleCancel(item.id)">取消关注</a>
          </div>
        </li>
      </ul>
    </list-wrap>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      dataSource: null,
    };
  },
  mounted() {
    this.request();
  },
  methods: {
    request() {
      axios({
        url: "/api/pmsPatient/queryStaffByOpenId?openId=" + localStorage.openID,
        method: "GET",
      }).then((res) => {
        if (res.data.code === 200 && res.data.data) {
          this.dataSource = res.data.data || [];
        } else {
          this.dataSource = new Error();
        }
      });
    },
    handleCancel(id) {
      const that = this;
      this.$dialog.confirm({
        title: '提示',
        message: '是否取消关注？',
        beforeClose(action, done) {
          if (action === 'confirm') {
            axios({
              url: "/api/pmsPatient/stopFollow",
              method: "post",
              data: {
                patientId: that.$store.state.patientId,
                staffId: id,
              },
            }).then((res) => {
              if (res.data.code === 200 && res.data.message === "操作成功") {
                that.request();
              }
            })
              .finally(() => {
                done();
              })
          } else {
            done();
          }
        }
      });
    },
  },
};
</script>

<style lang="less">
.doctor-page {
  text-align: left;
  font-size: 14px;
  ul {
    padding: 0 10px;
  }
  li {
    display: flex;
    align-items: center;
    border-top: 1px solid #666;
    border-bottom: 1px solid #666;
    margin-bottom: 10px;
  }
  .doctor-info {
    width: 0;
    flex: 1;
    padding: 20px;
    span {
      display: inline-block;
      margin-top: 10px;
      color: #f4d164;
      font-size: 14px;
    }
  }
  .doctor-image {
    width: 100px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    img {
      width: 75px;
      height: 75px;
      border-radius: 50%;
    }
    a {
      margin-top: 8px;
      padding: 0 10px;
      font-size: 12px;
      line-height: 24px;
      background: #f8f8f8;
      border-radius: 2px;
      color: #000;
      font-weight: bold;
    }
  }
  .doctor-name {
    font-weight: bold;
    font-size: 16px;
  }
  .doctor-dept {
    margin-top: 5px;
    color: #999999;
    font-size: 14px;
  }
  .doctor-introduce {
    color: #999;
    font-size: 14px;
    text-indent: 2em;
    width: 100%;
  }
}
</style>