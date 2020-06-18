<template>
  <div>
    <h2>Home 页面</h2>
    <el-button type="primary" @click="getUsers">获得用户信息</el-button>
    <el-button type="primary" @click="getMenus">获得菜单信息</el-button>
  </div>
</template>
<script>
  import axios from 'axios'
  import mock from '../mock/mock'
  export default {
    name: 'Home',
    methods: {
      getUsers(){
        this.$api.getUser().then(function(res){
          console.debug(res.data)
        }).catch(function(res){
          alert(res)
        })
      },
      getMenus(){
        this.$api.getMenu().then(function(res){
          console.debug(res.data)
        }).catch(function(res){
          alert(res)
        })
      }
    }
  }
</script>
<style></style>
