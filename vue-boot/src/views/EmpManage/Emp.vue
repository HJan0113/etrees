<template>
  <div>
    <!-- 增加员工弹框 -->
    <el-dialog title="添加员工" :visible.sync="dialogFormVisible" width="30%" :before-close="handleClose">
      <el-form ref="form" :rules="rules" :model="form" label-width="80px" :inline="true" style="text-align: center;">
        <el-form-item label="登入账号" prop="empName">
          <el-input v-model="form.empName"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="saveData">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 编辑员工弹框 -->
    <el-dialog title="修改信息" :visible.sync="dialogFormMsg" width="30%" :before-close="handleClose">
      <el-form ref="empform" :rules="rules" :model="empform" label-width="80px" :inline="true"
        style="text-align: center;">
        <el-form-item label="登入账号" prop="empName">
          <el-input v-model="empform.empName"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="name">
          <el-input v-model="empform.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="empform.phone"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="update">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 搜索框-->
    <div>
      <el-input style="width: 200px;margin: 0;height: 0;" placeholder="请输入名称" suffix-icon="el-icon-search"
        v-model="search.empName" @keyup.enter.native="getAll"></el-input>
      <el-input style="width: 200px" placeholder="请输入ID" suffix-icon="el-icon-search" v-model="search.ID"
        @keyup.enter.native="getAll"></el-input>
      <el-button class="ml-5" type="primary" @click="getAll">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="clearAll" style="margin-left: 0;">重置</el-button>
    </div>

    <!--   增加和删除功能 -->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="dialogFormVisible = true">新增<i
          class="el-icon-circle-plus-outline"></i></el-button>
      <el-button type="danger" @click="deleteBatch">批量删除<i class="el-icon-circle-remove-outline"></i></el-button>
    </div>

    <el-table :data="tableData" stripe @selection-change="handleSelectionChange"
      :header-cell-style="{ background: '#eef1f6', color: '#606266' }">
      <el-table-column type="selection" v-model="selectedIds" width="55">
      </el-table-column>
      <el-table-column prop="id" label="员工编号" width="140">
      </el-table-column>
      <el-table-column prop="empName" label="登录账号" width="120">
      </el-table-column>
      <el-table-column prop="name" label="真实姓名">
      </el-table-column>
      <el-table-column prop="phone" label="手机号">
      </el-table-column>
      <el-table-column prop="status" label="员工状态">
      </el-table-column>
      <el-table-column label="分配权限">
        <template v-slot="score">
          <el-button @click="setLimits(score.row.id)">设置</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="score">
          <el-button type="text" @click="updateD(score.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-button type="text" @click="deleteD(score.row)">删除<i class="el-icon-remove-outline"></i></el-button>
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
import { getAllEmp, deleteData, updateData, save, removeAll } from "@/api";

export default {
  data() {
    return {
      tableData: [],  //要获取展示的数据
      selectedIds: [],  //被选中的数据的Id
      dialogFormVisible: false, //添加员工弹窗
      dialogFormMsg: false,   //编辑员工弹窗
      //搜索框数据
      search: {
        empName: '',
        ID: ''
      },
      currentpage: 0, //当前页数
      pageSize: 10,  //当前页记录数
      total: 0, //总记录数
      form: {
        empName: '',
        name: '',
        phone: '',
      },
      empform: {
        empName: '',
        name: '',
        phone: '',
      },
      rules: {
        empName: [
          { required: true, message: '请输入员工账号', trigger: 'blur' },
        ],
        name: [
          { required: true, message: '请输入员工真实姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入员工手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],

      }
    }
  },
  created() {
    this.getAll();
  },
  methods: {
    //获取全部分页数据
    getAll() {
      getAllEmp(this.currentpage, this.pageSize, this.search).then((res) => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    //清空搜索框
    clearAll() {
      this.search.empName = '',
        this.search.ID = '',
        this.getAll()
    },
    //处理多选框选项
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.id)
    },
    //批量删除
    deleteBatch() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请选择需要删除的数据！')
        return
      }
      this.$confirm('确定要删除选中的数据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //调用后台接口进行删除操作
        console.log("列表数据为: ", this.selectedIds)
        removeAll(this.selectedIds).then(res => {
          if (res.code === 1) {
            this.$message.success(res.data)
            // 删除成功后，重新加载数据
            this.getAll()
          } else {
            this.$message.error(res.msg)
          }
        }).catch(error => {
          this.$message.error('删除失败：' + error.message)
        }).finally(()=>{this.getAll()})
      }).catch(() => {
        // 取消删除操作
      })
    },
    //增加数据
    saveData() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          save(this.form).then((res) => {
            if (res.code === 1) {
              this.$message.success("新增成功")
              this.dialogFormVisible = false
            } else {
              this.$message.error(res.msg)
            }
          }).finally(() => { this.getAll() })
        } else {
          // console.log('error submit!!');
          return false;
        }
      });

    },
    //修改数据
    update() {
      updateData(this.empform).then((res) => {
        if (res.code === 1) {
          this.$message.success("修改成功")
          this.dialogFormMsg = false
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAll() })
    },
    //删除数据
    deleteD(data) {
      this.$confirm("你确定要删除此条数据吗? ", "警告", {
        confirmButtonText: '确定', cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteData(data.id).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        }).finally(() => { this.getAll() })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    //修改数据时,将数据回显
    updateD(data) {
      this.dialogFormMsg = true;
      this.empform = data
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
      // console.log
      this.dialogFormVisible = false
      this.dialogFormMsg = false
      if (this.$refs.form !== undefined) {
        this.$refs.form.resetFields()
      }
      if (this.$refs.empform !== undefined) {
        // this.$refs.empform.resetFields()
        this.empform = {}
      }
    },
    //跳转到权限页面,并携带id
    setLimits(id) {
      this.$router.push({ name: 'emplimits', params: { id: id } })
    }
  },

  //数据监听,当dialogFormVisible变为false时,将form 里的数据清除
  // watch: {
  //   dialogFormVisible: {
  //     handler(newValue) {
  //       if (newValue === false) {
  //         this.$refs.form.resetFields()
  //         this.form = {}
  //       }
  //     }
  //   },
  //   dialogFormMsg: {
  //     handler(newValue) {
  //       if (newValue === false) {
  //         this.$refs.form.resetFields()
  //         this.form = {}
  //       }
  //     }
  //   }
  // }

}
</script>

<style scoped></style>
