<template>
    <div class="ss-pay-method-popup__body" v-if="drawer || myDrawer" @click.stop.prevent="onClose">
        <div class="ss-pay-method__body" >
            <div class="ss-pay-item__body ss-success__color" @click.stop.prevent="onConfirm(0)">
                <div class="ss-pay-title ">联系客服</div>
            </div>
            <div  v-if="myStatus > 4" class="ss-pay-item__body ss-danger__color" @click.stop.prevent="onConfirm(1)">
                <div class="ss-pay-title ">删除订单</div>
            </div>
            <div  v-if="myStatus===0||myStatus===1" class="ss-pay-item__body ss-danger__color" @click.stop.prevent="onConfirm(2)">
                <div class="ss-pay-title ">取消订单</div>
            </div>
            <!--已支付 //  2付款成功, 3制作, 4派送, 5派送完成-->
            <div  v-if=" 2 <= myStatus &&  myStatus <= 5  " class="ss-pay-item__body ss-danger__color" @click.stop.prevent="onConfirm(3)">
                <div class="ss-pay-title ">退款申请</div>
            </div>
        </div>
    </div>
</template>

<script>

const PAGE_NAME = "订单信息抽屉模块"
export default {
    name: "PayMethodPopup",
    props: ['drawer','orderId','order'],
    data(){
        return{
            myDrawer:false,
            myStatus:this.status,
            myOrderId:this.orderId,
            myOrder:this.order,
        }
    },
    deactivated() {
        this.myDrawer = false;
        this.myStatus = undefined;
        this.myOrderId = undefined;
        this.myOrder = undefined;
        console.info(PAGE_NAME,"缓存暂停")
    },
    watch:{ },
    methods:{
        onClose(){
            this.myDrawer = false;
            this.$emit("close");
        },
        onConfirm(e){
            this.myDrawer = false;
            this.$emit("confirm",e,this.myOrderId, this.myOrder);
        }
    }
}
</script>

<style lang="scss" scoped>
.ss-pay-method-popup__body{
    background-color: rgba(0,0,0,0.35);
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 101;
    display: flex;
    justify-content: center;
    align-items: center;
    .ss-pay-method__body{
        .ss-pay-item__body{
            //border: 1px solid #ffc200;
            background-color: #FFFFFF;
            padding: 26rem;
            margin-bottom: 42rem;
            border-radius: 8rem;
            .ss-pay-title{
                font-size: 18rem;
                line-height: 0;
                margin: 0 0 0 10rem;
                padding: 0;
                display: flex;
                justify-content: center;
                align-items: center;
            }

        }
        .ss-success__color{
            background-color: #409eff;
            color: #FFFFFF;
        }
        .ss-danger__color{
            background-color: #f56c6c;
            color: #FFFFFF;
        }
    }
}
</style>