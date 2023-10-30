<template>
    <div class="container">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <i class="el-icon-back" style="font-size: 23px;"></i>
          <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
        </div>
        <div style="height: 320px;display: flex;flex-direction: column;justify-content: space-evenly;">
          <div style="display: flex;height: 30%;">
            <div style="flex: 1;text-align: center;align-self: center;">账号说明</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                用户注册账号后可享受平台更多的功能，系统会为每一个账号生成一个用户编号，用户编号作为用户的唯一凭证。
            </div>
          </div>
          <div style="display: flex;height: 30%;">
            <div style="flex: 1;text-align: center;align-self: center;">账号安全</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                应工信部要求每一个账号都必须绑定手机，绑定的手机作为用户安全操作的校验标准。平台不会将用户绑定的手机号用作他相关操作。
            </div>
          </div>
          <div style="display: flex;height: 30%;">
            <div style="flex: 1;text-align: center;align-self: center;">隐私安全</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                平台将竭尽全力保障每一位用户的隐私安全，所有权限都须经过用户同意后方才获取。用户在使用平台时留下的所有痕迹，在未得到用户允许的情况下，平台将未用户保密。
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