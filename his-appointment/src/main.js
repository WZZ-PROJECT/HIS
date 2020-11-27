import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'

import '@/assets/common.css';
import 'vant/lib/index.less';

import './permission'
import './utils/wx'
import './utils/flexible'
// import '@/mock/mock'

import Vant, {
  Toast
} from 'vant';
import ListWrap from '@/components/ListWrap';

Vue.use(Vant);
Vue.use(Toast);
Vue.component('ListWrap', ListWrap);

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
