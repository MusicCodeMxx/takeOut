/** 自定义模拟太 */
import messageBox from './MessageBox.vue'
import toast from './Toast.vue'

export default {
     install(Vue) {
        Vue.prototype.$ssMessageBox = (message) => {
            // 生成一个Vue的子类
            const toastTpl = Vue.extend(messageBox);
            // 生成一个该子类的实例
            const tpl = new toastTpl().$mount();
            // 并将此div加入全局挂载点内部
            document.body.appendChild(tpl.$el);
            tpl.message = message;
            return tpl.confirm();
        }

         Vue.prototype.$ssToast = (message) => {
             // 设置默认参数，可设置多个
             let defaultOpts = { duration: 2000 }
             defaultOpts = Object.assign(defaultOpts);
             // 生成一个Vue的子类
             let toastTpl = Vue.extend(toast);
             // 生成一个该子类的实例
             let tpl = new toastTpl().$mount();
             // 并将此div加入全局挂载点内部
             document.body.appendChild(tpl.$el);
             // 修改提示语
             tpl.message = message;
             tpl.messageShow = true;
             // 定时消失
             setTimeout(() => {
                 console.log("1111");
                 document.body.removeChild(tpl.$el);
             }, defaultOpts.duration)
         }
    }
}
