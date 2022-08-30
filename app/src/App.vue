<template>
  <div id="app">
      <!--页面留存数据方法-->
      <keep-alive>
          <router-view ></router-view>
      </keep-alive>
  </div>
</template>

<script>
  export default {
      name: "App",
  }
</script>


<style scoped>

</style>
