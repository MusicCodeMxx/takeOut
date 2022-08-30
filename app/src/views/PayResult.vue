<template>
    <div id="pay_success" class="app">
        <div class="divHead">
            <div class="divTitle">
                <i class="" @click="goBack"></i>
                结果页面
                <img src="../assets/images/home.png" @click="toMainPage"/>
            </div>
        </div>
        <div class="divContent">
            <img src="../assets/images/success.png"/>
            <div class="divSuccess">下单成功</div>
            <div class="divDesc">预计{{finishTime}}到达</div>
            <div class="divDesc1">后厨正在加紧制作中，请耐心等待~</div>
            <div class="btnView" @click="toOrderPage">查看订单</div>
        </div>
    </div>
</template>

<script>
export default {
    name: "PayResult",
    data(){
        return {
            finishTime:''
        }
    },
    computed:{},
    created(){
        this.getFinishTime()
    },
    mounted(){},
    methods:{
        goBack(){
            window.requestAnimationFrame(()=>{
                this.$router.push("/");
            })
        },
        toOrderPage(){
            window.requestAnimationFrame(()=>{
                this.$router.replace("/order");
            })
        },
        toMainPage(){
            window.requestAnimationFrame(()=>{
                this.$router.push("/");
            })
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

    }
}
</script>

<style scoped>
#pay_success .divHead {
    width: 100%;
    /*height: 88rem;*/
    height: 50rem;
    opacity: 1;
    background: #333333;
    position: relative;
}

#pay_success .divHead .divTitle {
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
    display: flex;
    justify-content: space-between;
    align-items: center;
}

#pay_success .divHead .divTitle i {
    margin-left: 16rem;
}

#pay_success .divHead .divTitle img {
    width: 18rem;
    height: 18rem;
    margin-right: 19rem;
}

#pay_success .divContent {
    height: calc(100vh - 88rem);
    width: 100%;
    background: #ffffff;
    display: flex;
    flex-direction: column;
    text-align: center;
    align-items: center;
}

#pay_success .divContent img {
    margin-top: 148rem;
    margin-bottom: 19rem;
    width: 90rem;
    height: 86rem;
}

#pay_success .divContent .divSuccess {
    height: 33rem;
    opacity: 1;
    font-size: 24rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 33rem;
    margin-top: 19rem;
    margin-bottom: 10rem;
}

#pay_success .divContent .divDesc,
.divDesc1 {
    height: 22rem;
    opacity: 1;
    font-size: 16rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: center;
    color: #666666;
    line-height: 22rem;
}

#pay_success .divContent .divDesc1 {
    margin-top: 7rem;
    margin-bottom: 20rem;
}

#pay_success .divContent .btnView {
    width: 124rem;
    height: 36rem;
    opacity: 1;
    background: #ffc200;
    border-radius: 18px;
    opacity: 1;
    font-size: 15rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 21rem;
    letter-spacing: 0;
    line-height: 36rem;
}

</style>