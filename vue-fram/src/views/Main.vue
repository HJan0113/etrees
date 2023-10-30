<template>
  <el-container>
    <!-- 背景轮播图 -->
    <div class="image">
      <el-carousel :interval="3000" arrow="false"  indicator-position="none" class="carousel" height="100vh">
        <el-carousel-item>
          <img class="pic" src="../assets/R-C (2).jpg" alt="">
        </el-carousel-item>
        <el-carousel-item>
          <img class="pic" src="../assets/pic1.jpg" alt="">
        </el-carousel-item>
        <el-carousel-item>
          <img class="pic" src="../assets/pic2.jpg" alt="">
        </el-carousel-item>
      </el-carousel>
    </div>
    <!-- 头部数据 -->
    <el-header style="padding: 0;">
      <Head/>
    </el-header>

    <!-- 中心界面 -->
    <el-container>
      <el-main>
        <transition name="el-zcenter">oom-in-
          <router-view />
        </transition>
      </el-main>
    </el-container>

    <!-- 底部界面 -->
    <el-footer style="padding: 0;">
      <Foot/>
    </el-footer>
  </el-container>
</template>

<script>
import Head from '@/components/Head.vue'
import Foot from '@/components/Foot.vue';
export default {
  name: 'Main',
  components: {
    Head, Foot
  }
}
</script>
<style>
.image {
  position: fixed;
  top: 0;
  left: 0;
  z-index: -999;
  height: 100vh;
  width: 100vw;
}

.pic {
  width: 100vw;
  height: 100vh;
}

.el-carousel--horizontal {
  width: 100vw;
}

/* .el-carousel__item.is-animating {
  transition: transform 2s ease !important;
} */
</style>