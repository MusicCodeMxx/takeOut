<template>
    <div id="add_order" class="app" >
        <div class="divHead">
            <div class="divTitle">
                <i class="el-icon-arrow-left" @click="goBack"></i>订单支付
            </div>
        </div>
        <div class="divContent">
            <div class="divAddress">
                <div @click.prevent="toAddressPage">
                    <div class="address">{{address.detail}}</div>
                    <div class="name">
                        <span>{{address.consigneeName}}{{address.sex === '1' ? '先生':'女士'}}</span>
                        <span>{{address.phoneNumber}}</span>
                    </div>
                    <i class="el-icon-arrow-right"></i>
                </div>
                <div class="divSplit"></div>
                <div class="divFinishTime">&nbsp;预计{{finishTime}}&nbsp;送达</div>
            </div>
            <div class="order">
                <div class="title">订单明细</div>
                <div class="divSplit"></div>
                <div class="itemList">
                    <div class="item" v-for="(item,index) in cartData" :key="index">
                        <el-image :src="imgPathConvert(item.imageDefUrl)">
                            <div slot="error" class="image-slot">
                                <img src="../assets/images/noImg.png"/>
                            </div>
                        </el-image>
                        <div class="desc">
                            <div class="name">{{item.name}}</div>
                            <div class="ss-dish">
                                <el-tag style="margin-right: 6rem"
                                        type="success"
                                        size="mini"
                                        v-for="(tag,index) in item.dishFlavorList"
                                        :key="index">{{tag}}</el-tag>
                            </div>
                            <div class="numPrice">
                                <span class="num">数量: {{item.amount}}</span>
                                <div class="price">
                                    <span class="spanMoney">￥</span>{{item.price / 100}}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="note">
                <div class="title">备注</div>
                <van-field
                    v-model="note"
                    rows="2"
                    autosize
                    type="textarea"
                    maxlength="50"
                    placeholder="请输入您需要备注的信息"
                    show-word-limit
                />
            </div>
        </div>

        <div class="divCart">
            <div :class="{imgCartActive: cartData && cartData.length > 0, imgCart:!cartData || cartData.length<1}"></div>
            <div :class="{divGoodsNum:1===1, moreGoods:cartData && cartData.length > 99}" v-if="cartData && cartData.length > 0">{{ goodsNum }}</div>
            <div class="divNum">
                <span>￥</span>
                <span>{{goodsPrice}}</span>
            </div>
            <div class="divPrice"></div>
            <div :class="{btnSubmitActive: cartData && cartData.length > 0, btnSubmit:!cartData || cartData.length<1}" @click.stop.prevent="goToPaySuccess">立即付款</div>
        </div>

    </div>
</template>

<script>

import {Loading} from 'element-ui';
import {Notify} from 'vant';
import dateFomatUtils from "../utils/dateFomatUtils";
import randomNumberUtils from "../utils/randomNumberUtils";
import orderPaymentApi from "../api/orderPaymentApi";
import childOrderApi from "../api/childOrderApi";

const PAGE_NAME = '重新下单详细页';
export default {
    name: "ReOrderDetails",
    components:{ },
    data(){
        return {
            orderId: undefined,
            address:{
                phone:'',//手机号
                consignee:'',//姓名
                detail:'',//详细地址
                sex:'1',
                id:undefined
            },
            finishTime:'',//送达时间
            cartData:[],
            note:'', //备注信息
            submitUUID: undefined,// 幂等性
        }
    },
    computed:{

        goodsNum(){
            let num = 0
            if (this.cartData){
                if(this.cartData.length > 0){
                    this.cartData.forEach(item=>{
                        num += item.amount;
                    })
                }
            }
            return num >= 99?'99+':num;
        },

        goodsPrice(){
            let price = 0
            if (this.cartData){
                if(this.cartData.length > 0) {
                    this.cartData.forEach(item => {
                        price += ((item.price/100) * item.amount)
                    })
                }
            }
            return price;
        }
    },
    watch: {
        // 产品列表
        cartData:{
            // 深度监听
            deep: true,
            handler(val1,val2){
                // 商品列表
                if( val1 && val1.length > 0) val1.forEach(item => {
                    if (item.attributesValue) {
                        item['dishFlavorList'] = item.attributesValue.split(',');
                    }
                })
            }
        }
    },
    activated(){
        console.log(PAGE_NAME,"缓存激活");
    },
    deactivated() {
        console.info(PAGE_NAME,"缓存暂停")
    },
    created(){
        console.log(PAGE_NAME,"创建");
    },
    beforeMount() {
        console.log(PAGE_NAME,"初始化");
    },
    mounted(){
        console.log(PAGE_NAME,"初始化完成");
    },
    beforeDestroy() {
        console.log(PAGE_NAME,"销毁前");
    },
    destroyed() {
        console.log(PAGE_NAME,"销毁了");
    },
    beforeRouteEnter(to, from, next) {
        console.log(PAGE_NAME,"组件导航守卫路由器进入组件之前");
        const { fullPath }  =  from;
        const { params } = to;
        next(vm => {
            if (params){
                if(fullPath){
                    if (fullPath.includes('/order')) {
                       vm.initData(params);
                    }
                }else{
                    if (params.addressDetail){
                        if(params.addressDetail.params){
                            vm._data.address = params.addressDetail;
                        }
                    }
                }
            }
        });
    },
    beforeRouteLeave(to, from, next) {
        console.log(PAGE_NAME,"组件导航守卫路由器离开");
        next();
    },
    methods:{

        goBack(){
            this.$router.go(-1);
        },

        async initData(params){
            if (params){
                const { id } = params;
                this.orderId = id;
                let loadingInstance = Loading.service({ fullscreen: true });
                this.generateUUID();
                try {
                    const { status,code,message,data } = await childOrderApi.list(id);
                    if(status) {
                        if (data) {
                            const { addressBookVo, shoppingCartListVos } = data;
                            this.cartData = shoppingCartListVos;
                            if(addressBookVo){
                                this.address = addressBookVo;
                                this.getFinishTime();
                            }else{
                                this.$ssToast("您当前无配置地址");
                                this.toAddressPage();
                            }
                            loadingInstance.close();
                            return
                        }
                    }
                }catch (e){}
                loadingInstance.close();
            }
            Notify('当前无产品结算');
            this.$router.replace("/");
        },

        // 生成 uuid
        generateUUID(){
            this.submitUUID = dateFomatUtils.dateFormat(new Date(),"yyyyMMddhhmmss")+""+randomNumberUtils.uuid(5,10).toString();
            console.log(this.submitUUID,this.submitUUID.length);
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

        // 去添加地址页面
        toAddressPage(){
            window.requestAnimationFrame(()=>{
                //值为true需要缓存，为false不需要缓存("");
                this.$router.push('/user/address/book');
            })
        },

        // 提交订单,提示选择支付方式
        async goToPaySuccess(){
            if (!this.orderId){
                Notify("订单标识不存在")
                this.$router.replace("/order");
            }
            if (!this.address){
                Notify("用户地址信息不存在");
                return
             }
            let loadingInstance = Loading.service({ fullscreen: true });
            const { consigneeName, phoneNumber, detail, sex, label } = this.address;
            const vo = {
                orderId: this.orderId,
                /** 幂等性 */
                submitId: this.submitUUID,
                /** 收货人姓名 */
                consigneeName:consigneeName,
                /** 手机号 */
                phoneNumber:phoneNumber,
                /** 详细地址 */
                detail:detail,
                /** 性别,0女,1男 */
                sex:sex,
                /** 标签,0默认,1家,2学校,3公司,4其他*/
                label: label,
                /** 用户备注 */
                remark: this.note,
                /** 支付方式 */
                payMethod: 0,
                /** 地址绑定的主键 */
                addressBookId: this.address.id,
                /** 前端实际付价格 */
                payAmount: this.goodsPrice,
                // 优惠价格
            }
            // 发起支付
            try {
                const { status,code,message,data} = await orderPaymentApi.again(vo);
                if(status){
                    if (data){
                        loadingInstance.close();
                        // 后端返回的支付跳转地址
                        // 添加之前先删除一下，如果单页面，页面不刷新，添加进去的内容会一直保留在页面中，二次调用 form 表单会出错
                        const divForm = document.getElementsByTagName('div')
                        if (divForm.length) {
                            document.body.removeChild(divForm[0])
                        }
                        const div = document.createElement('div'); // 创建新的 div
                        div.innerHTML = data // data 就是接口返回的 form 表单字符串
                        document.body.appendChild(div);
                        // document.forms[0].setAttribute('target', '_blank'); // 新开窗口跳转
                        document.forms[0].setAttribute('target', '_self'); // 新开窗口跳转
                        document.forms[0].submit();// 提交
                        return
                    }
                }
                this.$message({ message: message, type: 'warning'});
                return;
            }catch (e) {
                console.log("提交错误信息=>",e);
            }
            loadingInstance.close();
            Notify("未知错误")
            this.$router.replace("/order");
        },

        // 网络图片路径转换
        imgPathConvert(path){
            return "http://10.10.10.55:9000/reggie/static/item/" + path;
        },

    }

}
</script>

<style scoped>
#add_order{
    background: #f3f2f7;
    height: 100vh;
}
#add_order .divHead {
    width: 100%;
    /*height: 88rem;*/
    height: 50rem;
    opacity: 1;
    background: #333333;
    position: relative;
}

#add_order .divHead .divTitle {
    font-size: 18rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #ffffff;
    line-height: 25rem;
    letter-spacing: 0;
    position: absolute;
    bottom: 13rem;
    width: 100%;
}

#add_order .divHead .divTitle i {
    position: absolute;
    left: 16rem;
    top: 50%;
    transform: translate(0, -50%);
}

#add_order .divContent {
    /*margin: 10rem 10rem 0 10rem;*/
    margin: 10rem 6rem 0 6rem;
    /*height: calc(100vh - 56rem - 110rem);*/
    height: calc(100vh - 10rem - 110rem);
    overflow-y: auto;
}

#add_order .divContent .divAddress {
    /*height: 120rem;*/
    height: 112rem;
    opacity: 1;
    background: #ffffff;
    border-radius: 6rem;
    position: relative;
    padding: 11rem 10rem 0 16rem;
}

#add_order .divContent .divAddress .address {
    height: 25rem;
    opacity: 1;
    font-size: 18rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: left;
    color: #20232a;
    line-height: 25rem;
    margin-bottom: 4rem;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 300rem;
}

#add_order .divContent .divAddress .name {
    height: 17rem;
    opacity: 1;
    font-size: 12rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #333333;
    line-height: 17rem;
}

#add_order .divContent .divAddress .name span:first-child {
    margin-right: 2rem;
}

#add_order .divContent .divAddress i {
    position: absolute;
    right: 14rem;
    top: 32rem;
}

#add_order .divContent .divAddress .divSplit {
    width: 100%;
    height: 1px;
    opacity: 1;
    border: 0;
    background-color: #ebebeb;
    margin-top: 14rem;
}

#add_order .divContent .divAddress .divFinishTime {
    height: 47rem;
    opacity: 1;
    /*font-size: 12rem;*/
    font-size: 16rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #333333;
    line-height: 47rem;
    margin-left: 2rem;
}

#add_order .divContent .order {
    background: #ffffff;
    border-radius: 6rem;
    margin-top: 10rem;
    margin-bottom: 10rem;
    padding: 3rem 10rem 7rem 16rem;
}

#add_order .divContent .order .title {
    height: 56rem;
    line-height: 56rem;
    opacity: 1;
    font-size: 16rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: left;
    color: #333333;
    letter-spacing: 0;
}

#add_order .divContent .order .divSplit {
    height: 1px;
    opacity: 1;
    background-color: #efefef;
    border: 0;
}

#add_order .divContent .order .itemList .item {
    display: flex;
}

#add_order .divContent .order .itemList .item .el-image {
    padding-top: 20rem;
    padding-bottom: 20rem;
    width: 64rem;
    height: 64rem;
}

#add_order .divContent .order .itemList .item .el-image img {
    width: 64rem;
    height: 64rem;
}

#add_order .divContent .order .itemList .item:first-child .desc {
    border: 0;
}

#add_order .divContent .order .itemList .item .desc {
    padding-top: 20rem;
    padding-bottom: 20rem;
    border-top: 2px solid #ebeef5;
    width: calc(100% - 64rem);
}

#add_order .divContent .order .itemList .item .desc .name {
    height: 22rem;
    opacity: 1;
    font-size: 16rem;
    font-family: PingFangSC, PingFangSC-Semibold;
    font-weight: 600;
    text-align: left;
    color: #20232a;
    line-height: 22rem;
    letter-spacing: 0;
    margin-left: 10rem;
    /*margin-bottom: 16rem;*/
    margin-bottom: 4rem;
}

#add_order .divContent .order .itemList .item .desc .numPrice {
    height: 22rem;
    display: flex;
    justify-content: space-between;
}

#add_order .divContent .order .itemList .item .desc .numPrice span {
    margin-left: 12rem;
    height: 20rem;
    opacity: 1;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #818693;
    line-height: 20rem;
    letter-spacing: 0;
    display: inline-block;
}

#add_order .divContent .order .itemList .item .desc .numPrice .price {
    font-size: 20rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #e94e3c;
}

#add_order
.divContent
.order
.itemList
.item
.desc
.numPrice
.price
.spanMoney {
    color: #e94e3c;
    font-size: 12rem;
}

#add_order .divContent .note {
    height: 164rem;
    opacity: 1;
    background: #ffffff;
    border-radius: 6px;
    margin-top: 11rem;
    padding: 3rem 10rem 10rem 11rem;
}

#add_order .divContent .note .title {
    height: 56rem;
    opacity: 1;
    font-size: 16rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: left;
    color: #333333;
    line-height: 56rem;
    letter-spacing: 0px;
    border-bottom: 2rem solid #efefef;
}

#add_order .divContent .note .van-cell {
    height: 103rem;
}

#add_order .divCart {
    width: 345rem;
    height: 44rem;
    opacity: 1;
    background: #000000;
    border-radius: 25rem;
    box-shadow: 0rem 3rem 5rem 0rem rgba(0, 0, 0, 0.25);
    margin: 0 auto;
    margin-top: 10rem;
    z-index: 3000;
    position: absolute;
    /* bottom: 35rem; */
    bottom: 6rem;
    left: 50%;
    transform: translate(-50%, 0);
}

#add_order .divCart .imgCartActive {
    background-image: url("../assets/images/cart_active.png");
}

#add_order .divCart .imgCart {
    background-image: url("../assets/images/cart.png");
}

#add_order .divCart > div:first-child {
    width: 60rem;
    height: 60rem;
    position: absolute;
    left: 11rem;
    bottom: 0;
    background-size: 60rem 60rem;
}

#add_order .divCart .divNum {
    font-size: 12rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #ffffff;
    letter-spacing: 0rem;
    position: absolute;
    left: 92rem;
    top: 10rem;
}

#add_order .divCart .divNum span:last-child {
    font-size: 20rem;
}

#add_order .divCart > div:last-child {
    width: 102rem;
    height: 36rem;
    opacity: 1;
    border-radius: 18rem;
    font-size: 15rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    line-height: 36rem;
    position: absolute;
    right: 5rem;
    top: 4rem;
}

#add_order .divCart .btnSubmit {
    color: white;
    background: #d8d8d8;
}
#add_order .divCart .btnSubmitActive {
    color: #333333;
    background: #ffc200;
}

#add_order .divCart .divGoodsNum {
    width: 18rem;
    height: 18rem;
    opacity: 1;
    background: #e94e3c;
    border-radius: 50%;
    text-align: center;
    font-size: 12rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    color: #ffffff;
    line-height: 18rem;
    position: absolute;
    left: 57rem;
    top: -5rem;
}

#add_order .divCart .moreGoods {
    width: 25rem;
    height: 25rem;
    line-height: 25rem;
}

.ss-dish{
    margin-left: 10rem;
    margin-bottom: 2rem;
    /*margin: 6rem 0;*/
}

</style>