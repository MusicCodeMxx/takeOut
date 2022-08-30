<template>
    <div class="popup__body" v-text="message"></div>
</template>

<script>
export default {
    name: "TestPopupMessage",
    props: ['message']
}
</script>

<style scoped>
.popup__body{
    position: fixed;
    top: 180rem;
    right: 0;
    left: 120rem;
    /*height: 50rem;*/
    /*line-height: 50rem;*/
    background-color: rgba(0,0,0,0.35);
    text-align: center;
    padding: 8rem;
    z-index: 9999;
    color: #FFFFFF;
    font-size: 12rem;
    font-weight: 300;
    letter-spacing: 0;
    /*filter: drop-shadow( 0px 20px 10px rgba(0, 0, 0, 0.5));*/
    /*filter: blur(20px); !*滤镜*!*/
    backdrop-filter: blur(10px);
}
</style>