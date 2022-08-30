<template>
    <footer class="footer_container">
        <div
          v-for="obj in list"
          :key="obj.id"
          class="footer_item"
          :class="{on:isCurrentRoute(obj.path)}"
          @click.stop.prevent="routerGoTo(obj.path)">
            <!--在静态页面上，添加动态的 class，添加路由跳转事件-->
            <span class="footer_icon">
                <i :class="'iconfont ' + obj.icon"></i>
            </span>
            <span class="footer_text" v-text="obj.name"></span>
        </div>
      </footer>
</template>


<script>

    export default {
        name: "tabBar",
        data() {
        return {
            // 菜单列表
            list:[
                { id: 1,icon: 'el-icon-dish',name: '首页',path: '/home'},
                { id: 2,icon: 'el-icon-s-order',name: '订单',path: '/order'},
                { id: 4,icon: 'el-icon-bank-card',name: '优惠',path: '/coupon/get'},
                { id: 3,icon: 'el-icon-user-solid',name: '我的',path: '/me'},
            ]
        }
        },
        mounted() { },
        methods:{
        /*作用：用来动态切换class，判断当前的路由与传进来的路由是不是一样*/
        isCurrentRoute(path){
            let url = this.$route.path;
            if(path == this.list[0].path){
                // 如果是 / 而且是列表中的第一个就显示激活
                if(url === "/"){ return true; }
            }
            return url === path  /*this.$route获得当前路由*/
        },
        /*作用：路由跳转，使用$router.replace不会向history添加记录*/
        routerGoTo(path){
            /* 如果是当前按钮就禁止跳转 */
            if(this.$route.path === path){  return;  }
            this.$router.replace(path)  /*this.$router路由实例，里面有很多api*/
        }
        }
    }
</script>
  
<style lang="scss">

.footer_container {
    /*一像素上边框 */
    border-top: 1px solid #e4e4e4;
    /*固定定位在底部*/
    position :fixed; 
    /*显示级别*/
    z-index: 100 ;
    bottom: 0;
    left :0;
    width: 100%;
    height: 50px;
    background-color: #ffffff;
    /*实现四列两行*/
    display: flex;  
    /*主轴行排*/
    flex-direction: row; 
    /*主轴对齐方式*/
    justify-content: center; 
    .footer_item{
    flex: 1;
    display: flex ; /*实现两行*/
    text-align: center ;/*行内元素居中*/
    flex-direction: column;
    align-items: center;
    margin: 5px;
    color: #999999;
    span{
        font-size: 12px;
        margin-top: 1px;
        margin-bottom: 1px;
        .iconfont{
         font-size: 22px;
        }
    }
    }
    /*动态的class*/
    .on {
    color:#ffc200;
    }

}

</style>
