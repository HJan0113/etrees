<template>
  <div>
    <el-dialog title="反馈信息" :visible.sync="dialogTableVisible" width="1060px">
      <div
        style="width: 700px;height: 200px;margin: 0 auto;display: flex;flex-direction: column;justify-content: space-around;">
        <div style="display: flex;justify-content: space-between;">
          <div><b>反馈编号</b>: {{ UpData.id }}</div>
          <div><b>用户编号</b>: {{ UpData.userId }}</div>
          <div><b>反馈时间</b>: {{ new Date(UpData.createTime).toLocaleString() }}</div>
        </div>
        <div><b>反馈主题</b>: {{ UpData.theme }}</div>
        <div><b>反馈内容</b>: {{ UpData.content }}</div>
      </div>
    </el-dialog>
    <el-card>
      <div>
        <el-form :inline="true" ref="form" :model="search" style="margin-top: 30px;text-align: center;">
          <el-form-item label="查询条件">
            <el-input v-model="search.feedback_id" placeholder="反馈编号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="search.user_id" placeholder="用户编号"></el-input>
          </el-form-item>
          <el-button style="margin-left: 60px;" type="primary" @click="getAll()">查询</el-button>
          <el-button style="margin-left: 30px;" type="primary" @click="clean">重置</el-button>
        </el-form>
      </div>
      <div style="margin-top: 20px;">
        <el-table :data="tableData" style="width: 100%;" :header-cell-style="{ background: '#eef1f6', color: '#606266' }">
          <el-table-column prop="id" label="反馈编号" width="180">
          </el-table-column>
          <el-table-column prop="userId" label="用户编号" width="180">
          </el-table-column>
          <el-table-column prop="theme" label="反馈主题">
          </el-table-column>
          <el-table-column label="操作">
            <template v-slot="score">
              <el-button type="text" @click="detail(score.row)">详细</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="padding: 10px 0">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage"
          :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes,prev,pager,next,jumper"
          :total="this.total">
        </el-pagination>
      </div>
    </el-card>

  </div>
</template>

<script>
import { getFeedBack } from '@/api'
export default {
  data() {
    return {

      dialogTableVisible: false,
      currentpage: 0, //当前页数
      pageSize: 5,  //当前页记录数
      total: 0, //总记录数
      search: {
        feedback_id: '',
        user_id: ''
      },
      UpData: {
        id: '',
        userId: '',
        createTime: '',
        theme: '',
        content: ''
      },
      tableData: []
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
    getAll() {
      getFeedBack(this.currentpage, this.pageSize, this.search).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    detail(data) {
      this.dialogTableVisible = true
      this.UpData = data
    },
    clean() {
      this.search.feedback_id = ''
      this.search.user_id = ''
      this.getAll()
    }
  }
}
</script>

<style scoped>
.el-table>>>.cell {
  font-size: 15px;
  text-align: center;
}
</style>