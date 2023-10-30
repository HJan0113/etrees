<template>
  <div>
    <el-dialog title="收货地址" :visible.sync="dialogTableVisible" width="1100px" top="60px">
      <div>
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>订单信息</span>
          </div>
          <div style="display: flex;justify-content: space-evenly;">
            <div>
              订单编号: {{ RecordData.orderId }}
            </div>
            <div>
              订单信息: 已下单
            </div>
            <div>
              下单时间: {{ new Date(RecordData.createTime).toLocaleString() }}
            </div>
          </div>
        </el-card>
      </div>
      <div style="display: flex;justify-content: space-between;">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span>用户信息</span>
          </div>
          <div style="width: 300px;margin-left: 100px;">
            <div style="line-height: 50px;">用户编号: {{ RecordData.userId }}</div>
            <div style="line-height: 50px;">用户手机: {{ RecordData.phone }}</div>
          </div>
        </el-card>

        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span>商品信息</span>
          </div>
          <div style="width: 300px;margin-left: 60px;">
            <div style="line-height: 30px;">商品编号: {{ RecordData.productId }}</div>
            <div style="line-height: 30px;">下单数量: {{ RecordData.quantity }}</div>
            <div style="line-height: 30px;">下单金额: {{ RecordData.totalPrice }}</div>
          </div>
        </el-card>
      </div>
      <div>
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span>收件信息</span>
          </div>
          <div>
            <div style="display: flex;justify-content:space-evenly">
              <div>收件人: {{ RecordData.userId }}</div>
              <div>手机号: {{ RecordData.phone }}</div>
            </div>
            <div style="margin-left: 260px;margin-top: 20px;">收件地址: {{ RecordData.address }}</div>
          </div>
        </el-card>
      </div>
    </el-dialog>
    <div>
      <el-form :inline="true" :model="form" style="text-align: center;margin-top: 30px">
        <el-form-item label="查询订单">
          <el-input v-model="form.orderId" placeholder="订单编号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.productId" placeholder="商品编号"></el-input>
        </el-form-item>
        <el-button style="margin-left: 160px;" type="primary" @click="getAll()">查询</el-button>
        <el-button style="margin-left: 30px;" type="primary" @click="reset">重置</el-button>
      </el-form>
    </div>
    <div class="content">
      <el-table :data="tableData" style="width: 100%;margin-top: 30px;">
        <el-table-column prop="orderId" label="订单编号" width="180">
        </el-table-column>
        <el-table-column prop="productId" label="商品编号" width="180">
        </el-table-column>
        <el-table-column prop="userId" label="购买用户">
        </el-table-column>
        <el-table-column prop="productPrice" label="下单金额">
        </el-table-column>
        <el-table-column prop="status" label="订单状态">
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="score">
            <el-button style="font-size: 15px;" type="text" @click="detailed(score.row)">详细</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="padding: 10px 0">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage"
          :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes,prev,pager,next,jumper"
          :total="this.total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { getRecord } from '@/api'
export default {
  data() {
    return {
      dialogTableVisible: false,
      currentpage: 0, //当前页数
      pageSize: 5,  //当前页记录数
      total: 0, //总记录数
      form: {
        orderId: '',
        productId: ''
      },
      tableData: [],
      RecordData: {

      }
    }
  },
  created() {
    this.getAll()
  },
  methods: {
    //在切换记录数时将pageSize属性修改为切换的页码记录数
    handleSizeChange(val) {
      this.pageSize = val;
      this.getAll()
    },
    //在切换当前页码数时,将currentpage修改为切换的页码数
    handleCurrentChange(val) {
      this.currentpage = val;
      this.getAll()
    },
    detailed(data) {
      this.dialogTableVisible = true
      this.RecordData = data
    },
    reset() {
      this.form.orderId = '',
      this.form.productId = '',
      this.getAll()
    },
    //获取所有订单分页
    getAll() {
      getRecord(this.currentpage, this.pageSize, this.form).then((res) => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    }
  }
}
</script>

<style scoped>
div/deep/.el-card__header {
  background-color: #f2f2f2;
}
.el-table>>>.cell {
  font-size: 15px;
  text-align: center;
}
</style>