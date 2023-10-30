<template>
    <div class="container">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <i class="el-icon-back" style="font-size: 23px;"></i>
          <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
        </div>
        <div style="height: 320px;display: flex;flex-direction: column;justify-content: space-evenly;">
          <div style="display: flex;height: 40%;">
            <div style="flex: 1;text-align: center;align-self: center;">实名须知</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                应工信部要求，涉及电信业务的相关操作均需要用户实名认证；用户需要实名认证方可进行商城商品的购买。
            </div>
          </div>
          <div style="display: flex;height: 40%;">
            <div style="flex: 1;text-align: center;align-self: center;">平台允诺</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                平台承诺，对于用户填写的实名信息，平台有义务进行保密，且对于用户填写的实名资料仅用户实名认证功能。
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