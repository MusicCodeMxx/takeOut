<template>
    <div class="ss-message_box__body" :visible.sync="dialogVisible"  v-if="dialogVisible" >
        <div class="el-message-box__wrapper el-message-box__wrapper_bg"   style="z-index: 2022;" @click.stop.prevent="onclose" >
            <div class="el-message-box" style="width: 90vw" @click.stop.prevent="">
                <div class="el-message-box__header">
                    <div class="el-message-box__title">
                        <span v-text="message"></span>
                    </div>
                    <button @click.stop.prevent="onclose"  type="button" aria-label="Close" class="el-message-box__headerbtn">
                        <i class="el-message-box__close el-icon-close"></i>
                    </button>
                </div>
                <div class="el-message-box__btns">
                    <button type="button" @click.stop.prevent="onclose" class="el-button el-button--default el-button--small">
                        <span>取消</span>
                    </button>
                    <button type="button" @click.stop.p.prevent="onConfirm" class="el-button el-button--default el-button--small el-button--primary ">
                        <span>确定</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: "MessageBox",
    data(){
        return{
            message: '消息提示',
            dialogVisible: false,
        }
    },
    methods:{

        confirm() {
            this.dialogVisible = true;
            return new Promise((resolve, reject) => {
                this.resolve = resolve;
                this.reject = reject;
            });
        },

        onclose(){
            this.dialogVisible = false;
            this.reject(true);
        },

        onConfirm(){
            this.dialogVisible = false;
            this.resolve(true);
        },

    }
}
</script>

<style scoped>
.ss-message_box__body{

}
.el-message-box__wrapper_bg{
    background-color: rgba(0, 0, 0, 0.35);
}
</style>