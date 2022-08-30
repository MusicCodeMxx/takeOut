import Vue from 'vue'
import VueRouter from 'vue-router'

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}
Vue.use(VueRouter)
const routes = [
  {
    path: '/',
    component: ()=>{return import("../views/LayOut")},
    redirect: "home",
    children: [
      { path: 'home', name: 'Home', component: () => { return import('../views/Home.vue') } },
      { path: 'order', name: 'Order', component: () => { return import('../views/Order.vue') } },
      { path: 'me', name: 'Me', component: () => { return import('../views/Me.vue') } },
      { path: '/coupon/get', name: 'GetCoupon', component: () => { return import('../views/GetCoupon.vue') } },
    ]
  },
  { path: '/login', name: 'Login', component: () => { return import('../views/Login.vue') } },
  { path: '/user/agreement', name: 'userAgreement', component: () => { return import('../views/UserAgreement.vue') } },
  { path: '/user/address/book', name: 'AddressBook', component: () => { return import('../views/AddressBook.vue') } },
  { path: '/user/address/edit', name: 'AddressEdit', component: () => { return import('../views/AddressEdit.vue') } },
  { path: '/order/details', name: 'OrderDetails', component: () => { return import('../views/OrderDetails.vue') } },
  { path: '/order/details/re', name: 'ReOrderDetails', component: () => { return import('../views/ReOrderDetails.vue') } },
  { path: '/order/pay/result', name: 'PayResult', component: () => { return import('../views/PayResult.vue') } },
  { path: '/order/return/apply', name: 'orderReturnApply', component: () => { return import('../views/OrderReturnApply.vue') } },
  { path: '/customer/service', name: 'customerService', component: () => { return import('../views/CustomerService.vue') } },
  { path: '/coupon', name: 'Coupon', component: () => { return import('../views/Coupon.vue') } },
]

const router = new VueRouter({
  routes
})

export default router
