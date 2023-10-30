<template>
  <!--  边框  -->
  <el-aside :width="sideWide + 'px'" style="background-color: rgb(238, 241, 246)">
    <el-menu style="min-height: 100%" active-text-color="#409EFF" :collapse-transition="false"
      class="el-menu-vertical-demo" :collapse="isCollapse" router>
      <!--        添加侧边顶栏文字-->
      <div style="height: 60px; line-height: 60px; display: flex;margin-left: 20px;">
        <!--       添加侧边栏图标   -->
        <img src="../assets/adm.png" style="width: 70px;height: 35px; margin-right: 5px;margin-top: 5px;align-self: center;">
        <b style="color: black;" v-show="!isCollapse">后台管理</b>
      </div>
      <!-- 侧边栏 -->
      <template v-for="route in menuList">
        <!-- 有二级菜单的目录 -->
        <el-submenu v-if="route.children.length!==0" :index="route.path" :key="route.path">
          <template slot="title">
            <i :class="route.icon"></i>
            <span slot="title">{{ route.name }}</span>
          </template>
          <el-menu-item v-for="child in route.children" :key="child.path" :index="'/'+route.path+'/'+child.path">{{child.name }}</el-menu-item>
        </el-submenu>
        <!-- 无二级菜单的目录 -->
        <el-menu-item v-else :index="'/'+route.path" :key="route.path">
          <i :class="route.icon"></i>
          <span slot="title">{{ route.name }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </el-aside>
</template>

<script>
export default {
  
  data() {
    return {
      
    }
  },
  created() {
    console.log("所有的组件: ", this.menuList)
  },
  methods: {
  },

  computed: {
    isCollapse() {
      return this.$store.state.isCollapse
    },
    //控制 侧边栏宽度, 
    sideWide() {
      return this.$store.state.sideWide
    },
    menuList() {
      return this.$store.getters.getMenuList
    }
  }
}
</script>
<style scoped>
.el-submenu .el-menu-item {
  min-width: 0;
}
</style>