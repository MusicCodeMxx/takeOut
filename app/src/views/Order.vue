<template>

    <van-pull-refresh v-model="pullRefreshLoading" :head-height="81" @refresh="onRefresh">
        <!-- 下拉提示，通过 scale 实现一个缩放效果 -->
        <template #pulling="props">
            <img
                class="doge"
                src="https://img01.yzcdn.cn/vant/doge.png"
                :style="{ transform: `scale(${props.distance / 80})`}"/>
        </template>

        <!-- 释放提示 -->
        <template #loosing>
            <img class="doge" src="https://img01.yzcdn.cn/vant/doge.png" />
        </template>

        <!-- 加载提示 -->
        <template #loading>
            <img class="doge" src="https://img01.yzcdn.cn/vant/doge-fire.jpg" />
        </template>

        <!--主体内容-->
        <!--列表滚动加载-->
        <div class="order">
            <ul v-infinite-scroll="loadActiveOrderDetailsList"
                :infinite-scroll-disabled="disabled">

                <li class="card" v-for="(obj,index) in orderDetailsListPage" :key="obj.id + index+new Date().getTime()">

                    <!--用户地址卡片-->
                    <div class="ss-address-details__body">
                        <div>
                            <div class="ss-address-details-value__body">
                                <span>送至</span>
                                <span v-text="obj['detail']"></span>
                            </div>
                            <div class="ss-address-details-user__body">
                                <span>{{ obj.consigneeName }}</span>
                                <span>{{ obj.phoneNumber }}</span>
                            </div>
                        </div>
                        <div class="ss-address-dateils-ico__body">
                            <i class="el-icon-info" @click.stop.prevent="onIconInfoEvent(obj)"></i>
                        </div>
                    </div>

                    <!-- 商品信息 -->
                    <div class="ss-card__body">
                        <!--订单状态-->
                        <div class="ss-order-steps__body">
                            <el-steps :active="obj.status"
                                      align-center
                                      :process-status="obj.steps.process"
                                      :finish-status="obj.steps.finish" >
                                <el-step title="待付款" icon="el-icon-s-finance"></el-step>
                                <el-step title="已出单" icon="el-icon-document-checked"></el-step>
                                <el-step title="待取餐" icon="el-icon-shopping-bag-2"></el-step>
                                <el-step title="派送中" icon="el-icon-truck"></el-step>
                                <el-step title="已送达" icon="el-icon-location-information"></el-step>
                                <el-step title="完成" icon="el-icon-eleme"></el-step>
                            </el-steps>
                        </div>


                        <div class="ss-order-message__body" v-if="obj.messagePrefix && obj.status !== 1" v-html="obj.messagePrefix"></div>
                        <div class="ss-order-message__body" v-if="obj.status === 1" style="display:flex;align-items: center;">
                            <span>请即时付款，</span>
                            <van-count-down :time="1800000" format="mm:ss" style="font-size: 16rem;color: orangered;padding-right: 3rem;"/>
                            <span>剩余分钟关闭订单</span>
                        </div>

                        <div class="ss-deatils__body">
                            <div v-for="(item,index) in obj.childOrderDetailVos" class="ss-order-deatils__body">
                                <!--左边-->
                                <div class="ss-deatils-left__body">
                                    <el-image
                                        style="width:86rem;border-radius:8px; height: 86rem"
                                        :src="prefixPath(item.imageDefUrl)"
                                        fit="fit">
                                        <div slot="error">
                                            <img src="../assets/images/noImg.png" style="width:90rem;height: 90rem"/>
                                        </div>
                                    </el-image>
                                </div>
                                <!--右边-->
                                <div class="ss-deatils-right__body">
                                    <div class="ss-deatils-left">
                                        <!--菜品名称-->
                                        <div class="ss-dish-name" v-text="item.name"></div>
                                        <!--口味-->
                                        <div class="ss-dish" style="flex: 1">
                                            <el-tag
                                                class="ss-tag"
                                                style="margin-right: 6rem"
                                                type="warning"
                                                color="#ffc200"
                                                size="small"
                                                effect="dark"
                                                v-for="(tag,index) in item.dishFlavorList"
                                                :key="index">{{ tag }}
                                            </el-tag>
                                        </div>
                                    </div>
                                    <div class="ss-deatils-right">
                                        <div class="ss-deatils-right-price__body">
                                            <span>￥</span>
                                            <span v-text="item.price"></span>
                                        </div>
                                        <!--当前总数量-->
                                        <div class="ss-deatils-right-amount__body">
                                            <span>X</span>
                                            <span v-text="item.amount"></span>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--订单信息页面-->
                        <div>
                            <el-collapse value="3">
                                <el-collapse-item title="订单信息" name="1">
                                    <div>订单号: {{obj.outTradeNo}}</div>
                                    <div v-if="obj.tradeNo">支付宝交易号: {{obj.tradeNo}}</div>
                                    <div v-if="obj.operationTime.createTime">订单创建时间：{{timeToformatDate(obj.operationTime.createTime)}}</div>
                                    <div v-if="obj.operationTime.paymentSuccess">支付成功时间：{{timeToformatDate(obj.operationTime.paymentSuccess)}}</div>
                                    <div v-if="obj.operationTime.cancel">订单取消时间：{{timeToformatDate(obj.operationTime.cancel)}}</div>
                                    <div v-if="obj.operationTime.timeOutCancel">订单关闭时间：{{timeToformatDate(obj.operationTime.timeOutCancel)}}</div>
                                    <div v-if="obj.operationTime.orderCompleted">订单完成时间：{{timeToformatDate(obj.operationTime.orderCompleted)}}</div>
                                    <div v-if="obj.operationTime.refund">退款申请时间：{{timeToformatDate(obj.operationTime.refund)}}</div>
                                    <div v-if="obj.operationTime.refundSuccess">退款完成时间：{{timeToformatDate(obj.operationTime.refundSuccess)}}</div>
                                </el-collapse-item>
                                <el-collapse-item title="价格明细" name="2">
                                    <div class="ss-price-detail__item">
                                        <div>商品总价&nbsp;<span style="font-size: 12rem">(共{{obj.productTotalNumber}}件)</span></div>
                                        <div>￥{{obj.originalPrice}}</div>
                                    </div>
                                    <div class="ss-price-detail__item">
                                        <div>运费</div>
                                        <div>￥{{obj.freightPrice }}</div>
                                    </div>
                                    <div class="ss-price-detail__item">
                                        <div>满减优惠</div>
                                        <div>￥{{obj.couponPrice}}</div>
                                    </div>
                                    <div  class="ss-price-detail__item">
                                        <div>合计</div>
                                        <div>￥{{obj.billPrice}}</div>
                                    </div>
                                </el-collapse-item>
                                <el-collapse-item title="备注" disabled name="3" v-if="obj.remark">
                                    {{obj.remark}}
                                </el-collapse-item>
                            </el-collapse>
                        </div>


                        <div>
                            <div class="ss-order-payAmunt__body">
                                <span>实际付</span>
                                <span>￥</span>
                                <span v-text="obj.billPrice"></span>
                            </div>
                        </div>

                        <div v-if="obj.status===1 || obj.status===6 || obj.status===7 || obj.status===8  "
                             style="display: flex; align-items: center;justify-content: flex-end;padding: 12rem 10rem 6rem 0;">
                            <el-button v-show="obj.status === 1" type="primary" @click.stop.prevent="payImmediately(obj)">立即支付</el-button>
                            <el-button v-show="obj.status === 6||obj.status === 7 || obj.status===8" type="primary"
                                       @click.stop.prevent="reOrderDetailsEvent(obj)">再来一单</el-button>
                            <el-button v-show="obj.status === 5 " type="primary">确认收货</el-button>
                            <el-button v-show="obj.status === 6 " type="primary">评价</el-button>
                        </div>

                    </div>


                </li>

            </ul>
            <p v-show="loading">加载中...</p>
            <p v-show="noMore" style="font-size: 22rem;color: red;text-align: center;margin-bottom: 20rem;">没有更多了</p>

            <info-method-popup ref="infoMethodPopup" @confirm="onConfirm"></info-method-popup>
        </div>

    </van-pull-refresh>

</template>


<script>

import {Notify} from "vant";
import {Loading} from 'element-ui';
import dateFomatUtils from "../utils/dateFomatUtils";
import orderApi from "../api/orderApi";
import paymentApi from "@/api/paymentApi";
// import userAddressBookApi from "../api/userAddressBookApi";
import InfoMethodPopup from "../components/InfoMethodPopup";
import TestPopupMessage from "../components/TestPopupMessage";

const PAGE_NAME = "订单列表页面"
    export default {
        name: "order",
        components:{ InfoMethodPopup, TestPopupMessage },
        data() {
          return {
              stepsArr: [{ finish:'finish',process:'wait'}, { finish:'error',process:'error' }],
              finishTime: undefined,
              orderDetailsListPage: undefined,
              finished: false,
              current: 0,
              size: 3,
              pages: 1,
              total: 1,
              // 滚动加载
              loading: false,
              // 提示没有更多了
              noMore: false,
              // 下拉加载
              pullRefreshLoading: false,
          }
        },
        computed: {
            disabled () {
                console.log("disabled检查");
                return this.loading || this.noMore
            }
        },
        mounted() {
            this.init();
        },
        beforeRouteEnter(to, from, next) {
            console.log(PAGE_NAME+"路由器进入");
            const { path } = from;
            next(vm => {
                console.log("from=>>",path);
                // 来自退款页面的重新刷新
                if(path){
                    if (path.includes('/order/return/apply')) {
                        vm.init();
                    }
                }
            });
        },
        watch:{
            orderDetailsListPage: {
                //true为进行深度监听,false为不进行深度监听
                deep: true,
                handler(val) {
                    if (val) val.forEach(obj => {
                        if (obj.childOrderDetailVos) {
                            obj.childOrderDetailVos.forEach(co => {
                                if (co.value) co['dishFlavorList'] = co.value.split(',');
                            })
                            this.checkStatus(obj);
                        }
                    })
                }
            }
        },

        methods:{


            init(){
                this.loadActiveOrderDetailsList();
                this.getFinishTime();
            },

            // 加载当前活动状态的订单详情
            async loadActiveOrderDetailsList(){
                if (this.current === this.pages) {
                    this.noMore = true;
                    Notify("到底部了");
                }
                this.loading = true
                Notify({type:"primary",message:"加载"})
                try {
                    let vo = {current: this.current + 1, size: this.size}
                    const { status,message,data } = await orderApi.pageList(vo);
                    if (status){
                        if (data) {
                            this.loading = false
                            this.current = data.current;
                            this.pages = data.pages;
                            this.size = data.size;
                            this.total = data.total;
                            if (this.orderDetailsListPage){
                                this.orderDetailsListPage.push(...data.records);
                            }else{
                                this.orderDetailsListPage = data.records;
                            }
                            if(data.pages === data.current){
                                this.noMore = true;
                                Notify({type: "warning", message: "到底部了"});
                            }
                        }
                    }else{
                        Notify({type: 'warning', message: message})
                    }
                }catch (e) {}
                // 加载状态结束
                this.loading = false
            },

            // 订单立即支付
            async payImmediately(e){
                const { id } = e;
                console.log("payImmediately=>",e);
                if(!id){
                    Notify("订单不存在,请重新刷新")
                    return;
                }
                let loadingInstance = Loading.service({ fullscreen: true });
                try {
                    const {status,code,message,data} = await paymentApi.payOrderById(id);
                    if (status){
                        if (data){
                            loadingInstance.close();
                            const divForm = document.getElementsByTagName('div')
                            if (divForm.length)  document.body.removeChild(divForm[0])
                            const div = document.createElement('div'); // 创建新的 div
                            div.innerHTML = data // data 就是接口返回的 form 表单字符串
                            document.body.appendChild(div);
                            document.forms[0].setAttribute('target', '_self'); // 新开窗口跳转
                            document.forms[0].submit();// 提交
                            return
                        }
                    }
                    loadingInstance.close();
                    Notify({type: "warning", message: message});
                }catch (e) {
                    loadingInstance.close();
                    Notify("未知错误,请重新页面,若无法解决请联系客服");
                }
            },

            onIconInfoEvent(e){
                this.$refs.infoMethodPopup.myDrawer = true;
                this.$refs.infoMethodPopup.myStatus = e.status;
                this.$refs.infoMethodPopup.myOrderId = e.id;
                this.$refs.infoMethodPopup.myOrder = e;

            },

            onConfirm(e,orderId,orderItem){
                switch (e) {
                    case 0:
                        this.toCustomerServicePage(orderItem);
                        return;
                    case 1:
                        this.deleteTheOrder(orderId);
                        break;
                    case 2:
                        this.cancelTheOrder(orderId);
                        break;
                    case 3:
                        this.toRefundApplicationPage(orderItem);
                        break;
                }
            },

            // 去客服页面
            toCustomerServicePage(orderItem){
                this.$router.push({
                    name: 'customerService',
                    params: orderItem
                })
            },

            // 退款去申请页面
            toRefundApplicationPage(orderItem){
                if (orderItem) {
                    //  2付款成功, 3制作, 4派送, 5派送完成
                    switch (orderItem.status){
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            this.$router.push({
                                name: 'orderReturnApply',
                                params: orderItem
                            })
                            return;
                        default:
                            return;
                    }
                }else{
                    Notify("未知错误,请重试");
                }
            },

            // 重新下单
            reOrderDetailsEvent(obj){
                this.$router.replace({
                    name: 'ReOrderDetails',
                    params: obj,
                })
            },

            // 删除订单
            deleteTheOrder(id){
                this.$dialog.confirm({
                    title: '警告提示',
                    message: '确认要删除该订单吗？',
                }).then( async () => {
                    let loadingInstance = Loading.service({ fullscreen: true });
                    if (this.orderDetailsListPage) {
                        try {
                            const {status, message} = await orderApi.delete(id);
                            if (status){
                                const index = this.orderDetailsListPage.findIndex(od => od.id === id);
                                this.orderDetailsListPage.splice(index,1);
                                loadingInstance.close();
                                Notify({type:"success",message:"删除成功"});
                            }else{
                                loadingInstance.close();
                                Notify({type:"warning",message});
                            }
                        } catch (e) {}
                    }
                    loadingInstance.close();
                }).catch(() => {});
            },

            // 取消订单
            cancelTheOrder(id){
                this.$dialog.confirm({
                    title: '警告提示',
                    message: '确认要取消该订单吗？',
                }).then( async () => {
                    if (this.orderDetailsListPage) {
                        try {
                            const {status, message} = await orderApi.cancel(id);
                            if (status){
                                const index = this.orderDetailsListPage.findIndex(od => od.id === id);
                                this.orderDetailsListPage[index]['status'] = 7;// 改为已取消
                                this.orderDetailsListPage[index]['steps'] = this.stepsArr[1];
                                this.orderDetailsListPage[index]['messagePrefix'] = "<span class='ss-message__time ss-message__time__warning'>您已取消订单</span>";
                            }else{
                                Notify({type:"warning",message});
                            }
                        } catch (e) {
                            Notify("无法操作")
                        }

                    }
                }).catch(() => {})
            },

            //网络图片路径转换
            prefixPath(path) {
                return "http://10.10.10.55:9000/reggie/static/item/" + path;
            },

            //获取送达时间
            getFinishTime(){
                let now = new Date()
                let hour = now.getHours() +1
                let minute = now.getMinutes()
                if(hour.toString().length <2){
                    hour = '0' + hour
                }
                if(minute.toString().length <2){
                    minute = '0' + minute
                }
                this.finishTime = hour + ':' + minute
            },

            checkStatus(obj){
                if (obj.status) switch (obj.status){
                    case 1:
                        obj.steps = this.stepsArr[0];
                        if (obj.operationTime){
                            obj['distance'] = dateFomatUtils.distanceTime(obj.operationTime.createTime)
                        }
                        if (0 >= obj['distance']) {
                            obj['status'] = 8;
                            this.checkStatus(obj);
                        }
                        break;
                    case 2:
                        obj.steps = this.stepsArr[0];
                        obj['messagePrefix'] = `当前已派单给商家,预计<span class="ss-message__time">${this.finishTime}</span>送达`;
                        break;
                    case 3:
                        obj.steps = this.stepsArr[0];
                        obj['messagePrefix'] = `当前已烹饪完成,等待专员取餐,预计<span class="ss-message__time">${this.finishTime}</span>送达`;
                        break;
                    case 4:
                        obj.steps = this.stepsArr[0];
                        obj['messagePrefix'] = `当前派送中了,很快送到您手中,预计<span class="ss-message__time">${this.finishTime}</span>送达`;
                        break;
                    case 5:
                        obj.steps = this.stepsArr[0];
                        obj['messagePrefix'] = "当前已送达,请检查商品是否完好";
                        break;
                    case 6:
                        obj.steps = this.stepsArr[0];
                        obj['messagePrefix'] = "当前完成,祝您用餐愉快,满意请给骑手五星好评哦~";
                        break;
                    case 7:
                        obj.steps = this.stepsArr[1];
                        obj['messagePrefix'] = "<span class='ss-message__time ss-message__time__warning'>订单已被取消</span>";
                        break;
                    case 8:
                        obj.steps = this.stepsArr[1];
                        obj['messagePrefix'] = "未在指定的时间内完成支付,<span class='ss-message__time ss-message__time__warning'>系统自动取消该订单</span>";
                        break;
                    case 9:
                        obj.steps = this.stepsArr[1];
                        obj['messagePrefix'] = "您已申请<span class='ss-message__time ss-message__time__warning'>退款</span>";
                        break;
                    case 10:
                        obj.steps = this.stepsArr[1];
                        obj['messagePrefix'] = "<span class='ss-message__time ss-message__time__warning'>商家已同意退款，请注意查收</span>";
                        break;
                }
            },


            // 下拉刷新
            async onRefresh() {
                // 重置参数
                this.current = 0;
                this.size = 3;
                this.pages = 1;
                this.total = 1;
                this.orderDetailsListPage = undefined;
                // 发起请求
                await this.init();
                // 响应
                Notify({type:'success',message:"刷新成功"});
                this.pullRefreshLoading = false;
            },

            // 日期格式化
            timeToformatDate(e){
                return dateFomatUtils.dateFormat(e,"yyyy-MM-dd hh:mm:ss")
            }
        }
    }
</script>


<style>
.ss-message__time{
    font-size: 14rem;
    font-weight: 600;
    padding: 0 3rem;
}
.ss-message__time__warning{
    color: red;
}
</style>

<style lang="scss" scoped>
.order{
    height: 100vh;
    overflow: hidden auto;
    box-sizing:border-box;
    padding-bottom: 50rem;
    .card{
        box-sizing:border-box;// 怪异盒子模型
        position: relative;
        margin-bottom: 24rem;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        border: 1px solid #EBEEF5;
        background-color: #FFF;
        overflow: hidden;
        transition: .3s;
        .ss-address-details__body{
            padding: 14rem 0 2rem 14rem;
            font-size: 18rem;
            display: grid;
            grid-template-columns: 14fr 2fr;
            .ss-address-details-value__body{
                width: 302rem;
                overflow: hidden;
                white-space: nowrap;
                -o-text-overflow: ellipsis;
                text-overflow: ellipsis
            }
            .ss-address-details-user__body{
                padding-top: 3rem;
                font-size: 15rem;
                span:nth-child(2){
                    padding-left: 6rem;
                }
            }
            .ss-address-dateils-ico__body{
                display: flex;
                justify-content: space-between;
                align-items: center;
                i:nth-child(1){
                    color: #ffc200;
                    font-size: 28rem;
                    margin-bottom: 10rem;
                }
            }
        }
        .ss-card__body{
            padding: 8rem;
            // 每个卡片的顶部的进度显示状态
            .ss-order-steps__body {
                padding: 10rem 0;
            }
            .ss-order-message__body{
                background-color: #ebeef5;
                padding: 8rem 12rem;
                padding-right: 0;
                border-radius: 8rem;
                font-size: 13rem;
                letter-spacing: 0;
            }

            .ss-deatils__body{
                padding: 10rem 0;
                // 商品列表
                .ss-order-deatils__body{
                    //border-top: 1px solid #EBEEF5;
                    display: grid;
                    grid-template-columns: 1fr 3fr;
                    //padding: 10rem 0;
                    padding: 4rem 0;
                    .ss-deatils-left__body{}
                    .ss-deatils-right__body{
                        display: grid;
                        grid-template-columns: 7fr 3fr;
                        .ss-deatils-left{
                            padding-left: 10rem;
                            .ss-dish-name{
                                height: 22rem;
                                opacity: 1;
                                font-size: 16rem;
                                font-weight: 600;
                                text-align: left;
                                color: #333333;
                                line-height: 22rem;
                                letter-spacing: 0;
                                margin-bottom: 5rem;
                                overflow: hidden;
                                text-overflow: ellipsis;
                                white-space: nowrap;
                                width: 175rem;
                            }
                            .ss-dish{
                                margin: 6rem 0;
                            }
                        }
                        .ss-deatils-right{
                            text-align: right;
                            letter-spacing: 0;
                            .ss-deatils-right-price__body{
                                font-weight: 600;
                                span:nth-child(1){
                                    font-size: 13rem;
                                }
                                span:nth-child(2){
                                    font-size: 18rem;
                                }
                            }
                            .ss-deatils-right-amount__body{
                                margin-top: 8rem;
                                color: #787878;
                                span:nth-child(1){
                                    font-size: 10rem;
                                }
                                span:nth-child(2){
                                    font-size: 16rem;
                                }
                            }
                        }
                    }
                }
            }
            // 备注
            .ss-remark{
                padding: 10rem;
            }
            .ss-order-payAmunt__body{
                font-weight: bolder;
                padding: 15rem 12rem 5rem 0;
                font-size: 16rem;
                letter-spacing: 0;
                text-align: right;
                span:nth-child(2){
                    font-size: 14rem;
                }
                span:nth-child(3){
                    font-size: 20rem;
                }
            }
        }
    }
    //.card:last-child{
    //    margin-bottom: 50rem;
    //}
    .card::before {
        position: absolute;
        right: 0;
        bottom: 0;
        left: 0;
        height: 3rem;
        background: -webkit-repeating-linear-gradient(135deg, #ff6c6c 0, #ff6c6c 20%, transparent 0, transparent 25%, #1989fa 0, #1989fa 45%, transparent 0, transparent 50%);
        background: repeating-linear-gradient(-45deg, #ff6c6c 0, #ff6c6c 20%, transparent 0, transparent 25%, #1989fa 0, #1989fa 45%, transparent 0, transparent 50%);
        background-size: 106rem;
        content: '';
    }

}
.ss-empt__body{
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    display: flex;
    align-items: center;
    justify-content: center;
}

.doge {
    width: 140px;
    height: 72px;
    margin-top: 8px;
    margin-bottom: 8rem;
    padding-bottom: 10rem;
    border-radius: 4px;
}

.ss-price-detail__item{
    //font-size: 16rem;
    padding: 2rem 6rem 2rem 0 ;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.ss-price-detail__item div:nth-child(2){
    color: #e94e3c;
}
</style>
