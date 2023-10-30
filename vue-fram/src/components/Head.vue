<template>
  <el-menu background-color="white" class="nav" mode="horizontal" @select="handleSelect" :default-active="index" router>
    <img @click="backToHome" class="logo" src="../assets/logo.png" alt="">
    <el-menu-item index="/home">首页</el-menu-item>
    <el-menu-item index="/complete">图鉴</el-menu-item>
    <!-- <el-menu-item index="/mall">商城</el-menu-item> -->
    <el-menu-item index="/about">关于我们</el-menu-item>
      <el-input placeholder="请输入病害名称" prefix-icon="el-icon-search" v-model="search" @keyup.enter.native="Enter">
      </el-input>
      <el-button style="font-size: 17px;padding-bottom: 17px;color: skyblue;margin-left: 30px;"  type="text" @click="shop">商城</el-button>
    <!-- 如果已经登录,展示登录后的头像,显示下拉框 -->
    <el-dropdown class="person" v-show="isLogin">
      <el-avatar :size="50"
        :style="{ 'background-image': 'url(' + avatar + ')', 'background-size': 'cover' }"></el-avatar>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item class="drop" @click.native="personal">个人中心</el-dropdown-item>
        <el-dropdown-item class="drop" @click.native="logout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <!-- 如果没有登录,显示登录按钮 -->
    <el-button class="person" v-show="!isLogin" type="text" @click="login">登录</el-button>
  </el-menu>
</template>
<script>
import { getPersonMsg, searchHandbook } from '@/api'
export default {
  data() {
    return {
      // index: '',
      search: '',
    };
  },
  mounted() {
    //判断token 决定是否处于登录状态
    let hasToken = localStorage.getItem("token");
    if (hasToken) {
      this.$store.state.isLogin = true
      getPersonMsg().then(res => {
        this.$store.state.avatar = res.data.imgUrl == null ? 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png' : res.data.imgUrl
        if (res.data.isRealname === 1) {
          this.$store.dispatch('auth')
        } else {
          this.$store.dispatch('noauth')
        }
      })
    }
  },
  methods: {
    Enter() {
      if (this.search === '') {
        this.$message.error("请输入搜索内容")
        return
      }
      this.$store.dispatch('search', this.search)
      if (this.$route.path !== '/search') {
        this.$router.push(`/search`)
      }

    },
    // 菜单导航被选中时触发
    handleSelect(key) {
      this.$store.dispatch('setactive',key)
      // this.index = key;
    },
    shop() {
      this.$router.push('/mall')
    },
    // 登录
    login() {
      // 登录,跳转至登录页面,
      this.$router.push('/login')
    },
    // 登出
    logout() {
      // 提示
      this.$confirm('确定退出登录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //清除本地存储的token
        localStorage.removeItem('token')
        // 将头像换成登录
        this.$store.dispatch("islogin")
        //切换到未实名
        this.$store.dispatch('noauth')
        //切换到首页
        this.$router.push("/")
        this.$store.state.avatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
        this.$message({
          type: 'success',
          message: '退出成功!'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        });
      });
    },
    // 进入个人中心
    personal() {
      this.$router.push('/person')
    },
    backToHome() {
      this.$router.push('/home')
    }
  },
  computed: {
    // 获取登录状态
    isLogin() {
      return this.$store.state.isLogin
    },
    avatar() {
      return this.$store.state.avatar
    },
    index() {
      return this.$store.state.activeMeau
    }
  }
}
</script>
<style scoped>
.nav {
  display: flex;
}

.logo {
  width: 150px;
  height: 40px;
  align-self: center;
  margin-right: 30px;
  margin-left: 30px;
}

.userImg {
  width: 60px;
  border-radius: 50%;
}

.person {
  position: absolute;
  top: 6px;
  right: 35px
}

.el-button {
  color: black;
  font-size: 16px;
}

.el-input {
  width: 380px;
  margin-left: 100px;
  padding-top: 12px;
}

.el-menu-item {
  font-size: 16px;
}

.el-input>>>.el-input__inner {
  border-radius: 30px;
  background-color: transparent;
  color: black;
}

.el-input>>>.el-input__inner::placeholder {
  color: #9095b0;
}

.el-input>>>.el-input__icon {
  color: black;
  /* width: auto; */
  height: 110%;
}
</style>