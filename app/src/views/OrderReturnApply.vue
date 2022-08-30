<template>
    <div class="ss-drawer-form__body" v-if="orderItem">
        <div class="ss-order-details__body">
            <div>
                <span>支付流水:</span>
                <span>{{ orderItem.id }}</span>
            </div>
            <div>
                <span>订单号:</span>
                <span>{{ orderItem.outTradeNo }}</span>
            </div>
            <div>
                <span>订单状态:</span>
                <span>{{ statusMessage }}</span>
            </div>
            <div>
                <span>退款路径:</span>
                <span>原支付路径退回</span>
            </div>
            <div>
                <span>退单菜品数量:</span>
                <span>{{ orderItem.productTotalNumber }}</span>
            </div>
            <div>
                <span>实际支付金额:</span>
                <span>{{ orderItem.billPrice }}</span>
            </div>
            <div>
                <span>退款金额:</span>
                <span>{{ orderItem.billPrice}}</span>
            </div>
        </div>
        <div class="ss-order-not__body">
            <div>申请人姓名:</div>
            <el-input v-model="buyerName" placeholder="请输入申请人姓名"></el-input>
        </div>
        <div class="ss-order-not__body">
            <div>联系手机号:</div>
            <el-input @input="numberCheckingEvent" v-model="buyerPhoneNumber"  placeholder="请输入联系手机号"></el-input>
        </div>
        <div class="ss-order-not__body">
            <div>退款原因:</div>
            <el-input
                type="textarea"
                placeholder="请输入退款原因"
                v-model="buyerReason"
                maxlength="30"
                show-word-limit/>
        </div>
        <div class="ss-order-submit-button__body">
            <el-button class="ss-submit-button" type="primary" @click.stop.prevent="onSubmit">提交</el-button>
        </div>
    </div>
</template>

<script>
import {Notify} from 'vant';
import {Loading} from "element-ui";
import orderTradeRefundApi from "../api/orderTradeRefundApi";

const PAGE_NAME = "退款申请页面"
export default {
    name: "OrderReturnApply",
    data(){
        return{
            orderItem:undefined,
            /** 退款原因不能为空 */
            buyerReason: "测试申请理由",
            /** 退款申请人 */
            buyerName: "莉莉丝",
            /** 退款申请人的联系电话 */
            buyerPhoneNumber: "19112345678",
        }
    },
    computed: {
        statusMessage(){
            if (this.orderItem){
                if (this.orderItem.id){
                    switch (this.orderItem.status){
                        case 2: return "已出单";
                        case 3: return "待取餐";
                        case 4: return "派送中";
                        case 5: return "已送达";
                        case 6: return "完成";
                    }
                }
            }
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
        this.drawer = false;
        console.log(PAGE_NAME,"组件导航守卫路由器离开");
        next();
    },
    methods:{

        initData(){
            if (this.$route.params) if (this.$route.params.id)  this.orderItem = this.$route.params;
            if (!this.orderItem){ Notify("无权操作");this.$router.replace( {path:"/order"}); }
            if (this.orderItem) {
                console.log("status,",this.orderItem.status);
                if (this.orderItem.status >= 2 && 6 <= this.orderItem.status){
                    Notify("您选择订单无法操作")
                    this.$router.replace("/order");
                }
            }
        },

        // 发起退款申请
        onSubmit(){
            if (this.orderItem){
                const { id } = this.orderItem;
                if (id){
                    this.drawer = false;
                    this.$dialog.confirm({
                        title: '警告提示',
                        message: '确认要提交申请退款吗？',
                    }).then(async () => {
                        const vo = {
                            orderId: id,// 订单号
                            buyerName: this.buyerName,// 退款原因不能为空
                            buyerPhoneNumber: this.buyerPhoneNumber,// 退款申请人
                            buyerReason: this.buyerReason,// 退款申请人的联系电话
                        }
                        let loadingInstance = Loading.service({ fullscreen: true });
                        try {
                            const {status,code,message} = await orderTradeRefundApi.returnApply(vo);
                            if (status){
                                loadingInstance.close()
                                this.$ssToast("提交成功,正在去订单页")
                                this.$router.replace("/order");
                                return
                            }
                        }catch (e){}
                        loadingInstance.close()
                        Notify("未知错误,请重试")
                        this.$router.replace("/order");
                    }).catch(() => { });
                }else{
                    Notify("未知错误,请重试")
                    this.$router.replace("/order");
                }
            }else{
                Notify("未知错误,请重试")
                this.$router.replace("/order");
            }
        },


        // 检查手机号
        numberCheckingEvent(e){
            const reg =  /^[1-9]\d*$/;
            if (e){
                if(!reg.test(e)){
                    this.$message({
                        message: '请输入数字',
                        type: 'warning'
                    });
                    this.refundApplicantPhoneNumber = undefined;
                }
            }
        },

    }
}
</script>

<style lang="scss" scoped>
.ss-drawer-form__body{
    font-size: 16rem;
    box-sizing: border-box;
    padding: 10rem;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    .ss-order-details__body{
        line-height: 38rem;
        margin-bottom: 6rem;
        span:nth-child(1){
            padding-right: 6rem;
        }
    }
    .ss-order-not__body{
        width: 100%;
        div:nth-child(1){
            margin-bottom: 8rem;
        }
    }
    .ss-order-submit-button__body{
        margin-top: 18rem;
        width: 100%;
        .ss-submit-button{
            width: 100%;
        }
    }
}
</style>