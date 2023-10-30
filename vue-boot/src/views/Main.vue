<template>
  <el-container style="min-height: 100vh; border: 1px solid #eee">
    <!--  边框  -->
    <Aside/>
    <el-container>
      <!--头部-->
      <el-header>
        <Head />
      </el-header>
      <!-- 主界面 , 展示数据-->
      <Tags />
      <el-main>
        <!-- 子路由展示,作用是切换路由时,侧边和顶部数据不变,主页面内容跳转    -->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped></style>

<script>

import Aside from '../components/Aside'
import Head from '../components/Head'
import Tags from '../components/Tags'

export default {
  components: { Aside, Head, Tags },
  data() {
    return {
    }
  },
  created() {
    this.$store.dispatch('getmsg')
  },
  methods: {}
}
</script>
<style scoped>
.el-main {
  padding-top: 10px;
}

.el-header {
  padding: 0 10px;
}
</style>