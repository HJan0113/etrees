<template>
  <!-- <div>
    <el-form ref="form" label-width="70px" :inline="true" class="login-container" :model="form" :rules="rules">
      <h3 class="login_title">系统登录</h3>

      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入账号"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
      </el-form-item>

      <el-form-item >
        <el-button style="margin-left: 70px;margin-top: 10px;" type="primary" @click="submit">登录</el-button>
      </el-form-item>
    </el-form>
  </div> -->
  <div class="container">
    <div class="logo">
      <img style="width: 170px;height: 45px;" src="../../assets//logo.png" alt="App Logo">
    </div>
    <h1>欢迎登录</h1>
    <el-form ref="form" label-width="70px" :inline="true" class="login-container" :model="form" :rules="rules">
      <div class="input-group">
        <el-form-item label="用户名" prop="username">
          <input type="text" v-model="form.username" placeholder="请输入用户名">
        </el-form-item>
      </div>
      <div class="input-group">
        <el-form-item label="密码" prop="password">
          <input type="password"  v-model="form.password" placeholder="请输入密码">
        </el-form-item>
      </div>
      <el-button type="primary" @click="submit">登录</el-button>
    </el-form>
  </div>
</template>
<script>
import { loginData } from "@/api";

export default {
  data() {
    return {
      //登录输入数据
      form: {
        username: '',
        password: ''
      },
      dialogVisible1: false,
      rules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入用户名' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入密码' }
        ]
      }
    }
  },

  methods: {
    submit() {
      //数据校验
      this.$refs.form.validate((validate) => {
        //如果通过数据校验
        if (validate) {
          loginData(this.form).then((res) => {
            if (res.code === 1) {
              this.$message.success("登录成功")
              console.log("后台返回的参数是: ", res)
              //将token存储到浏览器
              localStorage.setItem("EmpToken", res.data)
              //将用户权限存在本地存储中
              let permission = res.map.menuList;
              localStorage.setItem("EmpPerm", JSON.stringify(permission))
              this.$store.dispatch('getmsg')
              //在登录完成后清除form数据,防止注册页面数据还以上次登录的数据一致
              this.$refs.form.resetFields()
              //跳转到首页
              this.$router.push("/")
            } else {
              this.$message.error("登录失败")
            }
          })
        } else {
          this.$message.error("登录失败")
        }
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          //关闭注册弹窗后清除已经输入的数据
          done();
        })
        .catch(_ => {
          //注册时点击取消,也将之前输入的数据清除
          console.log("已经取消")
          done();
        });
    },
  },

  watch: {
    dialogVisible1: {
      handler(newValue) {
        if (newValue === false) {
          this.$refs.data.resetFields()
          this.resi = {}
        }
      }
    }
  }
}
</script>
<style scoped>
/* // .login-container {
//   width: 400px;
//   border: 1px solid aquamarine;
//   margin: 180px auto;
//   padding: 35px 35px 15px 35px;
//   background-color: rgb(255, 247, 245);
//   // background: url(../assets/image/hutao.jpg);
//   border-radius: 15px;
//   box-shadow: 1px 1px 25px #cac6c6;
//   box-sizing: border-box;

//   .login_title {
//     text-align: center;
//     margin-bottom: 40px;
//     color: cornflowerblue;
//   }

//   .el-input {
//     width: 198px;
//   }
// } */

.input-group >>>.el-form-item--mini .el-form-item__label {
  line-height: none;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-image: linear-gradient(120deg, #84fab0 0%, #8fd3f4 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  overflow: hidden;
}

.container {
  background-color: #ffffff;
  padding: 30px;
  width: 90%;
  max-width: 400px;
  border-radius: 20px;
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
  animation: fadeIn 1s ease;
  margin: 120px auto;
}

.logo {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  
}

.logo img {
  width: 60px;
  height: 60px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
  font-weight: bold;
}

.input-group {
  margin-bottom: 15px;
  position: relative;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 10px;
  border: none;
  border-bottom: 2px solid #ddd;
  border-radius: 0;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s ease;
}

input:focus {
  border-color: #007bff;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  border: none;
  border-radius: 5px;
  color: #ffffff;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.16), 0 2px 5px rgba(0, 0, 0, 0.23);
}

button:hover {
  background-color: #0056b3;
}

.forgot-password {
  text-align: center;
  margin-top: 15px;
}

.forgot-password a {
  color: #007bff;
  text-decoration: none;
}

.forgot-password a:hover {
  text-decoration: underline;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }

  100% {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
