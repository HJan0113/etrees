<template>
  <div class="container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>我的订单</span>
      </div>
      <div v-for="(item, index) in orderValue" :key="index">
        <div style="display:flex;justify-content: space-between;width: 90%;padding: 0 0 10px 10px;">
          <div style="text-align: center;">
            <div>{{ item.orderId }}</div>
            <div>
              <img style="width: 100px;height: 100px;" :src="item.imgUrl" alt="">
            </div>
          </div>
          <div style="align-self: center;">
            {{ item.productName }}
          </div>
          <div style="align-self: center;text-align: right;">
            <div>已下单</div>
            <div>{{ item.productPrice }}</div>
            <div>x {{ item.quantity }}</div>
            <div>实付款: {{ item.totalPrice }}</div>
          </div>
        </div>
        <el-divider></el-divider>
      </div>

    </el-card>
  </div>
</template>

<script>
import { getOrder } from '@/api'
export default {
  created() {
    this.getAll()
  },
  data() {
    return {
      orderValue: []
    }
  },
  methods: {
    getAll() {
      getOrder().then(res => {
        if (res.code === 1) {
          this.orderValue = res.data

        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  display: flex;
  height: 500px;
  flex-direction: column;
}

.box-card::-webkit-scrollbar {
  width: 0;
  /* 隐藏滚动条 */
}

.box-card {
  flex: 1;
  overflow: auto;
  margin: 20px auto;
  width: 900px;
  height: 30%;
  /* background-color: rgba(253, 246, 227, 0.6); */
  /* border-radius: 30px; */
}
</style>