<template>
    <div class="t-login" v-loading="loading">

        <!-- 标题 -->
        <div class="t-b">{{ title }}</div>

        <!-- 验证码登录 -->
        <div>
            <div class="cl">
                <div class="t-a">
                    <div>
                        <i class="el-icon-mobile-phone"></i>
                    </div>
                    <input v-model="phone" placeholder="请输入手机号"/>
                </div>
                <div class="t-a">
                    <div>
                        <i class="el-icon-chat-dot-round"></i>
                    </div>
                    <div class="t-inp-code">
                        <input v-model="checknum" placeholder="请输入验证码"/>
                        <div class="t-c"  >
                            <span @click.stop.prevent="getcode" v-text="checknummessage"></span>
                        </div>
                    </div>
                </div>

                <div class="t-d" >
                    <span>未注册的手机号验证后将</span>
                    <span>自动注册</span>
                </div>

                <button :class="{'submit-active' : disable}"
                            :disabled="disable"
                        @click.stop.prevent="submit" >
                    立即登录
                </button>

            </div>

        </div>

        <div class="t-f">
            <span>登录即同意</span>
            <span>《<a href="/#/user/agreement">用户协议</a>》</span>
        </div>


    </div>
</template>

<script>

import login from "../api/loginApi"
import {decryptByAES, encryptByAES} from "../utils/des/secruity";
import localStorage from "../utils/localStorage";
import systemProperties from "@/common/system";
import {Notify} from 'vant';

export default {

    name: "Login",

    data() {
        return {
            title: '欢迎登录',
            // 手机号
            phone: '19112345678',
            // 验证码
            checknum: '',
            // 时间
            codetime: 0,
            // 登录按钮控制(true禁用,false不禁用)
            disable: true,
            // 验证码起始时间
            codetimestart: 120,
            codetimeOjbect: null,
            loading: false,
        }
    },

    watch: {
        /** 监听用户输入,符合规则的开启按钮*/
        phone(val) {
            this.onbtncheck();
        },
        checknum(val) {
            this.onbtncheck();
        }
    },

    computed: {

        // 计算验证码有效时间
        checknummessage() {
            let msg = '获取验证码';
            if (!this.codetime) {
                if (this.isgot) {
                    msg = '重新验证码';
                }
            } else {
                this.isgot = true;
                msg = this.codetime + 's';
            }
            return msg;
        }
    },

    created() {

    },

    methods: {

        // 获取验证码
        async getcode() {
            // 当前验证码时间没有就不执行
            if (this.codetime > 0) {
                this.$message({ message: '还在有效期哦~', type: 'warning' });
                return;
            }
            //手机号正则,验证合法性
            if (!this.isphone(this.phone)) {
                this.$message({ message: '请输入手机号哦~', type: 'warning' });
                return;
            }
            this.loading = true;
            // 发起异步获取验证码
            try {
                // 加密处理
                const phone = encryptByAES(this.phone, systemProperties.outServiceAesKey);
                const {status, message} = await login.send({phone});
                if (status) {
                    this.$message({ message: message, type: 'success' });
                    this.loading = false;
                    this.codetime = this.codetimestart;
                    this.codetimeOjbect = setInterval(() => {
                        this.codetime--;
                        if (0 >= this.codetime) {
                            clearInterval(this.codetimeOjbect);
                            this.codetimeOjbect = undefined;
                            this.codetime = 0;
                        }
                    }, 1000)
                    return undefined;
                }
                this.loading = false;
                this.$message({ message: message, type: 'warning' });
            } catch (e) {
                this.loading = false;
                console.log("获取验证码发生异常=>", e);
            }
        },

        // 登录
        async submit() {
            if (!this.phone) {
                this.$message({ message: '请输入手机号哦~', type: 'warning' });
                return;
            }
            if (!this.checknum) {
                this.$message({ message: '请输入验证码哦~', type: 'warning' });
                return;
            }
            try {
                const vo = {
                    phoneNumber: encryptByAES(this.phone, systemProperties.outServiceAesKey),
                    verificationCode: this.checknum
                }
                const { status, data, message} = await login.submit(vo);
                if (status) {
                    let user = JSON.parse(decryptByAES(data));
                    // 解密并存储用户信息
                    localStorage.setByAES("STARSHINT_TOP_USER_INFO",user);
                    // 清除信息
                    this.phone = undefined;
                    this.checknum = undefined;
                    // 清除定时器
                    clearInterval(this.codetimeOjbect);
                    this.codetimeOjbect = undefined;
                    Notify({type: 'success', message: user['nickname'] + '，欢迎回来~'});
                    // 跳回来登录的页面
                    this.$router.replace({ path: "/" });
                    return;
                }
                this.$message({ message: message, type: 'warning' });
            } catch (e) {
                console.log("验证码登录异常=>", e);
            }
        },

        //判断是否有值才能点击
        onbtncheck() {
            if ((this.account && this.password) || (this.phone && this.checknum)) {
                this.disable = false;
                return;
            }
            this.disable = true;
        },

        // 手机号正则,验证合法性
        isphone(value) {
            let mPattern = /^1[3456789]\d{9}$/;
            return mPattern.test(value);
        }
    }

}
</script>

<style lang="scss" scoped>

.t-login {
    text-align: center;
    // 标题
    .t-b {
        text-align: left;
        font-size: 46px;
        color: #000;
        padding: 100px 0 38px 26px;
        //transform: translateX(-50px);
    }

    // 表单
    .cl {

        // 输入框盒子
        .t-a {

            border: 1px solid #ebebeb;
            border-radius: 6px;
            margin: 0 26px ;
            margin-bottom: 20px;
            box-sizing:border-box;
            position: relative;
            display: grid;
            grid-template-columns: 48rem 1fr;
            // 输入框图标
            div:nth-child(1){
                display: flex;
                align-items: center;
                justify-content: center;
                i {
                    color: #ffc200;
                    font-size: 26rem;
                }
            }

            // 输入框
            input {
                // padding: 0 20px 0 120px;
                height: 45px;
                line-height: 45px;
                // margin-bottom: 25px;
                // background: #f4f4f4;
                font-size: 16px;
                border-radius: 8px;
                box-shadow: none;
                border: none;
            }

            .t-inp-code{
                //text-align: left;
                display: grid;
                grid-template-columns: 1fr 86rem;
                .t-c {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    span{
                        color: #fff;
                        font-size: 14px;
                        //height: 36px;
                        //line-height: 36px;
                        background: #ffc200;
                        padding: 8px 6px;
                        border-radius: 6px;
                        z-index: 99;
                    }

                }

            }
        }

        // 自动注册提示
        .t-d {
            color: #999;
            font-size: 15px;
            text-align: center;
            margin: 28px 0;
            span:nth-child(2) {
                color: #000;
                letter-spacing: 0;
                font-weight: 600;
                margin-left: 10px;
            }
        }

        // 登录按钮
        button {
            font-size: 18px;
            background: #ffc200;
            color: #fff;
            height: 45px;
            border: none;
            border-radius: 6px;
            padding: 6px 86px;
        }

        // 登录按钮可以按的状态
        .submit-active {
            background: rgba(255, 201, 71, 0.63);
        }
    }

    // 用户协议
    .t-f {
        color: #666;
        letter-spacing: 1px;
        font-size: 16px;
        z-index: 99999;
        margin-top: 80px;
        span:nth-child(2){
            color: #000;
            letter-spacing: 0;
            font-weight: 600;
            margin-left: 10px;
        }
    }

}

</style>
