<template>
    <div class="container">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <i class="el-icon-back" style="font-size: 23px;"></i>
          <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
        </div>
        <div style="height: 320px;display: flex;flex-direction: column;justify-content: space-evenly;">
          <div style="display: flex;height: 40%;">
            <div style="flex: 1;text-align: center;align-self: center;">健康提示</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                平台首页将自动未用户展示与病虫害相关的健康提示；健康提示内容将由平台随机推送内容。
            </div>
          </div>
          <div style="display: flex;height: 40%;">
            <div style="flex: 1;text-align: center;align-self: center;">图鉴信息</div>
            <div style="flex: 2;background-color: #f2f2f2;text-indent: 2em;padding: 20px;">
                平台图鉴功能囊括了多种植物及多种病害的相关信息，其中包括病害多发季、病害概述、发病原因及治疗方案供用户参考
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