<template>
  <div class="content">
    <div class="login-wrapper">
      <!-- 左侧图像页面 -->
      <div class="left-img">
        <div class="glass">
          <div class="tips">
            <div class="title">
              ETREES
            </div>
            <h1>欢迎加入ETREES</h1>
            <span>智慧农识: 基于深度学习的植物表型识别系统</span>
            <span>快来加入吧</span>
          </div>
        </div>
      </div>
      <div class="right">
        <!-- 右侧登录页面 -->
        <transition name="el-zoom-in-center">
          <div class="right-login-form wrapper" v-if="!logRes">
            <el-form :model="form" :rules="rules" ref="form" class="form-wrapper">
              <h1>登录</h1>
              <el-form-item class="input-items" prop="userName">
                
                <el-input clearable class="input" type="text" placeholder="请输入用户名" v-model="form.userName">
                  <i slot="prefix" class="el-input__icon el-icon-user"></i>
                </el-input>
              </el-form-item>
              <el-form-item class="input-items" prop="password">
                
                <el-input clearable :show-password="true" class="input" type="password" placeholder="请输入密码" v-model="form.password">
                  <i slot="prefix" class="el-input__icon el-icon-edit-outline"></i>
                </el-input>
              </el-form-item>
              <button class="btn" @click.prevent="submit">登录</button>
              <div class="siginup-tips">
                <span>没有账号?</span>
                <span @click.prevent="switchLog">注册</span>
              </div>
            </el-form>
          </div>
        </transition>
        <!-- 右侧注册页面 -->
        <transition name="el-zoom-in-center">
          <div class="right-login-form wrapper" v-if="logRes">
            <el-form :model="regisform" :rules="rulepass" ref="form" class="form-wrapper">
              <h1>注册</h1>
              <el-form-item class="input-items" prop="userName">
                
                <el-input clearable class="input" type="text" placeholder="请输入用户名" v-model="regisform.userName">
                  <i slot="prefix" class="el-input__icon el-icon-user"></i>
                </el-input>
              </el-form-item>
              <el-form-item class="input-items" prop="password">
                
                <el-input :show-password="true" clearable class="input" type="password" placeholder="请输入密码" v-model="regisform.password">
                  <i slot="prefix" class="el-input__icon el-icon-edit-outline"></i>
                </el-input>
              </el-form-item>
              <el-form-item class="input-items" prop="phone">
                
                <el-input clearable class="input" type="text" placeholder="请输入手机号" v-model="regisform.phone">
                  <i slot="prefix" class="el-input__icon el-icon-phone"></i>
                </el-input>
              </el-form-item>
              <el-form-item class="input-items" prop="checkCode">
                
                <el-input clearable class="input" type="text" placeholder="请输入验证码" v-model="checkCode" :disabled="hasPhone">
                  <i slot="prefix" class="el-input__icon el-icon-s-claim"></i>
                  <el-button slot="append" style="color: red;" :disabled="hasPhone" @click="postverfi">发送验证码</el-button>
                </el-input>
              </el-form-item>

              <button class="btn" @click.prevent="register">注册</button>
              <div class="siginup-tips">
                <span>已有账号?</span>
                <span @click="switchLog">登录</span>
              </div>
            </el-form>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
import { loginData, resign, postVerifi, getPersonMsg } from '@/api';
export default {

  data() {

    return {
      form: {
        userName: '',
        password: '',
      },
      regisform: {
        userName: '',
        password: '',
        phone: '',
      },
      checkCode: '',

      rules: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],

      },
      rulepass: {
        userName: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
      },
      logRes: false   //是否登录, false表示没登录,true表示已经登录
    }
  },
  methods: {
    // 发送验证码
    postverfi() {
      //校验输入的手机号是否合理
      this.$refs.form.validateField('phone', (valid) => {
        if (valid === '') {
          postVerifi(this.regisform).then(res => {
            //如果响应码为 1 即成功
            if (res.code == 1) {
              this.$message({
                message: res.data,
                type: 'success'
              });
            } else {  //否则,失败,提示错误信息
              this.$message.error(res.msg)
            }
          })
        }
        else {
          this.$message.error(valid)
        }
      })

    },
    // 登录提交表单
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          loginData(this.form).then(res => {
            //如果登录失败
            if (res.code == 0) {
              this.$message.error(res.msg)
              return false
            }
            //登录成功,将后端返回的token存储在本地存储中
            else {
              localStorage.setItem("token", res.data.token)
              localStorage.setItem("id", res.data.id)
              this.$store.dispatch("islogin")
              this.$message.success("登录成功^_^")
              this.$router.push('/home')
            }
          })
        } else {
          this.$message.error("请正确输入用户名和密码")
          return false;
        }
      });
    },
    // 当切换登录和注册时,将form的数据清除
    switchLog() {
      this.logRes = !this.logRes;
      this.$refs.form.resetFields();
    },
    // 注册
    register() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          resign(this.regisform, this.checkCode).then(res => {
            console.log("后端返回的数据为: ", res)
            if (res.code === 0) {
              this.$message.error(res.msg);
            }
            else {
              this.$message({
                message: res.data,
                type: 'success'
              })
              this.switchLog()
            }
          })
        } else {
          
        }
      })
    }
  },
  computed: {
    hasPhone() {
      return !this.regisform.phone
    }
  }
}


</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  font-family: "Aguazyuan", Courier, monospace;
}
div>>>.el-input-group__append, .el-input-group__prepend {
  background-color: transparent;
  border: none;
  border-bottom: 1px #dddddd solid;
}
div>>>.el-input.is-disabled .el-input__inner {
  background-color: transparent;
}
.wrapper1 {
  position: absolute;
  top: -50%;
  /* left: 50%; */
  /* transform: translate(-50%); */
}

.right {
  flex: 1;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity .5s;
}

.fade-enter,
.fade-leave-to

/* .fade-leave-active below version 2.1.8 */
  {
  opacity: 0;
}

.content {
  width: 100vw;
  height: 100vh;
  /* background-color: #a4de61; */
  background-image: url(../../assets/bg1.jpg);
  position: relative;
}

.content .login-wrapper {
  width: 70vw;
  height: 80vh;
  background-color: #fff;
  border-radius: 40Px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: flex;
}

.content .login-wrapper .left-img {
  border-radius: 40Px;
  flex: 1;
  background: url(../../assets/pic.jpg) no-repeat;
  background-size: cover;
  position: relative;
}

.content .login-wrapper .left-img .glass {
  width: 80%;
  height: 44%;
  padding: 20Px;
  color: #fff;
  position: absolute;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -20%);
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 8Px 32Px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(10Px);
  -webkit-backdrop-filter: blur(10Px);
  border-radius: 15Px;
  border: 1Px solid rgba(255, 255, 255, 0.48);
}

.content .login-wrapper .left-img .glass .tips .title {
  width: 40%;
  font-weight: 600;
  font-size: 30px;
  text-align: center;
  padding: 10Px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 8Px 32Px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(10Px);
  -webkit-backdrop-filter: blur(10Px);
  border-radius: 15Px;
  border: 1Px solid rgba(255, 255, 255, 0.48);
}

.content .login-wrapper .left-img .glass .tips h1 {
  margin: 15Px 0;
}

.content .login-wrapper .left-img .glass .tips span {
  margin: 5Px 0;
  display: block;
  font-weight: 100;
  font-size: 25px;
}

.content .login-wrapper .right-login-form {
  flex: 1;
  position: relative;
}

.content .login-wrapper .right-login-form .form-wrapper {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 70%;
  transform: translate(-50%, -50%);
}

.content .login-wrapper .right-login-form .form-wrapper h1 {
  font-size: 30Px;
  text-align: center;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items {
  margin: 25px auto;
  width: 100%;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .input-tips {
  /* display: inline-block; */
  font-weight: 600;
  font-size: 17Px;
  margin: 10Px 0;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .inputs {
  width: 100%;
  height: 50Px;
  border-radius: 10Px;
  border: 1Px solid rgb(227, 227, 227);
  padding: 20Px;
  box-sizing: border-box;
  outline: none;
  transition: 0.5s;
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .inputs:focus {
  border: 1Px solid rgb(128, 106, 196);
}

.content .login-wrapper .right-login-form .form-wrapper .input-items .forgot {
  float: right;
  font-weight: 600;
  text-align: right;
  margin: 5Px 0;
  font-size: 13Px;
  cursor: pointer;
}

.content .login-wrapper .right-login-form .form-wrapper .btn {
  width: 100%;
  height: 50Px;
  background-color: rgba(88, 226, 136, 0.5);
  border: 0;
  border-radius: 10Px;
  color: #fff;
  font-size: 25Px;
  margin: 20px 0;
  cursor: pointer;
}

.content .login-wrapper .right-login-form .form-wrapper .siginup-tips {
  text-align: center;
  font-weight: 600;
  margin: 10Px 0;
}

.content .login-wrapper .right-login-form .form-wrapper .siginup-tips span:last-child {
  color: rgb(110, 87, 179);
  cursor: pointer;
}

.content .login-wrapper .right-login-form .form-wrapper .other-login .divider {
  width: 100%;
  margin: 20Px 0;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.content .login-wrapper .right-login-form .form-wrapper .other-login .divider .line {
  display: inline-block;
  max-width: 35%;
  width: 35%;
  flex: 1;
  height: 1Px;
  background-color: rgb(162, 172, 185);
}

.content .login-wrapper .right-login-form .form-wrapper .other-login .divider .divider-text {
  vertical-align: middle;
  margin: 0 20Px;
  display: inline-block;
  width: 50Px;
  color: rgb(162, 172, 185);
  white-space: normal;
}

.content .login-wrapper .right-login-form .form-wrapper .other-login .other-login-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content .login-wrapper .right-login-form .form-wrapper .other-login .other-login-wrapper .other-login-item {
  width: 70Px;
  padding: 10Px;
  text-align: center;
  border-radius: 10Px;
  cursor: pointer;
  font-weight: 600;
  color: rgb(51, 49, 116);
  margin: 0 10Px;
  transition: 0.4s;
}

.content .login-wrapper .right-login-form .form-wrapper .other-login .other-login-wrapper .other-login-item img {
  width: 40Px;
  height: 40Px;
  vertical-align: middle;
}

.content .login-wrapper .right-login-form .form-wrapper .other-login .other-login-wrapper .other-login-item span {
  vertical-align: middle;
}

.content .login-wrapper .right-login-form .form-wrapper .other-login .other-login-wrapper .other-login-item:hover {
  width: 80Px;
  height: 50%;
  background-color: #fff;
  border: 0;
  box-shadow: 1Px 10Px 32Px 1Px rgba(186, 175, 221, 0.37);
}

.el-input>>>.el-input__inner {
  height: 47Px;
  /* border-radius: 20Px; */
  border: none;
  border-bottom: 1px #dddddd solid;
}
div>>>.el-input__inner {
  border-radius: 0;
}
.el-input__icon {
  width: auto;
}

.el-input>>>.el-input__prefix {
  left: 13px;
}

.el-input--prefix>>>.el-input__inner {
  padding-left: 45px;
  font-size: 20px;
}

.el-input>>>.el-input__inner::placeholder {
  font-size: 17px;
  /* color: #fdf6e3; */
}

.input-tips {
  text-align: left;
}

.wrapper {
  position: relative;
  top: 50%;
  /* width: 100%; */
  perspective: 800px;
}

.input-items>>>.el-form-item__content {
  line-height: 30px;
}

@media (max-width: 1024Px) {
  .login-wrapper {
    padding: 20Px;
  }

  .login-wrapper .left-img {
    display: none;
  }

  .login-wrapper .right-login-form {
    padding: 20Px;
  }
}
</style>