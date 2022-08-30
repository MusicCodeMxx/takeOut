<template>
    <div id="layout">
        <router-view/>
        <tab-bar></tab-bar>
    </div>
</template>

<script>
import tabBar from "../components/tabBar"

export default {
    name: "LayOut",
    components:{ tabBar }
}
</script>

<style >
</style>