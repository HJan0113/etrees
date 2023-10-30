<template>
  <!--  头部数据   -->
  <el-header style="font-size: 12px; border-bottom: 1px solid #ccc; line-height: 60px; display: flex">
    <div style="flex: 1;display: flex;font-size: 18px;align-items: center ">
      <!-- 控制侧边栏展开折叠 -->
      <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"/>
      <!-- 面包屑 -->
      <el-breadcrumb>
        <!--   遍历breadList 获取其path 和 title   -->
        <el-breadcrumb-item v-for="(item,index) in breadList" :key="index" :to="(item.path!==$route.path) ? {path: item.path} : '' ">{{ item.meta.title }}
          <!--判断要跳转的路由是否是当前路由,如果是,则不跳,否则报错-->
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-dropdown style="width: 70px; cursor: pointer" size="medium" trigger="click">
      <div>
        <el-avatar :src="avatar" style="margin-top: 10px;"></el-avatar>
      </div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="privateData">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="outCheck">退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

  </el-header>
</template>
<script>

export default {
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold', //收缩图标
      //面包屑
      breadList: [
        {path: '/', name: 'home', meta: {title: '首页'}}
      ]
    }
  },

  //监视路由跳转, 如果跳转的路由是breadList 中没有的, 则将其加入breadList中, 然后通过遍历,将其 title展示在面包屑
  watch: {
    $route(to, from) {
      //判断是否 存在于breadList中, 如果不存在,则添加
      if (this.breadList.findIndex(value => value.name === to.name) === -1) {
        this.breadList.push(to)
      }

    }
  },

  methods: {
    collapse() {
      //触发vuex的change方法,更改iscollapse的值
      this.$store.dispatch('change')

      //更改侧边框大小
      if (this.isCollapse) {
        this.$store.dispatch('changeside', 64)
        this.collapseBtnClass = 'el-icon-s-unfold'
      } else {
        this.$store.dispatch('changeside', 200)
        this.collapseBtnClass = 'el-icon-s-fold'
      }
    },

    //  登出方法
    outCheck() {
      this.$confirm("你确定要退出登录吗? ", "警告", {
        confirmButtonText: '确定', cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '成功退出'
        })
        //成功退出
        localStorage.clear("EmpToken");
        
        this.$store.dispatch('clear')
        this.$router.push("/login")
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        })
      })
    },

    //  跳转至个人页面
    privateData() {
      this.$router.push("/personal")
    },


  },

  computed: {
    //获取state中的isCollapse属性
    isCollapse() {
      return this.$store.state.isCollapse
    },

    // loginName() {
    //   let token = JSON.parse(localStorage.getItem("user"))
    //   return token.name
    // },
    
    // 获取用户头像
    avatar() {
      return this.$store.state.avatar
    }
  },

}


</script>
<style scoped>
  .el-breadcrumb {
    margin-left: 20px;
  }
</style>