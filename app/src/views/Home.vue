<template>
    <div class="home" id="main">
        <div class="divBody">

            <!--品类列表-->
            <div class="divType">
                <ul>
                    <li
                        v-for="(obj,index) in categoryList"
                        :key="obj.id"
                        @click="categoryClick(index,obj.id,obj.type)"
                        :class="{active:activeType === index}"
                        v-text="obj.name">
                    </li>
                </ul>
            </div>

            <!-- 商品列表 -->
            <div class="divMenu">
                <div>
                    <div class="divItem"
                         v-for="(item,index) in dishList"
                         :key="item.id + item + index"
                         @click.prevent="dishDetails(item)">
                        <el-image :src="imgPathConvert(item.imageDefUrl)">
                            <div slot="error" class="image-slot">
                                <img src="../assets/images/noImg.png" alt=""/>
                            </div>
                        </el-image>
                        <div>
                            <div class="divName">{{ item.name }}</div>
                            <div class="divDesc">{{ item.description }}</div>
                            <div class="divDesc">{{ '月销' + (item.saleNum ? item.saleNum : 0) }}</div>
                            <div class="divBottom">
                                <span>￥</span>
                                <span>{{ item.price}}</span>
                                {{item.number}}
                            </div>
                            <div class="divNum">
                                <div class="divTypes"  @click.prevent.stop="chooseFlavorClick(item)">选择规格</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <!--商品属性选择-->
        <van-dialog
            v-model="dialogFlavor.show"
            :show-confirm-button="false"
            :closeOnClickOverlay="true"
            class="dialogFlavor"
            ref="flavorDialog">
            <div class="dialogTitle">{{ dialogFlavor.name }}</div>
            <div class="divContent">
                <div v-for="(flavor,index) in dialogFlavor.flavors" :key="flavor.id + index">
                    <div class="divFlavorTitle">{{ flavor.name }}</div>
                    <span v-for="(item,index) in JSON.parse(flavor.value)"
                          :key="item  + index"
                          @click="flavorClick(flavor,item)"
                          :class="{spanActive:flavor.dishFlavor === item}"
                    >{{ item }}</span>
                </div>
            </div>
            <div class="divBottom">
                <div>
                    <span class="spanMoney">￥</span>
                    {{ dialogFlavor.price }}
                </div>
                <div @click.stop.prevent="dialogFlavorAddCart">加入购物车</div>
            </div>
        </van-dialog>

        <!--购物车展示-->
        <van-popup v-model="cartDialogShow"
                   position="bottom"
                   :style="{ minHeight: '20vh', maxHeight: '80vh'}"
                   class="dialogCart">

            <div class="divCartTitle">
                <div class="title">购物车</div>
                <div class="clear" @click.stop.prevent="clearCart">
                    <span>清空全部</span>
                    <i class="el-icon-delete"></i>
                </div>
            </div>
            <div  v-if="cartData.length > 0" class="divCartContent">

                <div  v-for="(item,index) in cartData"
                     :key="item.id + index"
                     class="divCartItem">

                    <!--图片-->
                    <el-image :src="imgPathConvert(item.imageDefUrl)">
                        <div slot="error" class="image-slot">
                            <img src="../assets/images/noImg.png"/>
                        </div>
                    </el-image>

                    <!--产品信息-->
                    <div class="divDesc">
                        <div class="name">{{ item.name }}</div>
                        <div class="ss-dish">
                            <el-tag style="margin-right: 6rem"
                                    type="success"
                                    size="mini"
                                    v-for="(tag,index) in item.dishFlavorList"
                                    :key="index" >{{tag}}</el-tag>
                        </div>
                        <div class="price">
                            <span class="spanMoney">￥</span>
                            <span>{{ item.price}}</span>
                        </div>
                    </div>

                    <!-- 加减 -->
                    <div class="divNum">
                        <img src="../assets/images/subtract.png" @click.stop.prevent="cartNumberSubtract(item)"/>
                        <div class="divDishNum" style="font-size: 20rem;" v-text="item.amount"></div>
                        <img src="../assets/images/add.png" @click.stop.prevent="cartNumAdd(item)"/>
                    </div>
                </div>

            </div>

            <div v-else>
                <el-empty description="购物车空空如也"></el-empty>
            </div>
        </van-popup>

        <!-- 点击菜品图片显示 -->
        <van-dialog
            :closeOnClickOverlay="true"
            v-model="detailsDialog.show"
            :show-confirm-button="false"
            class="detailsDialog"
            ref="detailsDialog"
            v-if="detailsDialog.show">
            <div class="divContainer">
                <el-image :src="imgPathConvert(detailsDialog.item.imageDefUrl)">
                    <div slot="error" class="image-slot">
                        <img src="../assets/images/noImg.png"/>
                    </div>
                </el-image>
                <div class="title">{{ detailsDialog.item.name }}</div>
                <div class="content">{{ detailsDialog.item.description }}</div>
            </div>
            <div class="divNum">
                <div class="left">
                    <span>￥</span>
                    <span>{{ detailsDialog.item.price}}</span>
                </div>
                <div class="right">
                    <div class="divTypes" @click="chooseFlavorClick(detailsDialog.item)">选择规格</div>
                </div>
            </div>

            <div class="detailsDialogClose" @click="detailsDialog.show = false">
                <img src="../assets/images/close.png"/>
            </div>

        </van-dialog>

        <!--底部购物车栏-->
        <shopping-cart-button-bar
            :bottomDistance="46.32"
            :billPrice="goodsPrice"
            :productTotalNumber="goodsNum"
            @onCart="openCart"
            @onSubmit="toAddOrderPage"
        ></shopping-cart-button-bar>

    </div>
</template>

<script>

import ShoppingCartButtonBar from "@/components/ShoppingCartButtonBar";
import {Notify} from 'vant';
import {Loading} from 'element-ui';
import randomNumberUtils from "../utils/randomNumberUtils";
import system from "../common/system";
import localStorageCache from "../utils/localStorage";
import dishApi from "../api/dishApi";
import setmealApi from "../api/setmealApi";
import shoppingCartApi from "../api/shoppingCartApi";
import homeApi from "@/api/homeApi";


export default {
    name: 'Home',
    components: {  ShoppingCartButtonBar },
    data() {
        return {
            //左边菜品类别index
            activeType: 0,
            categoryList: [],
            categoryId: undefined,
            dishList: [],
            cartData: [],
            dialogFlavor: {
                name: '',
                flavors: [],
                dishId: undefined,
                price: undefined,
                show: false,
                image: ''
            },
            cartDialogShow: false,
            detailsDialog: {
                show: false,
                item: {image: ''}
            },
            // 选中的产品属性
            setMealDialog: {
                show: false,
                item: {}
            },
        }
    },
    computed: {

        goodsNum() {
            let num = 0
            this.cartData.forEach(item => {
                num += item.amount
            })
            if (num < 99) {
                return num
            } else {
                return '99+'
            }
        },

        goodsPrice() {
            let price = 0
            if (this.cartData){
                this.cartData.forEach(item => {
                    price += ((item.price) * item.amount)
                })
            }
            return price;
        }

    },
    created() {
        this.initData()
    },
    watch: {
        cartData:{
            deep: true,
            handler(val1){
                // 商品列表
                if( val1 && val1.length > 0) val1.forEach(item => {
                    if (item.value) {
                        item['dishFlavorList'] = item.value.split(',');
                    }
                })
            }
        },
    },
    mounted() {},
    beforeRouteEnter(to, from, next) {
        console.log("首页组件导航守卫路由器进入组件之前");
        const fullPath  =  from.fullPath;
        next(vm => {
            // 来自登录页重新刷新一下, 解决登录之后没有显示购物车信息
            if(fullPath) if (fullPath.includes('/login')) vm.initData();
            if(fullPath) if (fullPath.includes('/order/detail')) vm.loadCartData();
        });
    },
    methods: {

        //初始化数据
        initData() {
            let loadingInstance = Loading.service({ fullscreen: true });
            this.localHomePageCache();
            if (!localStorageCache.get(system.TOKEN_KEY)) Notify({type: 'warning', message: "您当前未登录，部分功能需要登录才能使用"});
            loadingInstance.close();
        },

        // 加载首页聚合信息
        async localHomePageCache(){
            try {
                const {status, message, data} = await homeApi.list();
                if (status){
                    if (data){
                        const {categoryVos, productVos, shoppingCartVos, couponVos} = data;
                        this.couponVos = couponVos;
                        this.categoryId = categoryVos[0].id;
                        this.categoryList = categoryVos;
                        this.dishList = productVos;
                        if (shoppingCartVos) this.cartData = shoppingCartVos;
                    }
                }
                console.log("data=>",data);
            }catch (e){
                Notify("服务姬不知道去哪，联系不上了~")
            }
        },


        // 分类列表点击
        categoryClick(index, id, type) {
            if (this.categoryId) if (this.categoryId === id) return;
            this.activeType = index
            this.categoryId = id
            this.getDishList();
        },

        // 获取菜品数据
        async getDishList() {
           if (!this.categoryId) return
           let loadingInstance = Loading.service({ fullscreen: true });
           try {
               const {status, data, message} = await dishApi.listByCategoryId({id: this.categoryId});
               if (status) {
                   this.dishList = data;
                   loadingInstance.close();
               } else {
                   loadingInstance.close();
                   this.$message({ message: message,type: 'warning'});
               }
           }catch (e){
               loadingInstance.close();
           }
        },

        // 获取购物车数据
        async getCartData() {
            await this.loadCartData();
            this.cartDialogShow = true
        },

        async loadCartData(){
            let loadingInstance = Loading.service({ fullscreen: true });
            try {
                const {status, data, message} = await shoppingCartApi.list();
                if (status) {
                    if (data){
                        this.cartData = data;
                    }else {
                        this.cartData = [];
                    }
                    loadingInstance.close();
                }else{
                    loadingInstance.close();
                    Notify({type: 'warning', message: message});
                }
            }catch (e) {
                loadingInstance.close();
            }
        },

        // 菜单中往购物车中添加商品
        async addCart(item) {
            let loadingInstance = Loading.service({ fullscreen: true });
            const {id, dishFlavor,categoryId,name,image,price } = item;
            const vo = {
                id: id, // 产品主键
                value: dishFlavor
            }
            try {
                const {status, code, data, message } = await shoppingCartApi.add(vo)
                if ( status ) {
                    let cartTemp = this.cartData;
                    if (cartTemp){
                        // 是否找到标识,true没有找到, false找到
                        let isNotFind = true;
                        // 找出对应的对象,并赋值后端返回的数据
                        if (cartTemp.length > 0) {
                            cartTemp.forEach(ct => {
                                if (ct.id === id) {
                                    if (ct.value === dishFlavor) {
                                        ct.amount = data;
                                        isNotFind = false;
                                    }
                                }
                            })
                        }
                        let isAdd = true;
                        // 循环查询是否有相同
                        this.cartData.forEach(co => {
                            if (id === co.id){
                                if(dishFlavor === co.value){
                                    co.amount = data;
                                    isAdd = false;
                                }
                            }

                        });
                        if(isAdd){
                            // 添加成功之后, 显示到购物车
                            const cartObj = {
                                id: id,
                                value: dishFlavor,
                                categoryId: categoryId,
                                name: name,
                                imageDefUrl: image,
                                price: price,
                                productStatus: undefined,
                                amount: data,
                            }
                            this.cartData.push(cartObj);
                        }
                    }
                    loadingInstance.close();
                    Notify({type: 'primary', message: name + '已添加到购物车'});
                    return;
                }
                loadingInstance.close();
                Notify({type: 'warning', message: message});
            } catch (e) {
                console.log(e);
                loadingInstance.close();
                Notify("添加失败");
            }
        },

        // 展开购物车
        async openCart() {
            if (!localStorageCache.get(system.TOKEN_KEY)){
                try { await this.$ssMessageBox("您还未登录,点击确定可以跳转登录页");  this.$router.push("/login"); } catch (e) { return; }
                return;
            }
            // 发起异步获取购物车信息
            this.getCartData();
        },

        // 购物车中增加商品数量
        async cartNumAdd(item) {
            let params = {
                // amount: item.amount,//金额
                value: item.value,//口味  如果没有传undefined
                id: item.id,//菜品id
                // name: item.name, // 名称
                // image: item.image // 图片
            }
            const { status, code, data, message} = await shoppingCartApi.add(params)
            if (status) {
                let index = this.cartData.findIndex(cart => ( cart.id === item.id && cart.value == item.value));
                this.cartData[index]['amount'] = this.cartData[index]['amount'] + 1;
                Notify({type: 'primary', message: item.name + '已增加数量'});
                return;
            }
            this.$ssToast(message);
        },

        // 购物车中减少商品数量
        async cartNumberSubtract(item) {
            let vo = {
                value: item.value,//口味  如果没有传 undefined
                id: item.id,//菜品id
            }
            let index = this.cartData.findIndex(cart => ( cart.id === (item.id || item.setmealId) && cart.attributesValue == item.attributesValue));
            let { amount } = this.cartData[index];
            if (1 === amount) {
                try {
                    await this.$dialog.confirm({
                        title: '温馨提示',
                        message: '您确定要将" ' + item.name + ' "从购物车中删除吗',
                    })
                } catch (e) { return; }
                // try { await this.$ssMessageBox('您确定要将"' + item.name + '"从购物车中删除吗' ); } catch (e) { return; }
            }
            this.crateSubtract(vo,index,item.name);
        },
        async crateSubtract(vo,index,name){
            const { status,code,message,data } = await shoppingCartApi.subtract(vo)
            if (status) {
                if( 0 >= data ){
                    this.cartData.splice(index,1);
                    Notify({type: 'primary', message: name + '已从购物车中移除'});
                }else{
                    this.cartData[index]['amount'] = data;
                    Notify({type: 'primary', message: name + '已减少数量'});
                }
            }else{
                this.$ssToast(message);
            }
        },

        //修改商品列表中的数据 number
        changeDishList(item) {
            for (let ele of this.dishList) {
                if (ele.id === (item.setmealId || item.dishId)) {
                    ele.number = item.number
                }
            }
        },

        // 清空购物车
        async clearCart() {
            const { status,message} = await shoppingCartApi.clear();
            if (status) {
                for (let ele of this.dishList) {
                    ele.number = undefined
                }
                this.cartData = []
                this.cartDialogShow = false
                Notify({type: 'primary', message: '已移除购物车所有商品'});

            } else {
                this.$ssToast(message);
            }
        },

        // 点击选择规格
        chooseFlavorClick(item) {
            this.dialogFlavor = {
                name: '',
                flavors: [],
                dishId: undefined,
                price: undefined,
                show: false
            }
            this.dialogFlavor = {
                name: item.name,
                flavors: item.attributesVos,
                dishId: item.id,
                price: item.price,
                show: true,
                image: item.imageDefUrl
            }
        },

        flavorClick(flavor, item) {
            flavor.dishFlavor = item
            //强制刷新dialog的 dom
            this.dialogFlavor.show = false
            this.dialogFlavor.show = true
        },

        // 选择规格加入购物车
        dialogFlavorAddCart() {
            const { price, dishId, image,name } = this.dialogFlavor
            let flag = false;
            let dishFlavor = []
            let message = "请选择";
            // 检查参数是否有选择
            this.dialogFlavor.flavors.forEach(item => {
                if (item.dishFlavor) {
                    dishFlavor.push(item.dishFlavor)
                    item.dishFlavor = undefined;
                } else {
                    flag = true;
                    message += "，" + item.name;
                }
            })
            // 进行提示
            if (flag) {
                Notify(message);
            }else {
                // 添加购物车
                this.addCart({
                    price: price,
                    dishFlavor: dishFlavor.join(","),
                    id: dishId,
                    flavors: [],
                    image: image,
                    name: name
                })
                this.dialogFlavor.show = false
            }
        },

        //网络图片路径转换
        imgPathConvert(path) {
            return "http://10.10.10.55:9000/reggie/static/item/" + path;
        },

        // 跳转到去结算界面
        toAddOrderPage() {
            if (this.cartData.length > 0) {
                if (!localStorageCache.get(system.TOKEN_KEY)){
                    this.$ssToast('您还未登录,无法操作');
                    return;
                }
                let submitId = randomNumberUtils.uuid(32,16).toString();
                this.$router.push({
                    name: 'OrderDetails', //注意使用 params 时一定不能使用 path
                    params: { pageCacheClose:false ,submitId:submitId },
                })
            }
        },

        toUserPage() {
            window.requestAnimationFrame(() => {
                this.$router.push("/me");
            })
        },

        // 点击菜品的图片
        async dishDetails(item) {
            // 先清除对象数据，如果不行的话 dialog 使用 v-if
            this.detailsDialog.item = {}
            this.setMealDialog.item = {}
            if (Array.isArray(item.attributesVos)) {
                this.detailsDialog.item = item
                this.detailsDialog.show = true
            } else {
                //显示套餐的数据
                const res = await setmealApi.setmealDishDetails(item.id)
                if (res.code === 1) {
                    this.setMealDialog.item = {...item, list: res.data}
                    this.setMealDialog.show = true
                } else {
                    this.$notify.error({ title: '消息提醒',  message: res.msg });
                    // this.$notify({type: 'warning', message: res.msg});
                }
            }
        }

    }
}
</script>


<style scoped>
/**
首屏样式
*/
#main {
    height: 100%;
    overflow: hidden;
}

#main .divTitle .divStatic .divDesc .divName {
    width: 90rem;
    height: 25rem;
    opacity: 1;
    font-size: 18rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: left;
    color: #20232a;
    line-height: 25rem;
}

#main .divTitle .divStatic .divDesc .divSend {
    opacity: 1;
    font-size: 11rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #333333;
    margin-bottom: 10rem;
}

#main .divTitle .divStatic .divDesc .divSend img {
    width: 14rem;
    height: 14rem;
    opacity: 1;
    vertical-align: sub;
}

#main .divTitle .divStatic .divDesc .divSend span {
    margin-right: 12rem;
}

#main .divTitle .divStatic .divDesc .divSend span:last-child {
    margin-right: 0;
}

#main .divTitle > .divDesc {
    opacity: 1;
    font-size: 12rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #9b9b9b;
    line-height: 17rem;
    margin-right: 18rem;
    padding-top: 9rem;
    border-top: 1rem dashed #ebebeb;
}

#main .divBody {
    display: flex;
    height: 100%;
}

#main .divBody .divType {
    background: #f6f6f6;
}

#main .divBody .divType ul {
    margin-top: 61rem;
    overflow-y: auto !important;
    height: calc(100% - 61rem);
    padding-bottom: 200rem;
    box-sizing: border-box;
    width: 84rem;
    opacity: 1;
}

#main .divBody .divType ul li {
    padding: 16rem;
    opacity: 1;
    font-size: 13rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #666666;
    line-height: 18rem;
    letter-spacing: 0rem;
    word-wrap: break-word;
    word-break: normal;
}

#main .divBody .divType ul li.active {
    color: #333333;
    font-weight: 500;
    background-color: #ffffff;
    font-family: PingFangSC, PingFangSC-Regular;
}

#main .divBody .divMenu {
    background-color: #ffffff;
    box-sizing: border-box;
    width: 100%;
    /*height: 100%;*/
    height: 100vh;
}

/* 列表的顶部空块 */
#main .divBody .divMenu > div {
    /*margin-top: 61rem;*/
    /*margin-top: 28rem;*/
    overflow: hidden auto;
    /*overflow-y: auto !important;*/
    height: calc(100% - 61rem);
    /*padding-bottom: 200rem;*/
    padding-bottom: 100rem;
    box-sizing: border-box;
}

#main .divBody .divMenu .divItem {
    /*margin: 10rem 15rem 20rem 14rem;*/
    margin: 10rem 0 20rem 10rem;
    display: flex;
}

#main .divBody .divMenu .divItem .el-image {
    width: 86rem;
    height: 86rem;
    margin-right: 14rem;
}

#main .divBody .divMenu .divItem .el-image img {
    width: 86rem;
    height: 86rem;
    border-radius: 5rem;
}

#main .divBody .divMenu .divItem > div {
    position: relative;
}

#main .divBody .divMenu .divItem .divName {
    height: 22rem;
    opacity: 1;
    font-size: 16rem;
    font-family: PingFangSC, PingFangSC-Semibold;
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

#main .divBody .divMenu .divItem .divDesc {
    height: 16rem;
    opacity: 1;
    font-size: 12rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #666666;
    line-height: 16rem;
    letter-spacing: 0rem;
    margin-bottom: 4rem;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 144rem;
}

#main .divBody .divMenu .divItem .divBottom {
    font-size: 15rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #e94e3c;
    line-height: 20rem;
    letter-spacing: 0rem;
}

#main .divBody .divMenu .divItem .divBottom span:first-child {
    font-size: 12rem;
}

#main .divBody .divMenu .divItem .divBottom span:last-child {
    font-size: 15rem;
}

#main .divBody .divMenu .divItem .divNum {
    display: flex;
    position: absolute;
    right: 12rem;
    bottom: 0;
}

#main .divBody .divMenu .divItem .divNum .divDishNum {
    font-size: 15rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 36rem;
    letter-spacing: 0;
    width: auto;
}
#main .dialogCart .divCartContent .divNum .divDishNum {
    font-weight: 600;
    color: #333333;
    padding: 0 6rem;
    letter-spacing: 0;
}

#main .divBody .divMenu .divItem .divNum .divTypes {
    /*width: 64rem;*/
    /*height: 24rem;*/
    padding: 2px 12px;
    opacity: 1;
    background: #ffc200;
    /*border-radius: 12rem;*/
    border-radius: 4rem;
    color: #505050;
    font-size: 12rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    line-height: 24rem;
    letter-spacing: 0;
}

#main .divBody .divMenu .divItem .divNum img {
    width: 36rem;
    height: 36rem;
}

#main .divCart {
    width: 345rem;
    height: 44rem;
    opacity: 1;
    background: #000000;
    border-radius: 25rem;
    box-shadow: 0rem 3rem 5rem 0rem rgba(0, 0, 0, 0.25);
    margin: 0 auto;
    /*bottom: 24rem;*/
    bottom: 60rem;
    position: fixed;
    left: 50%;
    transform: translate(-50%, 0);
    /*z-index: 3000;*/
}

#main .divCart .imgCartActive {
    background-image: url("../assets/images/cart_active.png");
}

#main .divCart .imgCart {
    background-image: url("../assets/images/cart.png");
}

#main .divCart > div:first-child {
    width: 60rem;
    height: 60rem;
    position: absolute;
    left: 11rem;
    bottom: 0;
    background-size: 60rem 60rem;
}

#main .divCart .divNum {
    font-size: 12rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #ffffff;
    letter-spacing: 0rem;
    position: absolute;
    left: 90rem;
    top: 15rem;
}

#main .divCart .divNum span:last-child {
    font-size: 20rem;
}

/* 去结算按钮 */
#main .divCart > div:last-child {
    width: 102rem;
    height: 36rem;
    opacity: 1;
    border-radius: 18rem;
    font-size: 15rem;
    font-weight: 500;
    text-align: center;
    line-height: 36rem;
    position: absolute;
    right: 5rem;
    top: 4rem;
    font-family: PingFangSC, PingFangSC-Regular;
}

#main .divCart .btnSubmit {
    color: white;
    background: #d8d8d8;
}

#main .divCart .btnSubmitActive {
    color: #333333;
    background: #ffc200;
}

#main .divCart .divGoodsNum {
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
    left: 50rem;
    top: -5rem;
}

#main .divCart .moreGoods {
    width: 25rem;
    height: 25rem;
    line-height: 25rem;
}

#main .divLayer {
    position: absolute;
    height: 68rem;
    width: 100%;
    bottom: 0;
    display: flex;
}

#main .divLayer .divLayerLeft {
    background-color: #f6f6f6;
    opacity: 0.5;
    width: 84rem;
    height: 100%;
}

#main .divLayer .divLayerRight {
    background-color: white;
    opacity: 0.5;
    width: calc(100% - 84rem);
    height: 100%;
}

#main .dialogFlavor {
    opacity: 1;
    background: #ffffff;
    border-radius: 10rem;
}

#main .dialogFlavor .dialogTitle {
    margin-top: 26rem;
    margin-bottom: 14rem;
    font-size: 18rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    color: #333333;
    letter-spacing: 0;
    text-align: center;
}

#main .dialogFlavor .divContent {
    margin-left: 15rem;
    margin-right: 15rem;
}

#main .dialogFlavor .divContent .divFlavorTitle {
    height: 20rem;
    opacity: 1;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #666666;
    line-height: 20rem;
    letter-spacing: 0;
    margin-left: 5rem;
    margin-bottom: 10rem;
    margin-top: 10rem;
}

#main .dialogFlavor .divContent span {
    box-sizing: border-box;
    display: inline-block;
    height: 30rem;
    opacity: 1;
    background: #ffffff;
    border: 1rem solid #ffc200;
    border-radius: 7rem;
    /*line-height: 30rem;*/
    /*padding-left: 13rem;*/
    /*padding-right: 13rem;*/
    padding: 5rem 13rem;
    margin: 0 5rem 10rem 5rem;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: center;
    color: #333333;
}

#main .dialogFlavor .divContent .spanActive {
    background: #ffc200;
    font-weight: 500;
}

#main .dialogFlavor .divBottom {
    margin-top: 20rem;
    margin-bottom: 19rem;
    margin-left: 20rem;
    display: flex;
    position: relative;
}

#main .dialogFlavor .divBottom div:first-child {
    height: 30rem;
    opacity: 1;
    font-size: 20rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #e94e3c;
    line-height: 30rem;
    letter-spacing: 0;
}

#main .dialogFlavor .divBottom div span {
    font-size: 14rem;
}

#main .dialogFlavor .divBottom div:last-child {
    width: 100rem;
    height: 30rem;
    opacity: 1;
    background: #ffc200;
    border-radius: 15rem;
    text-align: center;
    line-height: 30rem;
    position: absolute;
    right: 20rem;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
}

#main .dialogFlavor .divFlavorClose {
    position: absolute;
    bottom: -70rem;
    left: 50%;
    transform: translate(-50%, 0);
}

#main .dialogFlavor .divFlavorClose img {
    width: 44rem;
    height: 44rem;
}

#main .dialogCart {
    background: linear-gradient(180deg, #ffffff 0%, #ffffff 81%);
    /*border-radius: 12px 12px 0px 0px;*/
}

#main .dialogCart .divCartTitle {
    height: 59rem;
    display: flex;
    line-height: 60rem;
    position: relative;
    margin-left: 15rem;
    margin-right: 10rem;
    border-bottom: 1px solid #efefef;
}

#main .dialogCart .divCartTitle .title {
    font-size: 20rem;
    font-family: PingFangSC, PingFangSC-Semibold;
    font-weight: 600;
    text-align: left;
    color: #333333;
}

#main .dialogCart .divCartTitle i {
    margin-right: 3rem;
    font-size: 15rem;
    vertical-align: middle;
}

#main .dialogCart .divCartTitle .clear {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translate(0, -50%);
    color: #999999;
    font-size: 14rem;
    font-weight: 400;
    text-align: left;
    display: flex;
    align-items: center;
}
#main .dialogCart .divCartTitle .clear span {
    padding-right: 6rem;
}

#main .dialogCart .divCartItem {
    /*height: 108rem;*/
    height: 100rem;
    /*margin-left: 15rem;*/
    /*margin-right: 10rem;*/
    /*display: flex;*/
    /*align-items: center;*/
    position: relative;
    display: grid;
    grid-template-columns: 3fr 6fr 3fr;
    border-bottom: 1px solid #efefef;
}

#main .dialogCart .divCartItem .divDesc {
    padding-left: 4rem;
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
}

#main .dialogCart .divCartContent {
    /*height: calc(100% - 130rem);*/
    height: calc(80vh - 59rem);
    overflow-y: auto;
}

#main .dialogCart .divCartContent .el-image {
    /*width: 64rem;*/
    /*height: 64rem;*/
    /*opacity: 1;*/
    /*margin-right: 10rem;*/
    padding: 6rem;
    border-radius: 22rem;
    box-sizing: border-box;
    overflow: hidden;
}

#main .dialogCart .divCartContent .el-image img {
    width: 64rem;
    height: 64rem;
}

#main .dialogCart .divCartContent .divDesc .name {
    height: 22rem;
    opacity: 1;
    font-size: 16rem;
    font-family: PingFangSC, PingFangSC-Semibold;
    font-weight: 600;
    text-align: left;
    color: #333333;
    line-height: 22rem;
    letter-spacing: 0;
    /*margin-bottom: 17rem;*/
}

#main .dialogCart .divCartContent .divDesc .ss-dish{
    margin: 6rem 0;
}

#main .dialogCart .divCartContent .divDesc .price {
    font-size: 18rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #e94e3c;
}

#main .dialogCart .divCartContent .divDesc .price .spanMoney {
    font-size: 12rem;
}

#main .dialogCart .divCartContent .divCartItem .divNum {
    position: absolute;
    right: 0;
    /*bottom: 10rem;*/
    bottom: 0;
    display: flex;
    /*line-height: 36rem;*/
    /*height: 36rem;*/
    align-items: center;
    /*font-weight: 600;*/
}

#main .dialogCart .divCartContent .divCartItem .divNum img {
    width: 36rem;
    height: 36rem;
}

#main .dialogCart .divCartContent .divCartItem .divSplit {
    width: calc(100% - 64rem);
    position: absolute;
    bottom: 0;
    right: 0;
    height: 1px;
    opacity: 1;
    background-color: #efefef;
}

#main .dialogCart .divCartContent .divCartItem:last-child .divSplit {
    height: 0;
}

#main .detailsDialog {
    display: flex;
    flex-direction: column;
    text-align: center;
}

#main .detailsDialog .divContainer {
    padding: 20rem 20rem 0 20rem;
    overflow: auto;
    /*max-height: 50vh;*/
    max-height: 60vh;
    overflow-y: auto;
}

#main .detailsDialog .el-image {
    width: 100%;
    height: 100%;
}

#main .detailsDialog .el-image img {
    width: 100%;
    height: 100%;
}

#main .detailsDialog .title {
    height: 28rem;
    opacity: 1;
    font-size: 20rem;
    font-weight: 600;
    color: #333333;
    line-height: 28rem;
    letter-spacing: 0;
    /*margin-top: 18rem;*/
    /*margin-bottom: 11rem;*/
    margin: 10rem 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
}

#main .detailsDialog .content {
    opacity: 1;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: justify;
    color: #333333;
    line-height: 24rem;
}

#main .detailsDialog .divNum {
    display: flex;
    justify-content: space-between;
    margin-top: 23rem;
    margin-bottom: 20rem;
    padding-left: 20rem;
    padding-right: 20rem;
}

#main .detailsDialog .divNum .left {
    font-size: 20rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #e94e3c;
    line-height: 36rem;
    letter-spacing: 0rem;
}

#main .detailsDialog .divNum .left span:first-child {
    font-size: 12rem;
}

#main .detailsDialog .divNum .right {
    display: flex;
}

#main .detailsDialog .divNum .divDishNum {
    font-size: 15rem;
    /*font-family: PingFangSC, PingFangSC-Regular;*/
    font-weight: 500;
    text-align: center;
    color: #333333;
    /*line-height: 36rem;*/
    letter-spacing: 0;
    width: auto;
}

#main .detailsDialog .divNum .divTypes {
    width: 64rem;
    height: 24rem;
    opacity: 1;
    background: #ffc200;
    border-radius: 12rem;
    font-size: 12rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 24rem;
    letter-spacing: 0;
}

#main .detailsDialog .divNum .divSubtract, .divAdd {
    height: 36rem;
}

#main .detailsDialog .divNum img {
    width: 36rem;
    height: 36rem;
}

#main .detailsDialog .detailsDialogClose {
    position: absolute;
    bottom: -70rem;
    left: 50%;
    transform: translate(-50%, 0);
}

#main .detailsDialog .detailsDialogClose img {
    width: 44rem;
    height: 44rem;
}

#main .setMealDetailsDialog {
    display: flex;
    flex-direction: column;
    text-align: center;
}

#main .setMealDetailsDialog .divContainer {
    padding: 20rem 20rem 0 20rem;
    /*overflow: auto;*/
    max-height: 50vh;
    overflow-y: auto;
}

#main .setMealDetailsDialog .el-image {
    width: 100%;
    height: 100%;
}

#main .setMealDetailsDialog .el-image img {
    width: 100%;
    height: 100%;
}

#main .setMealDetailsDialog .divSubTitle {
    text-align: left;
    margin-top: 16rem;
    margin-bottom: 6rem;
    height: 25rem;
    opacity: 1;
    font-size: 18rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: left;
    color: #333333;
    line-height: 25rem;
    letter-spacing: 0px;
    position: relative;
}

#main .setMealDetailsDialog .divContainer .item .divSubTitle .divPrice {
    position: absolute;
    right: 0;
    top: 0;
    font-size: 18rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #e94e3c;
    line-height: 25rem;
    letter-spacing: 0rem;
}

#main
.setMealDetailsDialog
.divContainer
.item
.divSubTitle
.divPrice
span:first-child {
    font-size: 12rem;
}

#main .setMealDetailsDialog .title {
    height: 28rem;
    opacity: 1;
    font-size: 20rem;
    font-family: PingFangSC, PingFangSC-Semibold;
    font-weight: 600;
    color: #333333;
    line-height: 28rem;
    letter-spacing: 0;
    margin-top: 18rem;
    margin-bottom: 11rem;
}

#main .setMealDetailsDialog .content {
    opacity: 1;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: justify;
    color: #333333;
    line-height: 24rem;
}

#main .setMealDetailsDialog .divNum {
    display: flex;
    justify-content: space-between;
    margin-top: 23rem;
    padding-bottom: 15rem;
    padding-left: 20rem;
    padding-right: 20rem;
}

#main .setMealDetailsDialog .divNum .left {
    font-size: 20rem;
    font-family: DIN, DIN-Medium;
    font-weight: 500;
    text-align: left;
    color: #e94e3c;
    line-height: 36rem;
    letter-spacing: 0rem;
}

#main .setMealDetailsDialog .divNum .left span:first-child {
    font-size: 12rem;
}

#main .setMealDetailsDialog .divNum .right {
    display: flex;
}

#main .setMealDetailsDialog .divNum .divDishNum {
    font-size: 15rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 36rem;
    letter-spacing: 0;
    width: auto;
}

#main .setMealDetailsDialog .divNum .divTypes {
    width: 64rem;
    height: 24rem;
    opacity: 1;
    background: #ffc200;
    border-radius: 12rem;
    font-size: 12rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 24rem;
    letter-spacing: 0;
}

#main .setMealDetailsDialog .divNum .divSubtract,
.divAdd {
    height: 36rem;
}

#main .setMealDetailsDialog .divNum img {
    width: 36rem;
    height: 36rem;
}

#main .setMealDetailsDialog .divNum .right .addCart {
    width: 100rem;
    height: 30rem;
    opacity: 1;
    background: #ffc200;
    border-radius: 15rem;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 30rem;
}

#main .setMealDetailsDialog .detailsDialogClose {
    position: absolute;
    bottom: -70rem;
    left: 50%;
    transform: translate(-50%, 0);
}

#main .setMealDetailsDialog .detailsDialogClose img {
    width: 44rem;
    height: 44rem;
}


</style>
