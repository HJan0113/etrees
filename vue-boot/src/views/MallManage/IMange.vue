<template>
  <div>
    <el-dialog title="目录名称" :visible.sync="dialogFormVisible" width="300px">
      <el-form :model="indexForm">
        <div style="text-align: center;">
          <el-upload class="avatar-uploader" :action="upload" :show-file-list="false" :on-success="UpdateSuccess"
            :before-upload="beforeAvatarUpload">
            <img style="width: 60px;height: 60px;border-radius: 50%;margin-top: -9px;" v-if="indexForm.imgUrl"
              :src="indexForm.imgUrl">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="目录名称">
          <el-input v-model="indexForm.Indexname" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save">保 存</el-button>
      </div>
    </el-dialog>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <i class="el-icon-circle-plus-outline"></i>
        <span>添加目录</span>
      </div>
      <div class="item">
        <div>
          <el-form ref="form" :rules="rule" inline :model="form" label-width="80px" style="margin-left: 200px;">
            <el-form-item label="目录图标" prop="imgUrl" style="width: 200px;height: 50px;">
              <el-upload  class="avatar-uploader" :action="upload" :show-file-list="false" :on-success="handleSuccess"
                :before-upload="beforeAvatarUpload">
                <img style="width: 60px;height: 60px;border-radius: 50%;margin-top: -9px;" v-if="form.imgUrl"
                  :src="form.imgUrl">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item style="margin-left: 40px;" label="目录名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入目录名称" style="width: 300px;"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <div class="switch">
          <el-button class="button" type="primary" @click="add">确认添加</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="box">
      <div slot="header" class="clearfix">
        <i class="el-icon-menu"></i>
        <span>目录列表</span>
      </div>
      <div class="content">
        <el-table ref="filterTable" :data="tableData">
          <el-table-column prop="name" label="目录名称">
          </el-table-column>
          <el-table-column label="操作">
            <template v-slot="score">
              <el-button style="font-size: 15px;" type="text" @click="update(score.row)">编辑</el-button>
              <el-button style="font-size: 15px;" type="text" @click="delData(score.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getGoods, delGoods, addIndex, updataGoods } from '@/api';
import { serverIp, serverPort } from '@/../public/config';
export default {
  data() {
    return {
      dialogFormVisible: false,
      upload: `http://${serverIp}:${serverPort}/upload`,
      form: {
        name: '',
        imgUrl: ''
      },
      
      indexForm: {
        id: '',
        Indexname: '',
        imgUrl: ''
      },
      empId: '',
      tableData: [],
      rule: {
        name: [
          { required: true, message: '请输入目录名称', trigger: 'blur' },
        ],
      }
    }
  },
  methods: {
    getAll() {
      getGoods().then(res => {
        this.tableData = res.data
        this.$store.dispatch('getgoods', res.data)
      })
    },
    delData(id) {
      this.$confirm('删除此目录将导致此目录下的所有商品被删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delGoods(id).then(res => {
          if (res.code === 1) {
            this.$message.success(res.data)
          } else {
            this.$message.error(res.msg)
          }
        }).finally(() => { this.getAll() })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    add() {
      addIndex(this.form).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.$refs.form.resetFields()
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAll() })
    },
    //修改目录
    save() {
      const formData = {
        name: this.indexForm.Indexname,
        id: this.indexForm.id,
        imgUrl: this.indexForm.imgUrl
      }
      updataGoods(formData).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.dialogFormVisible = false
          this.indexForm.name = ''
          this.indexForm.imgUrl = ''
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAll() })
    },

    update(data) {
      this.dialogFormVisible = true,
      this.indexForm.Indexname = data.name
      this.indexForm.id = data.id
      this.indexForm.imgUrl = data.imgUrl
    },
    //添加商品的文件上传
    handleSuccess(res) {
      this.form.imgUrl = res.data;
    },
    UpdateSuccess(res) {
      this.indexForm.imgUrl = res.data
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
  },
  mounted() {
    this.getAll()
  },
  computed: {

  }
}
</script>

<style scoped>
.clearfix {
  margin-left: 50px;
}

.cont {
  display: flex;
  margin-left: 20px;
}

.descrip {
  position: absolute;
  right: 170px;
  top: 250px;
}

div>>>.el-card__header {
  padding: 18px 0 3px 10px;
}

.el-input--mini>>>.el-input__inner {
  height: 40px;
}

.el-form-item--mini .el-form-item__content,
.el-form-item--mini>>>.el-form-item__label {
  line-height: 45px;
}

.search {
  margin-left: 200px;
  height: 20px;
}

.item {
  display: flex;
}

.switch {
  margin-left: 170px;
  padding-top: 7px;
}

.button {
  width: 100px;
  height: 30px;
}

.box {
  margin-top: 10px;
  /* height: 460px; */

}

.el-table>>>.cell {
  font-size: 15px;
  text-align: center;
}

.el-table>>>th.el-table__cell>.cell {
  font-size: 18px;
}

div/deep/.el-upload {
  margin-top: 9px;
}
</style>