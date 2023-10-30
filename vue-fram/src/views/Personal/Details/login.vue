<template>
  <div class="container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <i class="el-icon-back" style="font-size: 23px;"></i>
        <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
      </div>
      <div style="height: 320px;display: flex;flex-direction: column;justify-content: space-evenly;">
        <div style="display: flex;height: 40%;">
          <div style="flex: 1;text-align: center;align-self: center;">账号注册</div>
          <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
            在注册账号时需填写用户名、密码、手机号，个人信息相关的字段，在注册时无需填写，可在个人中心页面自行添加。
          </div>
        </div>
        <div style="display: flex;height: 40%;">
          <div style="flex: 1;text-align: center;align-self: center;">用户登录</div>
          <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
            用户在登录时，可选择用户名密码登录或是手机验证码登录。登录状态与未登录状态均可享受平台识别等功能；为保障用户体验及权益，其余功能需要用户登录账号后使用。
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  methods: {
    back() {
      this.$router.push('/person/help')
    }
  }
}
</script>

<style></style>