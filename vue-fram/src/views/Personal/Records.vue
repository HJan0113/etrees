<template>
  <div class="container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>识别记录</span>
      </div>
      <div style="padding: 5px 0 5px 10px;" v-for="(item, index) in list" :key="index">
        <div style="display: flex;width: 70%;justify-content: space-between;">
          <div>
            <img style="width: 100px;height: 100px;" :src="item.imgUrl" alt="">
          </div>
          <div style="align-self: center;">
            {{ item.name }}
          </div>
          <div style="align-self: center;">
            {{ new Date(item.createTime).toLocaleString() }}
          </div>
        </div>
        <el-divider></el-divider>
      </div>

    </el-card>
  </div>
</template>

<script>
import { getRecord } from '@/api'
export default {
  data() {
    return {
      list: []
    }
  },
  created() {
    this.getAll()
  },
  methods: {
    getAll() {
      getRecord().then(res => {
        if (res.code === 1) {
          this.list = res.data
        }
      })
    }
  },
  computed: {
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