<template>
    <div class="ss-button-bar__body" :style="`bottom: ${bottomDistance?bottomDistance:0}rem`">
        <div class="ss-cart-img__body" @click.stop.prevent="onCartEvent">
            <div class="ss-goods-num__body"  v-text="productTotalNumber"></div>
            <div class="imgCartActive"></div>
        </div>
        <!--价格区域-->
        <div class="ss-center-price__body">
            <div class="ss-bill-price__body">
                <span v-show="productTotalNumber" class="ss-price-1">已选{{productTotalNumber}}件，</span>
                <span class="ss-price-2">合计:</span>
                <span class="ss-price-3">￥</span>
                <span class="ss-price-4" v-text="billPrice"></span>
            </div>
            <div class="ss-coupon-price__body"
                 v-if="enableCouponPrice"
                 @click.stop.prevent="onCouponDetailEvent">
                <span v-show="couponPrice">共减￥{{couponPrice}}</span>
                <span>&nbsp;</span>
                <span>&nbsp;</span>
                <span>查看优惠券</span>
                <span>&nbsp;</span>
                <van-icon name="arrow-up" />
            </div>
        </div>
        <!--按钮区域-->
        <div class="ss-button-to-submit__body">
            <van-button class="ss-to-submit__body"
                        :disabled="0>=billPrice"
                        color="#ffc200"
                        round
                        :loading="loading"
                        type="info"
                        @click="onSubmitEvent"
                       >结算</van-button>
        </div>

    </div>
</template>

<script>
export default {
    name: "ShoppingCartButtonBar",
    props:{
        enableCouponPrice:{
            type: Boolean,
            default: false
        },
        bottomDistance:{
            type:[Number,String],
            default: '0'
        },
        // 优惠价格
        couponPrice:{
            type: Number,
            // required: true,
            default: 0
        },
        // 实际需要支付的价格
        billPrice:{
            type: Number,
            // required: true,
            default: 0
        },
        // 产品数量
        productTotalNumber:{
            type: Number,
            // required: true,
            default: 0
        },
        // 按钮显示加载
        loading:{
            type: Boolean,
            default: false
        },

    },
    data(){
        return{}
    },
    methods:{
        onSubmitEvent(){
            // this.loadingFlag = true;
            this.$emit('onSubmit',true);
        },

        // 点击购物车事件
        onCartEvent(){
            this.$emit('onCart',true);
        },

        // 点击优惠券事件
        onCouponDetailEvent(){
             this.$emit('onCouponDetail',true);
        },
    }
}
</script>

<style scoped lang="scss">

.ss-button-bar__body{
    font-size: 14rem;
    height: 60rem;
    background: #FFFFFF;
    margin: 0;
    //bottom: 46.32rem;
    left: 0;
    right: 0;
    position: fixed;
    transform: translate(-0%, -0%);
    display: grid;
    grid-template-columns: 4fr 8fr 4fr;
    // 购物车样式
    .ss-cart-img__body{
        position: relative;
        .ss-goods-num__body {
            min-width: 16rem;
            min-height: 16rem;
            background: #e94e3c;
            border-radius: 50%;
            letter-spacing: 0;
            color: #ffffff;
            padding: 2rem;
            position: absolute;
            left: 62rem;
            top: 4rem;
            display: flex;
            align-items: center;
            justify-content: center;

        }
        .imgCartActive {
            position: absolute;
            width: 64rem;
            height: 64rem;
            left: 20rem;
            top: -7.3rem;
            background-image: url("../assets/images/cart_active.png");
            background-size: 64rem 64rem;
        }
    }
    // 中间价格展示样式
    .ss-center-price__body{
        font-size: 14rem;
        padding-right: 10rem;
        display: flex;
        justify-content: center;
        align-items: flex-end;
        flex-direction: column;
        .ss-bill-price__body{
            color: #e94e3c;
            .ss-price-1{
                color: #787878;

            }
            .ss-price-2{
                color: #000000;
                font-size: 15rem;
            }
            .ss-price-3{
                font-size: 12rem;
            }
            .ss-price-4{
                letter-spacing: 1px;
                font-size: 21rem;
                font-weight: 600;
            }
        }
        .ss-coupon-price__body{
            margin-top: 2rem;
            font-weight: 300;
            color: #e94e3c;
        }
    }

    // 结算按钮样式
    .ss-button-to-submit__body{
        display: flex;
        justify-content: flex-start;
        align-items: center;
        padding: 4rem 0;
        .ss-to-submit__body{
            font-size: 16rem;
            width: 87rem;
            height: 38rem;
            letter-spacing: 2rem;
            //padding: 0 26rem;
        }
    }

}

</style>