<template>
    <div class="ss-customer-service__body">
        <div class="ss-message__body">
            <div class="ss-title"></div>
            <div class="ss-title">
                <div>联系客服正在开发中敬请期待</div>
                <div style="text-align: left">
                    <div>届时会使用</div>
                    <div style="font-weight: bold;font-size: 18rem;">vue3+vite+webSocket</div>
                    <div>制作在线与客服联系</div>
                </div>
            </div>
            <div class="ss-button__body">
                <el-button  type="primary" @click.stop.prevent="onConfirm">我要取消并退款</el-button>
            </div>
        </div>

    </div>
</template>

<script>

import {Notify} from 'vant';

const PAGE_NAME = "客服页面"
export default {
    name: "CustomerService",
    data(){
        return{
            orderItem:undefined,
        }
    },
    created(){
        console.log(PAGE_NAME+"创建");
        this.initData()
    },
    beforeMount() {
        console.log(PAGE_NAME+"初始化");
    },
    mounted(){
        console.log(PAGE_NAME+"初始化完成");
    },
    beforeDestroy() {
        console.log(PAGE_NAME+"销毁前");
    },
    destroyed() {
        console.log(PAGE_NAME+"销毁了");
    },
    beforeRouteEnter(to, from, next) {
        console.log(PAGE_NAME+"路由器进入");
        next(vm => {
            vm.initData();
        });
    },
    beforeRouteLeave(to, from, next) {
        this.orderItem = undefined;
        console.log(PAGE_NAME,"组件导航守卫路由器离开");
        next();
    },
    methods:{

        initData(){
            if (this.$route.params) if (this.$route.params.id)  this.orderItem = this.$route.params;
        },

        onConfirm(){
           if (this.orderItem) {
               if (this.orderItem.status === 1){
                   this.drawer = false;
                   Notify("您选择订单时未付款无法操作");
                   this.$router.replace({path:"/order"});
               }else{
                   // 去退款申请页
                   this.$router.push({
                       name: 'orderReturnApply',
                       params: this.orderItem
                   })
               }
           }else{
               Notify("未知错误,请重试");
               this.$router.replace({path:"/order"});
           }
        },

    }
}
</script>

<style lang="scss" scoped>
.ss-customer-service__body {
    position: relative;
    .ss-message__body {
        height: 100vh;
        display: flex;
        flex-direction: column;
        align-items: center;
        font-size: 16rem;
        text-align: center;

        .ss-title:nth-child(1) {
            flex: 5;
        }

        .ss-title:nth-child(2) {
            flex: 1;
            line-height: 30rem;
        }

        .ss-button__body {
            margin-top: 20rem;
            flex: 2;
        }
    }
}
</style>