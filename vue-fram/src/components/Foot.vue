<template>
  <div class="con">
    <div class="area">
      <div class="link">
        <a href="http://www.cnsoftbei.com/">软件杯官网</a>
        <a href="https://ecloud.10086.cn/home/">移动云官网</a>
        <a href="https://jiutian.10086.cn/edu/#/home">九天毕昇官网</a>
        <a href="https://ecloud.10086.cn/home/product-introduction/jiutiandl">九天智算平台</a>
        <a href="https://ecloud.10086.cn/home/product-introduction/interactive">智能交互平台</a>
      </div>
      <div style="margin: 0 auto;width: 70%;">
        <div class="describe">
          智慧农识——基于深度学习的植物表型识别系统（ETREES）
        </div>
        <div class="team">
          这不仅仅是植物病虫害识别系统，更是一款功能完善的助农产品！
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style scoped>
.con {
  position: absolute;
  top: 100%;
  height: 90px;
  width: 100%;
  /* margin-top: 30px; */
  /* 设置半透明的白色背景 */
  background-color: rgba(255, 255, 255, 0.5);
  /* 添加内外阴影模糊效果 */
  box-shadow: 0 0 20px rgba(255, 255, 255, 0.5) inset, 0 0 20px rgba(255, 255, 255, 0.5);
}

.area {
  margin: 0 auto;
  height: 90px;
  width: 500px;
  /* background-color: pink; */
  overflow: hidden;
}

.link {
  margin-top: 20px;
  text-align: left;
}

.link a {
  margin-right: 10px;
  text-decoration: none;
}

.describe {
  text-align: left;
  font-size: 12px;
  /* color:darkgrey; */
}

.team {
  text-align: left;
  font-size: 12px;
}
</style>