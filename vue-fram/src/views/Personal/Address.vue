<template>
  <div class="container">
    <el-dialog title="收货地址" :visible.sync="dialogFormVisible">
      <el-form :model="form" ref="form" :rules="rules">
        <el-form-item label="姓名" label-width="80px" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="手机号" label-width="80px" prop="phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址" label-width="80px" prop="address">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </div>
    </el-dialog>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>收货地址</span>
        <el-button style="float: right;" type="primary" size="mini" @click="add">添加</el-button>
      </div>
      <div v-for="(item, index) in tableData" :key="index">
        <div style="display: flex;justify-content: space-between;width: 80%;padding-left: 30px;">
          <div style="display: flex;flex-direction: column;justify-content: space-around;height: 50px;width: 300px;">
            <div>{{ item.name }}</div>
            <div>{{ item.address }}</div>
          </div>
          <div style="align-self: center;width: 200px;">
            {{ item.phone }}
          </div>
          <div style="align-self: center;">
            <el-button type="text" size="mini" @click="del(item.id)">删除</el-button>
          </div>
        </div>
        <el-divider></el-divider>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getAddress, addAdd,delAdd } from '@/api'
export default {
  created() {
    this.getAll()
  },
  data() {
    return {
      dialogFormVisible: false,
      tableData: [],
      form: {
        name: '',
        phone: '',
        address: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入收件人姓名', trigger: 'blur' },
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入收货地址', trigger: 'blur' }
        ],

      }
    }
  },
  methods: {
    getAll() {
      getAddress().then(res => {
        if (res.code === 1) {
          this.tableData = res.data
        }
      })
    },
    add() {
      this.dialogFormVisible = true

    },
    del(id) {
      this.$confirm('确定删除该收货信息?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delAdd(id).then(res => {
          if (res.code === 1) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          } else {
            this.$message({
              type: 'error',
              message: '删除失败~'
            });
          }
        }).finally(()=>{this.getAll()})
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          addAdd(this.form).then(res => {
            if (res.code === 1) {
              this.$message.success(res.data)
              this.dialogFormVisible = false
            } else {
              this.$message.error(res.msg)
            }
          }).finally(() => { this.getAll() })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }
  },
  watch: {
    dialogFormVisible: {
      handler(newValue) {
        if (newValue === false) {
          this.$refs.form.resetFields()

        }
      }
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