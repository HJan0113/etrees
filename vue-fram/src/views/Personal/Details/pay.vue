<template>
    <div class="container">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <i class="el-icon-back" style="font-size: 23px;"></i>
          <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
        </div>
        <div style="height: 320px;display: flex;flex-direction: column;justify-content: space-evenly;">
          <div style="display: flex;height: 30%;">
            <div style="flex: 1;text-align: center;align-self: center;">商城商品</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                平台提供销售贴合用户需求的商品销售平台，专注于农作物、农耕器械、治疗药物及物品等相关商品。平台药物不可替代医嘱。
            </div>
          </div>
          <div style="display: flex;height: 30%;">
            <div style="flex: 1;text-align: center;align-self: center;">收货信息</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                用户填写的收货信息仅用于平台为用户发货，且平台承诺为用户填写的收货信息提供安全保障。
            </div>
          </div>
          <div style="display: flex;height: 30%;">
            <div style="flex: 1;text-align: center;align-self: center;">支付安全</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                平台接入第三方支付接口用于用户购买商品下单时的付款，用户购买商品时的支付安全由第三方支付平台负责。我司不对支付安全承担任何责任。
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