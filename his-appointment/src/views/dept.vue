<template>
  <div>
    <van-nav-bar :title="$route.query.deptName" left-arrow @click-left="$router.go(-1)"/>
    <!-- <van-field
      readonly
      clickable
      label="科室"
      :value="value"
      placeholder="选择科室"
      @click="showPicker = true"
    />
    <van-popup v-model="showPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns"
        @cancel="showPicker = false"
        @confirm="onConfirm"
        @change="onChange"
      />
    </van-popup> -->
    <order/>
  </div>
</template>

<script>
const depts = {
  '西院区': ["脾胃病科", "肝病科", "消化科", "内分泌科", "肺病科", "风湿免疫科", "肾病科", "肿瘤科"],
};
import Order from './fourth.vue';

export default {
  components: {
    Order,
  },
  data() {
    return {
      value: "",
      showPicker: false,
      columns: [{ values: Object.keys(depts) }, { values: depts["西院区"] }],
    };
  },
  methods: {
    onConfirm(value) {
      this.value = value.join('-');
      this.showPicker = false;
    },
    onChange(picker, values) {
      picker.setColumnValues(1, depts[values[0]]);
    },
  },
};
</script>

<style>
</style>