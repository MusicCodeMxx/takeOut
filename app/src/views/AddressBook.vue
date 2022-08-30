<template>
    <div id="address" class="app">

        <div class="divHead">
            <div class="divTitle">
                <i class="el-icon-arrow-left" @click.prevent="goBack"></i>地址管理
            </div>
        </div>

        <div class="divContent">

            <div class="divItem"
                 v-for="(item,index) in addressList"
                 :key="index"
                 >


                <div @click.stop.prevent="itemClick(item)" >
                    <!-- 地址-->
                    <div class="divAddress">
                        <span :class="{ spanCompany:item.label === '公司',spanHome:item.label === '家',spanSchool:item.label === '学校'}">{{item.label}}</span>
                        {{item.detail}}
                    </div>

                    <!-- 用户手机 -->
                    <div class="divUserPhone">
                        <span>{{item.consigneeName}}</span>
                        <span>{{item.sex === '0' ? '女士' : '先生'}}</span>
                        <span>{{item.phoneNumber}}</span>
                    </div>

                </div>

                <!--修改-->
                <img src="../assets/images/edit.png" @click.stop.prevent="toAddressEditPage(item)"/>

                <!--线-->
                <div class="divSplit"></div>

                <div class="divDefault" >
                    <img src="../assets/images/checked_true.png" v-if="item.isDefault === 1">
                    <img src="../assets/images/checked_false.png" @click.stop.prevent="setDefaultAddress(item)" v-else>设为默认地址
                </div>

            </div>
        </div>

        <div class="divBottom" @click.stop.prevent="toAddressCreatePage">+ 添加收货地址</div>
    </div>
</template>

<script>

import {Loading} from 'element-ui';
import {Notify, Toast} from 'vant';
import userAddressBookApi from "../api/userAddressBookApi";
import localStorageCache from "../utils/localStorage";
import system from "../common/system";
// 存储上一级路由地址
let previousPageUrl = undefined;
const PAGE_NAME = '地址簿页';
export default {
    name: "AddressBook",
    data() {
        return {
            // 地址列表
            addressList: [],
        }
    },
    computed: {},
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
        // console.log(to,from);
        console.log(PAGE_NAME+"路由器进入");
        previousPageUrl = from.fullPath;
        const routerToParams = to.params;
        next(vm => {
            // 检查是否刷新地址,或
            if (routerToParams){
                if (routerToParams.flushCahceId){
                    vm.removeAddressBookById(routerToParams.flushCahceId)
                }
            }
        });
    },
    methods: {

        // 返回上一层
        goBack() {
            this.$router.back(-1);
        },

        toAddressEditPage(item) {
            window.requestAnimationFrame(() => {
                this.$router.push({
                    name: 'AddressEdit',  params: { addressDetail:item },
                })
            })
        },

        // 移除指定地址信息
        removeAddressBookById(e){
          const index = this.addressList.findIndex(al => al.id == e);
          this.addressList.splice(index,1);

        },

        // 去添加地址
        toAddressCreatePage() {
            window.requestAnimationFrame(() => {
                this.$router.push({path: '/user/address/edit'})
            })
        },

        // 获取地址簿
        async initData() {
            if (!localStorageCache.get(system.TOKEN_KEY)){
                this.$ssMessageBox("未登录，无法操作！点击确定去登录")
                    .then(()=>{
                        // 去登录
                        this.$router.replace("/login");
                    })
                    .reject(()=>{
                        // 去首页
                        this.$router.replace("/");
                    })
                return
            }
            let loadingInstance = Loading.service({ fullscreen: true });

            try {
                const { status,code,message,data } = await userAddressBookApi.list();
                loadingInstance.close();
                if (status) {
                    if (data){
                        if (data.length > 0){
                            this.addressList = data;
                            return
                        }
                    }
                    Notify({type: 'warning', message: '暂无地址信息'});
                    // 获取用户信息
                    const user = localStorageCache.getByAES(system.STARSHINT_TOP_USER_INFO);
                    let vo = {
                        id: '',
                        phoneNumber: undefined
                    }
                    if (user){
                        const { phoneNumber } = user;
                        if (phoneNumber){
                            vo.phoneNumber = phoneNumber;
                        }
                    }
                    this.$router.push({
                        path: '/user/address/edit',
                        query: { params: JSON.stringify(vo)}
                    });
                } else {
                    this.$ssToast(message);
                }
            }catch (e){
                loadingInstance.close();
            }
        },

        // 设置为默认地址
        async setDefaultAddress(item) {
            const { id } = item;
            if (id) {
                const { status,code, message, data } = await userAddressBookApi.reviseDefault(id)
                if (status) {
                    // 遍历找出这属性
                    const index = this.addressList.findIndex(ad => ad.id === id );
                    this.addressList.map(ad => ad['isDefault'] = 0);
                    this.addressList[index]['isDefault'] = 1;
                    const url = previousPageUrl;
                    if(url){
                        if (url.includes('details')) {
                            this.$router.replace({
                                name: 'OrderDetails', params: {addressDetail:item },
                            })
                        }
                    }
                } else {
                    this.$message.error(message)
                }
            }
        },

        // 点击地址
        itemClick(item) {
            const url = previousPageUrl;
            // console.log(item);
            if(url){
                if (url.includes('details')) {
                    this.$router.replace({
                        name: 'OrderDetails', //注意使用 params 时一定不能使用 path
                        // query: item,
                        params: { addressDetail:item },
                    })
                    return;
                    // this.$router.replace({ path: '/user/address/book', query:item });
                }
            }
            this.toAddressEditPage(item);
        }

    },
}
</script>

<style scoped>
#address .divHead {
    width: 100%;
    /*height: 88rem;*/
    height: 50rem;
    opacity: 1;
    background: #333333;
    position: relative;
}

#address .divHead .divTitle {
    font-size: 18rem;
    font-weight: 500;
    text-align: center;
    color: #ffffff;
    line-height: 25rem;
    letter-spacing: 0;
    position: absolute;
    bottom: 13rem;
    width: 100%;
}

#address .divHead .divTitle i {
    position: absolute;
    left: 16rem;
    top: 50%;
    transform: translate(0, -50%);
}

#address .divContent {
    height: calc(100vh - 157rem);
    overflow: auto;
}

#address .divContent .divItem {
    height: 128rem;
    opacity: 1;
    background: #ffffff;
    border-radius: 6rem;
    margin-top: 10rem;
    margin-left: 10rem;
    margin-right: 9rem;
    padding-left: 12rem;
    position: relative;
}

#address .divContent .divItem > img {
    width: 16rem;
    height: 16rem;
    position: absolute;
    /*top: 40rem;*/
    top: 36rem;
    right: 24rem;
    padding: 10rem;
}

#address .divContent .divItem .divDefault img {
    width: 16rem;
    height: 16rem;
    opacity: 1;
}

#address .divContent .divItem .divAddress {
    font-size: 14rem;
    font-weight: 400;
    text-align: left;
    color: #333333;
    line-height: 20rem;
    letter-spacing: 0;
    padding-top: 21rem;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 280rem;
}

#address .divContent .divItem .divAddress span {
    width: 34rem;
    height: 20rem;
    opacity: 1;
    font-size: 12rem;
    display: inline-block;
    text-align: center;
    margin-right: 4rem;
    margin-bottom: 10rem;
    background-color: #ffc200;
}

#address .divContent .divItem .divUserPhone span {
    height: 20rem;
    opacity: 1;
    font-size: 14rem;
    font-weight: 400;
    text-align: left;
    /*color: #999999;*/
    color: #333333;
    line-height: 20rem;
    letter-spacing: 0;
    margin-right: 10rem;
}

#address .divContent .divItem .divUserPhone span:first-child {
    margin-right: 2rem;
}

#address .divContent .divItem .divAddress .spanCompany {
    background-color: #e1f1fe;
}

#address .divContent .divItem .divAddress .spanHome {
    /*background: #fef8e7;*/
    background: #ffc200;
}

#address .divContent .divItem .divAddress .spanSchool {
    background: #e7fef8;
}

#address .divContent .divItem .divSplit {
    height: 1px;
    opacity: 1;
    background: #efefef;
    border: 0;
    margin-top: 16rem;
    margin-bottom: 10rem;
    margin-right: 10rem;
}

#address .divContent .divItem .divDefault {
    height: 18rem;
    opacity: 1;
    font-size: 13rem;
    font-weight: 400;
    text-align: left;
    color: #333333;
    line-height: 18rem;
    letter-spacing: 0;
}

#address .divContent .divItem .divDefault img {
    height: 18rem;
    width: 18rem;
    margin-right: 5rem;
    vertical-align: bottom;
}

#address .divBottom {
    height: 36rem;
    opacity: 1;
    background: #ffc200;
    border-radius: 18rem;
    opacity: 1;
    font-size: 15rem;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 36rem;
    letter-spacing: 0;
    position: absolute;
    bottom: 23rem;
    left: 50%;
    transform: translate(-50%, 0);
    width: 334rem;
}

</style>