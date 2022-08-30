<template>
    <div id="address_edit" class="app">
        <div class="divHead">
            <div class="divTitle">
                <i class="el-icon-arrow-left" @click="goBack"></i>{{title}}
            </div>
        </div>
        <div class="divContent">
            <div class="divItem">
                <span>联系人：</span>
                <el-input placeholder=" 请填写收货人的姓名"
                          v-model="form.consigneeName"
                          maxlength="10" class="inputUser" ></el-input>
                <span class="spanChecked" @click="form.sex = '1'">
                    <i :class="{iActive:form.sex === '1'}"></i>
                    先生
                   </span>
                <span class="spanChecked" @click.="form.sex = '0'">
                    <i :class="{iActive:form.sex === '0'}"></i>
                    女士
                </span>
            </div>
            <div class="divItem">
                <span>手机号：</span>
                <el-input @input="numberCheckingEvent" placeholder=" 请填写收货人手机号码" v-model="form.phoneNumber"  maxlength="20" style="width: calc(100% - 80rem);"></el-input>
            </div>
            <div class="divItem">
                <span>收货地址：</span>
                <el-input placeholder=" 请输入收货地址" v-model="form.detail"  maxlength="140" style="width: calc(100% - 80rem);" ></el-input>
            </div>
            <div class="divItem ">
                <span>标签：</span>
                <span v-for="(item,index) in labelList"
                      :key="index"
                      class="spanItem"
                      @click.prevent="form.label = item; form.labelIndex = index;"
                      :class="{spanActiveSchool:form.labelIndex === index}">{{item}}</span>
            </div>
            <div class="divItem ">
                <div class="divDefault" @click.stop.prevent="setDefaultAddress">
                    设为默认地址
                    <img src="../assets/images/checked_true.png" v-if="form.isDefault === 1">
                    <img src="../assets/images/checked_false.png" v-else>
                </div>
            </div>
            <div class="divSave" @click.stop.prevent="saveAddress">保存地址</div>
            <div class="divDelete" @click="deleteAddress" v-show="editType || form.id">删除地址</div>
        </div>
    </div>
</template>

<script>

import {Notify} from "vant";
import userAddressBookApi from "../api/userAddressBookApi";
import localStorageCache from "../utils/localStorage";
import system from "../common/system";
import {encryptByAES} from "../utils/des/secruity";
import systemProperties from "@/common/system";


const PAGE_NAME = "地址详情编辑页"
export default {
    name: "AddressEdit",
    data(){
        return {
            title:'新增收货地址',
            editType: 0, // 页面类型,0新建地址,1修改地址
            form:{
                id: undefined,
                consigneeName:'',//联系人
                phoneNumber:undefined,//手机号
                sex:'1',//0表示女 1 表示男
                detail:'',//收货地址
                label:'默认',//标签
                labelIndex: 0,// 标签的下标
                isDefault: 0 //是否默认,0否,1是,
            },
            labelList:[ '默认','家','学校','公司','其他'],
        }
    },
    computed:{},
    created(){
        this.initData()
    },
    mounted(){
    },
    watch:{
        'form':{
            immediate: true,
            handler(val){
                this.labelList.forEach((la,index) =>{
                    if (val.label === la){
                        val.labelIndex = index;
                    }
                });
            }
        }
    },
    deactivated() {
        console.info(PAGE_NAME,"缓存暂停")
        this.clearDate();
    },
    beforeRouteEnter(to, from, next) {
        let tempAddressItem = to.params;
        next(vm => {
            if (tempAddressItem){
                if (tempAddressItem.addressDetail){
                    if(tempAddressItem.addressDetail.phoneNumber){
                        vm._data.form = tempAddressItem.addressDetail;
                    }
                }
            }
        });
    },
    methods:{

        // 检查手机号
        numberCheckingEvent(e){
            const reg =  /^[1-9]\d*$/;
            if (e){
                if(!reg.test(e)){
                    Notify({type:"warning",message:"请输入数字" })
                    this.form.phone = undefined;
                }
            }
        },

        setDefaultAddress(){
            if (this.form.isDefault === 1){
                this.form.isDefault = 0;
            }else if (this.form.isDefault === 0){
                this.form.isDefault = 1;
            }
        },

        // 返回上一层
        goBack() {
            this.clearDate();
            this.$router.back(-1);
        },

        async initData(){
            // 检查是否登录
            if (!localStorageCache.get(system.TOKEN_KEY)){
                this.$ssMessageBox("未登录，无法操作！点击确定去登录")
                    .then(()=>{
                        this.clearDate();
                        // 去登录
                        this.$router.replace("/login");
                    })
                    .reject(()=>{
                        this.clearDate();
                        // 去首页
                        this.$router.replace("/");
                    })
                return
            }
            const params = this.$route.params;
            if (params){
                if (params.addressDetail){
                    this.form = params.addressDetail;
                    const { id ,phoneNumber} = params.addressDetail
                    if(id){
                        this.title = '编辑收货地址'
                        this.editType = 1;
                        if (!phoneNumber){
                            const {status,code,message,data } = await userAddressBookApi.find(id)
                            status ? this.form = data : Notify(message);
                        }
                    }
                }
            }
        },
        async saveAddress(){
            const { id,consigneeName,phoneNumber,detail,sex,label,isDefault,labelIndex } = this.form
            if(!consigneeName){
                Notify({type:"warning",message:"请输入联系人" })
                return
            }
            if(!phoneNumber){
                Notify({type:"warning",message:"请输入手机号" })
                this.$ssToast("请输入手机号");
                return
            }
            if(!detail){
                Notify({type:"warning",message:"请输入收货地址" })
                return
            }
            const reg = /^1[3456789]\d{9}$/;
            if(!reg.test(phoneNumber)){
                Notify({type:"warning",message:"您输入的手机号码不合法" })
                return
            }

            let vo = {
                phoneNumber : phoneNumber, //手机号
                consigneeName : consigneeName, //收货人姓名
                detail : detail, //详细地址
                sex: sex, //性别,0女,1男
                label: label, //标签
                labelIndex: labelIndex, // 标签的下标
                isDefault: isDefault // 是否默认,0否,1是
            }
            let res = {}
            if(this.editType === 1 || id){
                // 更新
                vo.id = id;
                res = await userAddressBookApi.update(vo)
            }else{
                // 加密处理
                vo.phoneNumber = encryptByAES(vo.phone, systemProperties.outServiceAesKey)
                vo.consigneeName = encryptByAES(vo.consigneeName, systemProperties.outServiceAesKey)
                vo.detail = encryptByAES(vo.detail, systemProperties.outServiceAesKey)
                // 添加
                res = await userAddressBookApi.add(vo)
            }
            if(res.status){
                // 返回上一层
                this.goBack();
            }else{
                // 提示
                Notify({type:"warning",message: res.message})

            }
        },
        deleteAddress(){
            this.$dialog.confirm({
                title: '确认删除',
                message: '确认要删除当前地址吗？',
            })
            .then( async () => {
                const { id } = this.form;
                if (id){
                    const { status,message } = await userAddressBookApi.delete(id);
                    if(status){
                        window.requestAnimationFrame(()=>{
                            this.clearDate();
                            this.$router.replace(
                                { name:'AddressBook',params:{ flushCahceId:id }}
                            );
                        })
                    }else{
                        this.$ssToast(message)
                    }
                }else{
                    this.goBack();
                }
            })
            .catch(() => {});
        },

        // 清除信息
        clearDate(){
            this.title = '新增收货地址';
            this.editType = 0;
            this.form = {
                id: undefined,
                consigneeName:'',//联系人
                phoneNumber:undefined,//手机号
                sex:'1',//0表示女 1 表示男
                detail:'',//收货地址
                label:'默认',//标签
                labelIndex: 0,// 标签的下标
                isDefault: 0
            };
        }
    }
}
</script>

<style scoped>
#address_edit {
    height: 100%;
}
#address_edit .divHead {
    width: 100%;
    /*height: 88rem;*/
    height: 50rem;
    opacity: 1;
    background: #333333;
    position: relative;
}

#address_edit .divHead .divTitle {
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

#address_edit .divHead .divTitle i {
    position: absolute;
    left: 16rem;
    top: 50%;
    transform: translate(0, -50%);
}

#address_edit .divContent {
    height: 100%;
    opacity: 1;
    background: #ffffff;
    padding-left: 9rem;
    padding-right: 9rem;
}

#address_edit .divContent .divItem {
    height: 55rem;
    line-height: 55rem;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: left;
    color: #333333;
    line-height: 20rem;
    letter-spacing: 0rem;
    border-bottom: 1px solid #efefef;
    display: flex;
    align-items: center;
}

#address_edit .divContent .divItem .el-input {
    width: auto;
}

#address_edit .divContent .divItem input {
    border: 0;
    padding: 0;
}

#address_edit .divContent .divItem .inputUser {
    width: 150rem;
}

#address_edit .divContent .divItem span {
    display: block;
}

#address_edit .divContent .divItem span:first-child {
    margin-right: 12rem;
    white-space: nowrap;
    width: 69rem;
}

#address_edit .divContent .divItem .spanChecked {
    width: 50rem;
}

#address_edit .divContent .divItem span i {
    width: 16rem;
    height: 16rem;
    background: url(../assets/images/checked_false.png);
    display: inline-block;
    background-size: cover;
    vertical-align: sub;
}

#address_edit .divContent .divItem span .iActive {
    background: url(../assets/images/checked_true.png);
    background-size: cover;
}

#address_edit .divContent .divItem .spanItem {
    /*width: 34rem;*/
    /*height: 20rem;*/
    /*opacity: 1;*/
    border: 1px solid #e5e4e4;
    border-radius: 3rem;
    text-align: center;
    margin-right: 10rem;
    border-radius: 2px;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    color: #333333;
    /*padding: 6rem 10rem;*/
    padding: 6rem 8rem;
}

#address_edit .divContent .divItem .spanActiveCompany {
    background: #e1f1fe;
}

#address_edit .divContent .divItem .spanActiveHome {
    background: #fef8e7;
}

#address_edit .divContent .divItem .spanActiveSchool {
    background: #ffc200;
}

.divDefault {
    /*font-size: 13rem;*/
    font-weight: 400;
    text-align: left;
    color: #333333;
    line-height: 18rem;
    letter-spacing: 0;
}

.divDefault img {
    height: 20rem;
    width: 20rem;
    margin-right: 6rem;
    vertical-align: bottom;
}

#address_edit .divContent .divItem .el-input {
    font-size: 13px;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #333333;
}

#address_edit .divContent .divSave {
    height: 36rem;
    opacity: 1;
    background: #ffc200;
    border-radius: 18rem;
    font-size: 15rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 36rem;
    margin-top: 20rem;
}

#address_edit .divContent .divDelete {
    height: 36rem;
    opacity: 1;
    background: #f6f6f6;
    border-radius: 18rem;
    font-size: 15rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 36rem;
    margin-top: 20rem;
}

</style>