import Vue from 'vue'
import App from './App'
import router from './router'
import Vant, { Toast } from 'vant';

import '@/assets/common.css';
import 'vant/lib/index.less';

// import '@/mock/mock'

Vue.use(Vant);
Vue.use(Toast);

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
