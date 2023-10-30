<template>
  <div>
    <el-dialog title="编辑" :visible.sync="dialogFormVisible" :before-close="handleClose" width="300px">
      <el-form ref="indexform" :model="form">
        <el-form-item label="目录图标" label-width="80px" prop="imgUrl">
          <el-upload class="avatar-uploader" :action="upload" :show-file-list="false" :on-success="handleUpdate"
            :before-upload="beforeAvatarUpload">
            <img style="width: 100px;height: 100px;" v-if="this.form.imgUrl" :src="this.form.imgUrl">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="目录名称" label-width="80px" prop="indexName">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="提示信息" :visible.sync="dialogFormTip" :before-close="handleClose">
      <el-form ref="indexform" :model="Tipform">
        <el-form-item label="提示信息" label-width="80px" prop="indexName">
          <el-input type="textarea" v-model="Tipform.text" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveTip">保 存</el-button>
      </div>
    </el-dialog>

    <el-dialog title="信息详细" :visible.sync="dialogFormHandBook" width="1300px">
      <div style="display: flex;">
        <div style="width: 400px;height: 300px;display: flex;justify-content: center;align-content: center;">
          <el-upload class="avatar-uploader" :action="upload" :show-file-list="false" :on-success="handleHandBook"
            :before-upload="beforeAvatarUpload">
            <img style="width: 300px;height: 300px;" v-if="HandBookForm.imgUrl" :src="HandBookForm.imgUrl">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <div class="right">
          <!-- 上面三个一行的框 -->
          <div>
            <el-form :inline="true" :model="HandBookForm" class="demo-form-inline" label-width="80px">
              <el-form-item label="目录名称">
                <el-input v-model="HandBookForm.dirName"></el-input>
              </el-form-item>
              <el-form-item label="信息名称">
                <el-input v-model="HandBookForm.name"></el-input>
              </el-form-item>
              <el-form-item label="病发季节">
                <el-input v-model="HandBookForm.season"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <!-- 下面三列的框 -->
          <div>
            <el-form :model="HandBookForm" class="demo-form-inline" label-width="80px">
              <el-form-item label="病害描述">
                <el-input type="textarea" placeholder="请输入病害概述" v-model="HandBookForm.overview"></el-input>
              </el-form-item>
              <el-form-item label="病发原因">
                <el-input type="textarea" placeholder="请输入病发原因" v-model="HandBookForm.reason"></el-input>
              </el-form-item>
              <el-form-item label="治疗方案">
                <el-input type="textarea" placeholder="请输入治疗方案" v-model="HandBookForm.treatment"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div style="margin-left: 400px;">
            <el-button type="primary" style="width: 100px;" @click="updateHandBook">保存</el-button>
          </div>
        </div>

      </div>
    </el-dialog>

    <el-tabs value="first">
      <el-tab-pane label="图鉴目录" name="first">
        <div>
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <i class="el-icon-circle-plus-outline"></i>
              <span>添加目录</span>
            </div>
            <div class="item">
              <div class="search">
                <el-form ref="addform" :rules="rule" inline :model="addform" label-width="80px">
                  <el-form-item label="目录图标" style="margin-right: 140px;" prop="imgUrl">
                    <el-upload  class="avatar-uploader" :action="upload" :show-file-list="false"
                      :on-success="handleSuccess" :before-upload="beforeAvatarUpload">
                      <img style="width: 60px;height: 60px;margin-top: -10px;" v-if="addform.imgUrl"
                        :src="addform.imgUrl">
                      <i v-else style="border: 1px #c0c4cc dashed;" class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                  </el-form-item>

                  <el-form-item label="目录名称" prop="name">
                    <el-input v-model="addform.name" placeholder="请输入目录名称" style=""></el-input>
                  </el-form-item>
                </el-form>
              </div>
              <div class="switch">
                <el-button class="button" type="primary" @click="addIndex">确认添加</el-button>
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
                <el-table-column label="操作" style="margin-left: -30px !important;">
                  <template v-slot="score">
                    <el-button style="font-size: 15px;" type="text" @click="up(score.row)">编辑</el-button>
                    <el-button style="font-size: 15px;" type="text" @click="ban(score.row.id)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-card>
        </div>
      </el-tab-pane>

      <el-tab-pane label="图鉴信息" name="second">
        <div style="display: flex;height: 250px;">
          <div style="flex: 1;align-self: center;">
            <el-upload list-type="picture-card" class="avatar-uploader" style="text-align: center;" :action="upload" :show-file-list="false" :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="formInline.imageUrl" :src="formInline.imageUrl" class="avatar">
              <i v-else  class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <!-- <el-upload list-type="picture-card" :auto-upload="false" class="avatar-uploader" style="text-align: center;" :action="upload" :show-file-list="false" :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <i slot="default" class="el-icon-plus"></i>
              <div slot="file" >
                <img v-if="formInline.imageUrl" :src="formInline.imageUrl" class="avatar">
              </div>
            </el-upload> -->
          </div>
          <div style="flex: 3;">
            <!-- 上面三个一行的框 -->
            <div>
              <el-form :inline="true" :model="formInline" class="demo-form-inline" label-width="80px">
                <el-form-item label="目录名称">
                  <el-select v-model="formInline.index" placeholder="请选择">
                    <el-option :label="item.name" :value="item.id" v-for="(item, index) in tableData"
                      :key="index"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="信息名称">
                  <el-input v-model="formInline.name" placeholder="请输入信息名称"></el-input>
                </el-form-item>
                <el-form-item label="病发季节">
                  <el-input v-model="formInline.season" placeholder="请输入内容"></el-input>
                </el-form-item>
              </el-form>
            </div>
            <!-- 下面三列的框 -->
            <div>
              <el-form :model="formInline" class="demo-form-inline" label-width="80px">
                <el-form-item label="病害描述">
                  <el-input type="textarea" placeholder="请输入病害概述" v-model="formInline.desc"></el-input>
                </el-form-item>
                <el-form-item label="病发原因">
                  <el-input type="textarea" placeholder="请输入病发原因" v-model="formInline.reason"></el-input>
                </el-form-item>
                <el-form-item label="治疗方案">
                  <el-input type="textarea" placeholder="请输入治疗方案" v-model="formInline.project"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </div>
          <div style="align-self: center;margin-left: 30px;">
            <el-button type="primary" @click="saveHandBook">确定添加</el-button>
          </div>
        </div>
        <div class="content">
          <el-table :data="handData" style="width: 100%">
            <el-table-column prop="id" label="信息编号" width="280">
            </el-table-column>
            <el-table-column prop="dirName" label="所属目录" width="180">
            </el-table-column>
            <el-table-column prop="name" label="信息名称">
            </el-table-column>
            <el-table-column prop="handle" label="操作">
              <template v-slot="score">
                <el-button style="font-size: 15px;" type="text" @click="detailed(score.row)">详细</el-button>
                <el-button style="font-size: 15px;" type="text" @click="Dele(score.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!--        分页组件-->
        <div style="padding: 10px 0">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage"
            :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes,prev,pager,next,jumper"
            :total="this.total">
          </el-pagination>
        </div>
      </el-tab-pane>

      <el-tab-pane label="健康提示" name="third">
        <div>
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <i class="el-icon-circle-plus-outline"></i>
              <span>添加提示</span>
            </div>
            <div class="item">
              <div class="search">
                <el-form ref="form" :model="healthTip" label-width="80px">
                  <el-form-item label="提示内容">
                    <el-input v-model="healthTip.context" placeholder="请输入提示内容(15字以内)" style="width: 300px;"></el-input>
                  </el-form-item>
                </el-form>
              </div>
              <div class="switch">
                <el-button class="button" type="primary" @click="addTip">确认添加</el-button>
              </div>
            </div>
          </el-card>
          <el-card class="box">
            <div slot="header" class="clearfix">
              <i class="el-icon-menu"></i>
              <span>提示列表</span>
            </div>
            <div class="content">
              <el-table ref="filterTable" :data="Tips">
                <el-table-column prop="text" label="提示内容">
                </el-table-column>
                <el-table-column label="操作" style="margin-left: -30px !important;">
                  <template v-slot="score">
                    <el-button style="font-size: 15px;" type="text" @click="setTip(score.row)">编辑</el-button>
                    <el-button style="font-size: 15px;" type="text" @click="delTip(score.row.id)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import { add_index, getAllIndex, updateIndex, deleteIndex, getHandbookLimit, addHandBook, setOrCan, deleteHandBook, updateHandInfo, getAllTips, addTips, updataTip, delTips } from '@/api';
import { serverPort, serverIp } from '@/../public/config';
export default {
  created() {
    this.getAll()
    this.getAllHandbook()
    this.getTips()
  },
  methods: {
    //在切换记录数时将pageSize属性修改为切换的页码记录数
    handleSizeChange(val) {
      this.pageSize = val;
      this.getAllHandbook()
    },
    //在切换当前页码数时,将currentpage修改为切换的页码数
    handleCurrentChange(val) {
      this.currentpage = val;
      this.getAllHandbook()
    },
    //图鉴信息的文件上传
    handleAvatarSuccess(res) {
      this.formInline.imageUrl = res.data;
    },
    //添加商品的文件上传
    handleSuccess(res) {
      this.addform.imgUrl = res.data;
    },

    handleHandBook(res) {
      this.HandBookForm.imgUrl = res.data
    },
    //编辑商品的文件上传
    handleUpdate(res) {
      //将传入的img信息存入临时的变量
      this.form.imgUrl = res.data;
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    },

    //获取全部目录信息
    getAll() {
      //发送请求,获取全部数据
      getAllIndex().then(res => {
        console.log("请求成功", res)
        this.tableData = res.data
      })
    },
    //获取全部图鉴信息
    getAllHandbook() {
      getHandbookLimit(this.currentpage, this.pageSize).then((res) => {
        this.handData = res.data.records
        this.total = res.data.total
      })
    },
    //获取全部提示信息
    getTips() {
      getAllTips().then(res => {
        if (res.code === 1) {
          this.Tips = res.data
        }
      })
    },
    //添加目录
    addIndex() {
      //表单校验
      this.$refs.addform.validate(valid => {
        if (valid) {
          console.log("校验通过", valid)
          add_index(this.addform).then(res => {
            if (res.code === 1) {
              this.$message.success(res.data)
              this.$refs.addform.resetFields()
            } else {
              this.$message.error(res.msg)
            }
          }).finally(() => { this.getAll() })
        }
        else {
          console.log("校验失败")
        }
      })
    },

    //添加图鉴
    saveHandBook() {
      const formData = {
        dirId: this.formInline.index + '',
        name: this.formInline.name,
        overview: this.formInline.desc,
        season: this.formInline.season,
        treatment: this.formInline.project,
        imgUrl: this.formInline.imageUrl,
        reason: this.formInline.reason
      }
      addHandBook(formData).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.formInline.imageUrl = '',
            this.formInline.index = '',
            this.formInline.name = '',
            this.formInline.season = '',
            this.formInline.desc = '',
            this.formInline.reason = '',
            this.formInline.project = ''
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAllHandbook() })
    },
    //添加提示信息
    addTip() {
      const tip = { text: this.healthTip.context }
      addTips(tip).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.healthTip.context = ''
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getTips() })
    },
    //修改提示信息
    saveTip() {
      updataTip(this.Tipform).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.dialogFormTip = false
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getTips() })
    },
    //编辑目录
    up(data) {
      this.dialogFormVisible = true
      this.form.imgUrl = data.imgUrl
      this.form.name = data.name
      this.form.id = data.id
    },
    //编辑图鉴
    detailed(data) {
      this.dialogFormHandBook = true,
        this.HandBookForm = data
    },
    //编辑提示
    setTip(data) {
      this.dialogFormTip = true,
        this.Tipform = data
    },

    //删除目录
    ban(id) {
      //发送delete请求,携带id
      this.$confirm('确定要删除该目录吗, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteIndex(id).then(res => {
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
    //删除图鉴
    Dele(id) {
      this.$confirm('确定要删除该图鉴信息吗, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteHandBook(id).then(res => {
          if (res.code === 1) {
            this.$message.success(res.data)
          } else {
            this.$message.error(res.msg)
          }
        }).finally(() => { this.getAllHandbook() })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //删除提示
    delTip(id) {
      this.$confirm('确定要删除该提示信息吗, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delTips(id).then(res => {
          if (res.code === 1) {
            this.$message.success(res.data)
          } else {
            this.$message.error(res.msg)
          }
        }).finally(() => { this.getTips() })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },

    //设为/取消常见
    setCommon(id) {
      setOrCan(id).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAllHandbook() })
    },

    //取消修改
    handleClose() {
      this.dialogFormVisible = false
      this.dialogFormHandBook = false
      this.dialogFormTip = false
      this.getAll()
      this.getAllHandbook()
      this.getTips()
      // if (this.$refs.indexform !== undefined) {
      //   this.$refs.indexform.resetFields()
      // } 
    },
    //保存修改
    save() {
      //发送请求,保存修改的数据
      console.log("数据保存成功")

      //发送请求
      updateIndex(this.form).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAll() })
    },
    //修改图鉴
    updateHandBook() {
      updateHandInfo(this.HandBookForm).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.dialogFormHandBook = false
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAllHandbook() })
    }

  },
  data() {
    return {
      currentpage: 0, //当前页数
      pageSize: 5,  //当前页记录数
      total: 0, //总记录数
      upload: `http://${serverIp}:${serverPort}/upload`,  //头像上传地址
      dialogFormVisible: false,   //目录编辑弹窗
      dialogFormHandBook: false,  //图鉴编辑弹窗
      dialogFormTip: false,  //提示编辑弹窗
      //图鉴目录搜索框数据
      seach: '',
      //编辑数据
      form: {
        id: '',
        name: '',
        imgUrl: ''
      },
      //添加数据
      addform: {
        name: '',
        imgUrl: ''
      },
      //添加健康提示
      healthTip: {
        context: ''
      },
      //健康提示展示数据
      Tips: [],
      //图鉴目录表格数据
      tableData: [],
      //图鉴信息表格数据
      handData: [],
      //编辑提示信息
      Tipform: {
        id: '',
        text: ''
      },

      // 添加信息form表单数据
      formInline: {
        imageUrl: '', //图鉴头像
        index: '',  //所属目录
        name: '',   //名称
        season: '', //多发季节
        desc: '',   //病害概述
        reason: '', //病发原因
        project: '', //治疗方案
      },
      //编辑图鉴信息
      HandBookForm: {
        imgUrl: '',
        dirName: '',
        name: '',
        season: '',
        reason: '',
        overview: '',
        treatment: '',
      },
      rule: {
        name: [
          { required: true, message: '请输入目录名称', trigger: 'blur' },
        ],
      }
    }
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  /* width: 178px;
  height: 178px; */
  /* line-height: 44px; */
  text-align: center;
  /* align-self: center; */
  margin-top: 10px;

}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  
}

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

div /deep/.el-input--mini .el-input__inner {
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

div /deep/.el-table__empty-text {
  margin-right: 140px;
}
</style>
