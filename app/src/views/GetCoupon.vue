<template>
    <div>
        <template v-if="couponPageList">
            <div v-for="cp in couponPageList" :key="cp.id ||cp.batchId" class="stamp" :class="couponColorClass(cp.price)" >
                <div class="par"  @click.stop.prevent="onCouponDetailEvent(cp)">
                    <p v-text="cp.couponName"></p>
                    <sub class="sign">￥</sub>
                    <span v-text="cp.price"></span>
                    <sub>全场满减</sub>
                    <p>订单满{{ cp.threshold }}元即可使用</p></div>
                <div class="copy">
                    满减券
                    <p>{{dateFormat(cp.startTime)}}<br>{{dateFormat(cp.endTime)}}</p>
                    <a @click.stop.prevent="getCouponEvent(cp)">立即领取</a>
                </div>
                <div class="card__body"></div>
                <div class="ss-no" v-if="cp.id" @click.stop.prevent="onCouponDetailEvent(cp)">
                    <span>已领取</span>
                </div>
            </div>
        </template>

        <div class="popup__body" v-show="popupShow"
             @click.stop.prevent="popupShow = false">
            <div class="popup-message__body"
                 @click.stop.prevent="" v-text="popupMessage"></div>
        </div>

    </div>
</template>

<script>

import {Loading} from 'element-ui';
import {Notify, Toast} from 'vant';
import dateFomatUtils from "../utils/dateFomatUtils";
import couponBatchApi from "@/api/couponBatchApi";

export default {
    name: "GetCoupon",
    data(){
        return{
            couponPageList:undefined,
            popupShow:false,
            popupMessage:'暂无说明',
        }
    },

    created() {
        this.init();
    },

    methods:{

        init(){
            this.loadCouponBatchPageList();
        },

        async loadCouponBatchPageList(){
            let loadingInstance = Loading.service({ fullscreen: true });

            try {
                const {status, message, data} = await couponBatchApi.list();
                if(status){
                    loadingInstance.close();
                    this.couponPageList = data;
                }else{
                    Notify(message);
                }
            }catch (e){}
            loadingInstance.close();
        },

        dateFormat(v1){
            return dateFomatUtils.dateFormat(v1,"yyyy-MM-dd hh:mm")
        },

        couponColorClass(v1){
            // 10元以下
            if (10 >= v1){
                return "stamp01";
            } else if (v1 >= 11 && v1 <= 30) {
                // 11元 ~ 30元
                return "stamp02"
            } else if (v1 >= 31 && v1 <= 50) {
                // 31元 ~ 50元
                return "stamp03"
            } else {
                // 51元以上
                return "stamp04"
            }
        },

        async getCouponEvent(couponObj){
            if (couponObj){
                if (couponObj.batchId){
                    let loadingInstance = Loading.service({ fullscreen: true });
                    try {
                        const {status, message, data} = await couponBatchApi.receiveCouponById(couponObj.batchId);
                        if(status){
                            couponObj.id = data;
                            loadingInstance.close();
                            Toast.success('已领取成功');
                        }else{
                            Notify(message);
                        }
                    }catch (e){}
                    loadingInstance.close();
                }
            }
        },

        onCouponDetailEvent(couponObj){
            this.popupShow = true;
            this.popupMessage = couponObj.description;
        },
    }
}
</script>

<style scoped>
.popup__body{
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    background-color: rgba(0,0,0,0.35);
}
.popup-message__body{
    background-color: #FFFFFF;
    box-sizing: border-box;
    width: 80vw;
    min-height: 30vh;
    max-height: 60vh;
    padding: 16rem;
    border-radius: 8rem;
    overflow: hidden auto;
    font-size: 14rem;
    box-shadow: 0 2px 12px rgba(0,0,0,0.35);
}
.stamp {
    margin: 10rem auto;
    display: grid;
    grid-template-columns: 2fr 1fr;
    /*width: 387px;*/
    width: 342px;
    height: 140px;
    padding: 0 10px;
    position: relative;
    overflow: hidden;
}

.stamp:before {
    content: '';
    position: absolute;
    top: 0;
    bottom: 0;
    left: 10px;
    right: 10px;
    z-index: -1;
}

.stamp:after {
    content: '';
    position: absolute;
    left: 10px;
    top: 10px;
    right: 10px;
    bottom: 10px;
    /*box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.5);*/
    z-index: -2;
}

.stamp .card__body {
    z-index: 1;
    position: absolute;
    left: 20%;
    top: 45px;
    height: 190px;
    width: 390px;
    background-color: rgba(255, 255, 255, .15);
    transform: rotate(-30deg);
}

.stamp .par {
    z-index: 100;
    height: 140rem;
    box-sizing:border-box;
    float: left;
    padding: 0 15px 0;
    /*width: 220px;*/
    width: 100%;
    border-right: 2px dashed rgba(255, 255, 255, .3);
    text-align: left;
}

.stamp .par p {
    font-size: 12rem;
    color: #fff;
}

.stamp .par span {
    font-size: 50px;
    color: #fff;
    margin-right: 5px;
}

.stamp .par .sign {
    font-size: 34px;
}

.stamp .par sub {
    font-size: 12rem;
    position: relative;
    top: -5px;
    color: rgba(255, 255, 255, .8);
}

.stamp .copy {
    z-index: 100;
    /*display: inline-block;*/
    padding: 21px 14px;
    width: 100px;
    vertical-align: text-bottom;
    font-size: 30px;
    color: rgb(255, 255, 255);
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    align-items: flex-start;
}

.stamp .copy p {
    z-index: 100;
    font-size: 16px;
    margin-top: 15px;
}

.stamp .copy a {
    text-align: center;
    z-index: 100;
    background-color: #fff;
    color: #333;
    font-size: 14px;
    text-decoration: none;
    padding: 5px 10px;
    border-radius: 3px;
    display: block;
}

.stamp01 {
    background: #F39B00;
    background: radial-gradient(rgba(0, 0, 0, 0) 0, rgba(0, 0, 0, 0) 5px, #F39B00 5px);
    background-size: 15px 15px;
    background-position: 9px 3px;
}

.stamp01:before {
    background-color: #F39B00;
}

.stamp01 .copy {
    z-index: 2;
    padding: 10px 6px 10px 12px;
    font-size: 24px;
}

.stamp01 .copy p {
    /*font-size: 14px;*/
    font-size: 12rem;
    margin-top: 5px;
    margin-bottom: 8px;
}

.stamp01 .copy a {
    background-color: #fff;
    color: #333;
    font-size: 14px;
    text-decoration: none;
    padding: 5px 10px;
    border-radius: 3px;
    display: block;
    z-index: 6;
}


.stamp02 {
    background: #D24161;
    background: radial-gradient(transparent 0, transparent 5px, #D24161 5px);
    background-size: 15px 15px;
    background-position: 9px 3px;
}

.stamp02:before {
    background-color: #D24161;
}

.stamp02 .copy {
    z-index: 2;
    padding: 10px 6px 10px 12px;
    font-size: 24px;
}

.stamp02 .copy p {
    /*font-size: 14px;*/
    font-size: 12rem;
    margin-top: 5px;
    margin-bottom: 8px;
}

.stamp02 .copy a {
    background-color: #fff;
    color: #333;
    font-size: 14px;
    text-decoration: none;
    padding: 5px 10px;
    border-radius: 3px;
    display: block;
    z-index: 6;
}

.stamp03 {
    background: #7EAB1E;
    background: radial-gradient(transparent 0, transparent 5px, #7EAB1E 5px);
    background-size: 15px 15px;
    background-position: 9px 3px;
}

.stamp03:before {
    background-color: #7EAB1E;
}

.stamp03 .copy {
    z-index: 2;
    padding: 10px 6px 10px 12px;
    font-size: 24px;
}

.stamp03 .copy p {
    /*font-size: 14px;*/
    margin-top: 5px;
    font-size: 12rem;
    margin-bottom: 8px;
}

.stamp03 .copy a {
    background-color: #fff;
    color: #333;
    font-size: 14px;
    text-decoration: none;
    padding: 5px 10px;
    border-radius: 3px;
    display: block;
    z-index: 6;
}

.stamp04 {
    background: #50ADD3;
    background: radial-gradient(rgba(0, 0, 0, 0) 0, rgba(0, 0, 0, 0) 4px, #50ADD3 4px);
    background-size: 15px 15px;
    background-position: 9px 3px;
}

.stamp04:before {
    background-color: #50ADD3;
}

.stamp04 .copy {
    z-index: 2;
    padding: 10px 6px 10px 12px;
    font-size: 24px;
}

.stamp04 .copy p {
    /*font-size: 14px;*/
    margin-top: 5px;
    font-size: 12rem;
    margin-bottom: 8px;
}

.stamp04 .copy a {
    background-color: #fff;
    color: #333;
    font-size: 14px;
    text-decoration: none;
    padding: 5px 10px;
    border-radius: 3px;
    display: block;
    z-index: 6;
}
.ss-no{
    color: #FFFFFF;
    font-size: 30rem;
    z-index: 100;
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    display: flex;
    justify-content: center;
    align-items: center;
}
.ss-no span:nth-child(1){
    background-color: rgba(0,0,0,0.35);
    border-radius: 10rem;
    padding: 32rem 64rem;
}
</style>