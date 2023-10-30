<template>
  <div>
    <!--搜索框-->
    <div style="margin-bottom: 20px;">
      <el-input style="width: 200px;margin: 0;height:auto" placeholder="请输入名称" suffix-icon="el-icon-search"
        v-model="search.userName" @keyup.enter.native="getAll"></el-input>
      <el-input style="width: 200px" placeholder="请输入ID" suffix-icon="el-icon-search" v-model="search.id"
        @keyup.enter.native="getAll"></el-input>
      <el-button class="ml-5" type="primary" @click="getAll">查询</el-button>
      <el-button class="ml-5" type="warning" @click="clearAll" style="margin-left: 0;">重置</el-button>
    </div>
    <el-table :data="tableData" stripe @selection-change="handleSelectionChange"
      :header-cell-style="{ background: '#eef1f6', color: '#606266' }">
      <el-table-column prop="id" label="用户编号" width="140">
      </el-table-column>
      <el-table-column prop="userName" label="用户名" width="120">
      </el-table-column>
      <el-table-column prop="phone" label="手机号">
      </el-table-column>
      <el-table-column prop="gender" label="性别">
        <template v-slot="scope">
          {{ scope.row.gender === null ? '暂未填写' : scope.row.gender === 0 ? '女' : '男' }}
        </template>
      </el-table-column>
      <el-table-column prop="age" label="年龄">
        <template v-slot="scope">
          {{ scope.row.age === null ? '暂未填写' : scope.row.age }}
        </template>
      </el-table-column>
      <el-table-column prop="address" label="地址">
        <template v-slot="scope">
          {{ scope.row.address === null ? '暂未填写' : scope.row.address }}
        </template>
      </el-table-column>
      <el-table-column prop="registerTime" label="注册时间" :formatter="dateFormat">
      </el-table-column>
      <el-table-column prop="status" label="账户状态">
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="score">
          <el-button v-if="score.row.status" type="text" @click="ban(score.row.id)">禁用</el-button>
          <el-button v-else type="text" @click="ban(score.row.id)">启用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--        分页组件-->
    <div style="padding: 10px 0">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage"
        :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes,prev,pager,next,jumper"
        :total="this.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getAllUser, banUser } from "@/api";

export default {
  data() {
    return {
      tableData: [],  //要获取展示的数据
      selectedIds: [],  //被选中的数据的Id
      dialogFormVisible: false,
      //搜索框数据
      search: {
        userName: '',
        id: ''
      },
      currentpage: 0, //当前页数
      pageSize: 10,  //当前页记录数
      total: 0, //总记录数
    }
  },
  created() {
    this.getAll();
  },
  methods: {
    dateFormat(row, column, cellValue, index) {
      const daterc = row[column.property]
      console.log(row, column)
      if (daterc) {
        if (daterc.indexOf('T') === -1) return daterc
        const arr = daterc.split('T')
        const d = arr[0]
        const darr = d.split('-')
        const t = arr[1]
        const tarr = t.split('.000')
        const marr = tarr[0].split(':')
        const dd = darr[0] + '-' + darr[1] + '-' + darr[2] + ' ' + marr[0] + ':' + marr[1] + ':' + marr[2].substring(0, 2)
        return dd
      }
    },
      //获取全部用户分页数据
      getAll() {
  getAllUser(this.currentpage, this.pageSize, this.search).then((res) => {
    console.log("后端返回的分页查询数据为: ", res)
    this.tableData = res.data.records
    this.total = res.data.total
  })
},
//清空搜索框
clearAll() {
  this.search.userName = '',
    this.search.id = '',
    this.getAll()
},
//处理多选框选项
handleSelectionChange(selection) {
  this.selectedIds = selection.map(item => item.id)
},
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
//在关闭弹窗时
handleClose() {
  this.dialogFormVisible = false
},
//禁用用户
ban(id) {
  this.$confirm("你确定要进行此操作吗? ", "警告", {
    confirmButtonText: '确定', cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    banUser(id).then(res => {
      if (res.code === 1) {
        this.$message.success(res.data)
      }
      else {
        this.$message.error(res.msg)
      }
    }).finally(() => { this.getAll() })
  }).catch(() => {
    this.$message({
      type: 'info',
      message: '已取消操作'
    })
  })

}
  },

//数据监听,当dialogFormVisible变为false时,将form 里的数据清除
watch: {
  dialogFormVisible: {
    handler(newValue) {
      if (newValue === false) {
        this.$refs.form.resetFields()
        this.form = {}
      }
    }
  }
}

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/* .el-table>>>.cell {
  font-size: 15px;
  text-align: center;
} */
</style>
