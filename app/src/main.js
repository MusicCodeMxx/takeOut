import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

/** 引入 elemetUI Star */
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
/** vant Star */
import Vant from 'vant';
import 'vant/lib/index.css';
/** 自定义模拟太 */
import ssMessageBox from "./components/index"; // 全局弹窗转接提示组件
/** 权限控制 - 导航守卫 */
// permission control
import './permission';

Vue.use(ElementUI);
/** 引入 elemetUI End */
Vue.use(Vant);
/** vant End */
Vue.use(ssMessageBox);

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: function (h) { return h(App) }
}).$mount('#app')


