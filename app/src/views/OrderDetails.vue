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
                                    <span class="spanMoney">￥</span>{{item.price}}</div>
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
            <div class="note" style="height: auto;">
                <div class="title">价格明细</div>
                <div class="ss-price-detail__item">
                    <div>商品总价&nbsp;<span style="font-size: 12rem">(共{{goodsNum}}件)</span></div>
                    <div>￥{{originalPrice}}</div>
                </div>
                <div class="ss-price-detail__item">
                    <div>运费</div>
                    <div>￥{{ freightPrice }}</div>
                </div>
                <div class="ss-price-detail__item">
                    <div>满减优惠</div>
                    <div>￥{{ coupon.price }}</div>
                </div>
                <div  class="ss-price-detail__item">
                    <div>合计</div>
                    <div>￥{{ goodsPrice }}</div>
                </div>
            </div>
        </div>


        <!--<van-notice-bar-->
        <!--    left-icon="info-o"-->
        <!--    style="position: fixed; bottom: 60rem; right: 0; left: 0;"-->
        <!--    :scrollable="false" text="已为你选择默认地址" />-->
        <!--底部购物车栏-->
        <shopping-cart-button-bar
            :couponPrice="coupon.price"
            :billPrice="goodsPrice"
            :productTotalNumber="goodsNum"
            :loading="submitButtonLoadingFlag"
            :enableCouponPrice="true"
            @onCouponDetail="showCouponPopup = true"
            @onSubmit="goToPaySuccess"
        ></shopping-cart-button-bar>

        <van-popup v-model="showCouponPopup" position="bottom">
            <van-coupon-list
                :show-exchange-bar="false"
                :coupons="coupons"
                :chosen-coupon="chosenCoupon"
                :disabled-coupons="disabledCoupons"
                @change="onChange"
            />
        </van-popup>



    </div>
</template>

<script>
import {Loading} from 'element-ui';
import {Notify, Toast} from 'vant';
import dateFomatUtils from "../utils/dateFomatUtils";
import randomNumberUtils from "../utils/randomNumberUtils";
import orderApi from "../api/orderApi";
import orderPaymentApi from "../api/orderPaymentApi";
import shoppingCartApi from "../api/shoppingCartApi";
import userAddressBookApi from "../api/userAddressBookApi";
import ShoppingCartButtonBar from "@/components/ShoppingCartButtonBar";
import paymentApi from "@/api/paymentApi";

const PAGE_NAME = '订单详细页';
export default {
    name: "OrderDetails",
    components: {  ShoppingCartButtonBar },
    data(){
        return {
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
            priceDetails:[],// 当前符合使用优惠券的信息
            showCouponPopup: false,
            // 运费
            freightPrice: 0,
            // 优惠券列表
            couponVos: undefined,
            // 当前选中的优惠券
            coupon: {
                // 使用门槛
                threshold: 0,
                // 优惠面额
                price: 0,
            },
            // 当前选中优惠券的索引, -1 全部都不选
            chosenCoupon: -1,
            // 可用优惠券列表
            coupons: [],
            // 不可用用的优惠券列表
            disabledCoupons: [],
            // 根据价格自动选择符合门槛并且面额最大的优惠券
            autoSelectCoupon : true,
            // 商品原价
            originalPrice: 0,
            // 提交按钮加载状态开启
            submitButtonLoadingFlag: false,
            // 提交之后生成的主键
            submitOfPayOrderId: undefined,
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
            this.originalPrice = 0;
            this.freightPrice = 8;
            if (this.cartData){
                if(this.cartData.length > 0) {
                    this.cartData.forEach(item => {
                        price += ((item.price) * item.amount)
                    })
                }
                this.originalPrice = price;
                // 检查运费
                this.freightPrice = price >= 66 ? 0 : 8;
                // 初始化
                this.coupons = [];
                this.disabledCoupons = [];
                if (this.couponVos){
                    // 循环遍历检查是否符合门槛
                    this.couponVos.forEach(cp=>{
                        // 如果当前循环的优惠券门槛大于总价就剔除
                        if (cp.threshold > price){
                            cp['reason'] = '当前金额不满使用门槛';
                            this.disabledCoupons.push(cp);
                        }else{
                            // 添加到可以用的优惠券中
                            this.coupons.push(cp);
                        }
                    })
                    // 检查符合消费门槛中最大面额的优惠券
                    if(this.coupons){
                        this.coupons.forEach((cp,index)=>{
                            // 关闭自动选择券之后, 用户减少商品导致不满足优惠券门槛再次开启自动选券
                            if (this.coupon) if (this.coupon.threshold > price) if (!this.autoSelectCoupon) {
                                this.autoSelectCoupon = true;
                                this.coupon.price = 0;
                            }
                            // 自动选券
                            if (this.autoSelectCoupon){
                                console.log("价格=>",price, this.coupon,"优惠券门槛=>", cp.threshold);
                                // 比较面额并交换, 校验是否满足门槛
                                if (cp.price > this.coupon.price && price >= cp.threshold){
                                    this.coupon = cp;
                                    this.chosenCoupon = index;
                                }
                            }

                        })
                    }
                    if (this.coupon) if (this.coupon.threshold > price) {
                        this.resetAll();
                        this.goodsPrice;
                    }
                    if (this.freightPrice) price += this.freightPrice;
                    if (this.coupon) price = ( price - this.coupon.price );
                }
            }
            return price;
        }
    },
    watch: {
        // 优惠券列表
        couponVos(val){
            if (val){
                val.forEach(cp=>{
                    cp['available'] = cp.id;
                    cp['condition'] = '满'+ cp.threshold + '元可用';
                    cp['reason'] = '';
                    cp['startAt'] = cp.startTime;
                    cp['endAt'] = cp.endTime;
                    cp['value'] = cp.price;
                    cp['name'] = cp.couponName;
                    cp['valueDesc'] = cp.price;
                    cp['unitDesc'] = '元';
                })
            }
        },
        // 产品列表
        cartData:{
            // 深度监听
            deep: true,
            handler(val1,val2){
                // 商品列表
                if( val1 && val1.length > 0) val1.forEach(item => {
                    if (item.value) {
                        item['dishFlavorList'] = item.value.split(',');
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
        // this.initData()
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
        const fullPath  =  from.fullPath;
        let tempAddressItem = to.params;
        next(vm => {
            if (tempAddressItem){
                if (tempAddressItem.addressDetail){
                    if(tempAddressItem.addressDetail.phoneNumber) vm._data.address = tempAddressItem.addressDetail;
                }
            }
            // 如果来自首页就重新获取一下商品信息
            if(fullPath){
                if (fullPath.includes('/home')) {
                    console.log("来自首页强制刷新数据")
                    vm.initData();
                    // vm.generateUUID();// uuid 一起刷新
                    // vm.loadDefaultAddressDetails();
                }else{
                    if (!vm._data.cartData || vm._data.cartData.length === 0) vm.goBack();
                }
            }
        });
    },
    beforeRouteLeave(to, from, next) {
        console.log(PAGE_NAME,"组件导航守卫路由器离开");
        next();
    },
    methods:{

        /**重置数据*/
        resetAll(){
            this.submitOfPayOrderId = undefined;
            this.originalPrice = 0;
            // 提交按钮显示加载
            this.submitButtonLoadingFlag = false;
            // 自动选择优惠券
            this.autoSelectCoupon = true;
            // 默认选中的优惠券下标
            this.chosenCoupon = -1;
            // 当前选中的优惠券
            this.coupon =  { threshold: 0, price: 0,};
        },

        goBack(){
            this.$router.go(-1);
        },

        // 加载订单聚合信息, 购物车数据, 地址信息, 优惠券集合
        async initData(){
            this.resetAll();
            let loadingInstance = Loading.service({ fullscreen: true });
            try {
                const { status,data } = await orderApi.buildBillData();
                if(status) {
                    if (data) {
                        const {userAddressBookVo, shoppingCartVos, couponVos} = data;
                        // 优惠券
                        if (couponVos) this.couponVos = couponVos;
                        // 购物车
                        if (shoppingCartVos) this.cartData = shoppingCartVos;
                        // 地址簿
                        if(userAddressBookVo){
                            this.address = userAddressBookVo;
                            this.getFinishTime();
                            Toast({
                                message: '已您自动选择默认收货地址',
                                position: 'bottom',
                            });
                        }else{
                            Notify({type: 'warning', message: name + '您当前无默认地址'});
                            this.toAddressPage();
                        }
                        loadingInstance.close();
                        return
                    }
                }
            }catch (e){}
            loadingInstance.close();
            Notify({type: 'warning', message: '当前无产品结算'});
            this.$router.replace("/");
        },

        // 点击优惠券
        onChange(index) {
            // 手动选券就失效调用自动券
            this.autoSelectCoupon = false;
            // 关闭优惠券列表
            this.showCouponPopup = false;
            // 当前选中的优惠券下标
            this.chosenCoupon = index;
            // 当前选择的优惠券对象
            if (index !== -1) this.coupon = this.coupons[index];
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

        // 获取地址
        async loadDefaultAddressDetails(){
            try {
                const { status,message,data } = await  userAddressBookApi.getDefault();
                if (status){
                    if (data){
                        this.address = data;
                    }else {
                        this.toAddressPage();
                    }
                }else{
                    Notify("获取地址失败:" + message);
                }
            }catch (e) {
                Notify(e.message);
            }
        },

        // 获取购物车数据
        async getCartData(){
            const {status, message, data} = await shoppingCartApi.list();
            if(status){
                if (data){
                    if (data.length > 0){
                        this.cartData = data;
                        return
                    }
                }
                this.$ssToast('当前无产品结算');
                window.requestAnimationFrame(()=>{
                    this.$router.replace("/");
                })
            }else{
                this.$ssToast(message);
            }
        },

        // 提交订单
        async goToPaySuccess(){
            if (!this.address){
                Notify("用户地址信息不存在");
                return
             }
            this.submitButtonLoadingFlag = true;
            // 检查是否提交成功了
            if (this.submitOfPayOrderId){
                this.payOrder(this.submitOfPayOrderId);
                return;
            }
            // let loadingInstance = Loading.service({ fullscreen: true });
            // 解构地址簿
            const { consigneeName, phoneNumber, detail, sex, label } = this.address;
            // 解构优惠券
            let vo = {
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
                /** 地址绑定的主键 */
                addressBookId: this.address.id,
                //原总价
                originalPrice: this.originalPrice,
                /** 前端实际付价格 */
                billPrice: this.goodsPrice,
                // 运费
                freightPrice:this.freightPrice,
                // 产品数量
                productTotalNumber:this.goodsNum,
            }

            if (this.coupon){
                try {
                    const {id, price} = this.coupon;
                    vo['couponId'] = id;
                    vo['couponPrice'] = price;
                }catch (e){}
            }
            try {
                // 发起订单生成
                const {status, message, data} = await orderPaymentApi.submit(vo);
                if(status){
                    // 提交成功, 拉起支付
                    Toast({
                        message: '提交成功，正在拉起支付',
                        position: 'bottom',
                    });
                    this.submitOfPayOrderId = data;
                    await this.payOrder(data);
                }else {
                    Toast({
                        message: '提交失败{}' + message,
                        position: 'bottom',
                    });
                }
            }catch (e) {
            }
            this.submitButtonLoadingFlag = false;
            // loadingInstance.close();
            this.$message({ message: message, type: 'warning'});
        },

        async payOrder(orderId){
            if (!orderId) {
                Notify("订单号不存在");
                return;
            }
            const id = orderId + '';
            // 这里判断导致数据异常
            if (orderId === '-1') {
                // 提示失败
                Notify("拉起支付失败");
                // 跳转到订单页面
                this.$router.push("/order");
            }
            try{
                // 发起支付请求
                const {status,code, message, data} = await paymentApi.payOrderById(id)
                if (status){
                   if (data){
                       this.submitButtonLoadingFlag = false;
                       // 后端返回的支付跳转地址
                       // 添加之前先删除一下，如果单页面，页面不刷新，添加进去的内容会一直保留在页面中，二次调用 form 表单会出错
                       const divForm = document.getElementsByTagName('div')
                       if (divForm.length) document.body.removeChild(divForm[0])
                       const div = document.createElement('div'); // 创建新的 div
                       div.innerHTML = data // data 就是接口返回的 form 表单字符串
                       document.body.appendChild(div);
                       // document.forms[0].setAttribute('target', '_blank'); // 新开窗口跳转
                       document.forms[0].setAttribute('target', '_self'); // 新开窗口跳转
                       document.forms[0].submit();// 提交
                   }
                }else {
                    Notify(message);
                    // 该订单不存在, 回到主页
                    if (code === 70001) await this.$router.push("/home");
                }
            }catch (e){
                console.log(e);
                Notify("支付异常");
            }
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

.ss-dish{
    margin-left: 10rem;
    margin-bottom: 2rem;
    /*margin: 6rem 0;*/
}

.ss-price-detail__item{
    font-size: 16rem;
    padding: 6rem 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.ss-price-detail__item div:nth-child(2){
    color: #e94e3c;
}

</style>