<template>
    <div class="ss-toast__body" v-if="messageShow">
        <div class="ss-toast__content">
            <div class="ss-toast__content__text">
                <!--<i class="el-icon-loading"></i>-->
                <span >{{ message }}</span>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Toast",
    data() {
        return {
            message: '消息提示',
            messageShow: false,
        }
    }
}
</script>

<style lang="scss" scoped>
.ss-toast__body{
    --status-bar-height: 0px;
    --top-window-height: 0px;
    --window-left: 0px;
    --window-right: 0px;
    --window-margin: 0px;
    --window-top: calc(var(--top-window-height) + 0px);
    --window-bottom: 0px;
    user-select: none;
    -webkit-tap-highlight-color: transparent;
    flex-direction: column;
    flex-grow: 0;
    flex-basis: auto;
    align-content: flex-start;
    transition-property: opacity;
    transition-duration: 300ms;
    transition-timing-function: ease-out;
    z-index: 2030;
    background-color: rgba(0, 0, 0, 0.35);
    //backdrop-filter: blur(1px);
    display: flex;
    justify-content: center;
    align-items: center;
    //padding-top: 20vh;
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    .ss-toast__content{
        flex-grow: 0;
        flex-basis: auto;
        align-content: flex-start;
        display: flex;
        flex-direction: row;
        padding: 14px 22px;
        border-radius: 4px;
        align-items: center;
        max-width: 300px;
        position: relative;
        //color: #5ac725;
        background-color: #ffc200;
        //border-color: #BEF5C8;
        transform: translateY(0px);
        .ss-toast__content__text{
            font-size: 20rem;
            max-width: 90vw;
            //color: #5ac725;
            color: #333333;
            i{
                margin-right: 8rem;
            }
        }
    }
}
</style>