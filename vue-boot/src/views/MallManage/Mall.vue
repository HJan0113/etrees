<template>
  <div>
    <el-dialog title="商品信息" :visible.sync="dialogFormVisible" :before-close="handleClose" width="1000px">
      <div style="display: flex;">
        <div class="des">
          <el-form :model="upGoods" ref="goods" label-width="100px" class="demo-goods">
            <div style="display: flex;">
              <div style="flex: 1;height: 300px;text-align: center;line-height: 300px;">
                <el-upload class="avatar-uploader" :action="action" :show-file-list="false"
                  :on-success="handleSuccessByUp" :before-upload="beforeAvatarUpload">
                  <img style="width: 300px;height: 300px;margin-left: 100px;" v-if="upGoods.imgUrl" :src="upGoods.imgUrl"
                    class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </div>
              <div style="flex: 1;">
                <el-form-item label="所属目录" prop="dirId">
                  <el-select v-model="upGoods.dirId" placeholder="请选择">
                    <el-option :label="item.name" :value="item.id" v-for="(item, index) in goodsIndex"
                      :key="index"></el-option>
                  </el-select>
                </el-form-item>

                <el-form-item label="商品名称" prop="name">
                  <el-input v-model="upGoods.name"></el-input>
                </el-form-item>

                <el-form-item label="商品介绍" prop="introduction">
                  <el-input type="textarea" v-model="upGoods.introduction"></el-input>
                </el-form-item>

                <el-form-item label="商品定价" prop="price">
                  <el-input v-model.number="upGoods.price"></el-input>
                </el-form-item>

                <el-form-item label="商品状态" prop="status">
                  <el-switch v-model="upGoods.status" :active-value="1" :inactive-value="0"></el-switch>
                </el-form-item>

                <el-form-item>
                  <el-button style="margin-left: -70px;width: 100px;" type="primary" @click="save">保存</el-button>
                </el-form-item>
              </div>
            </div>
          </el-form>
        </div>
      </div>
    </el-dialog>
    <el-tabs value="first">
      <el-tab-pane label="商品管理" name="first">
        <div>
          <el-form :inline="true" ref="form" :model="search" class="demo-form-inline">
            <el-form-item label="查询条件">
              <el-input v-model="search.id" placeholder="请输入商品编号"></el-input>
            </el-form-item>
            <el-form-item>
              <el-input v-model="search.name" placeholder="请输入商品名称"></el-input>
            </el-form-item>

            <el-button style="margin-left: 60px;" type="primary" @click="getAll">查询</el-button>
            <el-button style="margin-left: 30px;" type="primary" @click="clean">重置</el-button>
          </el-form>
        </div>
        <div class="content">
          <el-table :data="tableData" style="width: 100%;">
            <el-table-column prop="id" label="商品编号" width="180">
            </el-table-column>
            <el-table-column prop="name" label="商品名称" width="180">
            </el-table-column>
            <el-table-column prop="price" label="商品定价">
            </el-table-column>
            <el-table-column prop="dirName" label="所属目录">
            </el-table-column>
            <el-table-column prop="status" label="商品状态">
            </el-table-column>
            <el-table-column label="操作">
              <template v-slot="score">
                <el-button type="text" @click="update(score.row)">编辑<i class="el-icon-edit"></i></el-button>
                <el-button type="text" @click="del(score.row.id)">删除<i class="el-icon-remove-outline"></i></el-button>
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
      </el-tab-pane>

      <el-tab-pane label="添加商品" name="second">
        <div style="display: flex;">
          <div class="des">
            <el-form :model="goods" ref="goods" label-width="100px" class="demo-goods">
              <div style="display: flex;">
                <div style="flex: 1;height: 300px;text-align: center;line-height: 300px;">
                  <el-upload list-type="picture-card" class="avatar-uploader" :action="action" :show-file-list="false" :on-success="handleSuccess"
                    :before-upload="beforeAvatarUpload">
                    <img style="width: 250px;height: 250px;" v-if="goods.imgUrl" :src="goods.imgUrl"
                      class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
                </div>
                <div style="flex: 2;">
                  <el-form-item label="所属目录" prop="dirId">
                    <el-select v-model="goods.dirId" placeholder="请选择">
                      <el-option :label="item.name" :value="item.id" v-for="(item, index) in goodsIndex"
                        :key="index"></el-option>
                    </el-select>
                    <el-select v-model="goods.pushKey" placeholder="适用病害">
                      <el-option :label="item" :value="item" v-for="(item, index) in pushKey"
                        :key="index"></el-option>
                    </el-select>
                  </el-form-item>

                  <el-form-item label="商品名称" prop="name">
                    <el-input v-model="goods.name"></el-input>
                  </el-form-item>

                  <el-form-item label="商品介绍" prop="introduction">
                    <el-input type="textarea" v-model="goods.introduction"></el-input>
                  </el-form-item>

                  <el-form-item label="商品定价" prop="price">
                    <el-input v-model.number="goods.price"></el-input>
                  </el-form-item>

                  <el-form-item label="商品状态" prop="status">
                    <el-switch style="margin-left: 60px;" v-model="goods.status" active-value="1"
                      inactive-value="0"></el-switch>
                  </el-form-item>

                  <el-form-item>
                    <el-button style="margin-left: -70px;width: 100px;" type="primary" @click="add">确定添加</el-button>
                  </el-form-item>
                </div>
              </div>
            </el-form>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import { getIndex, addGoods, updateGoods, delGood,getGoods } from '@/api';
import { serverPort, serverIp } from '@/../public/config';
export default {
  data() {
    return {
      currentpage: 0, //当前页数
      pageSize: 5,  //当前页记录数
      total: 0, //总记录数
      dialogFormVisible: false,
      action: `http://${serverIp}:${serverPort}/upload`, //商品图片上传地址
      imgUrl: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', //商品图片url
      pushKey: ['番茄叶斑病','苹果黑星病','葡萄黑腐病','白菜霉斑病','白菜虫洞','玉米大叶斑病','玉米小叶斑病','番茄黄叶病','番茄卷曲病毒','草莓白粉病','番茄粉末状霉菌','番茄晚疫病水霉菌','苹果锈病','番茄早疫病','番茄叶斑真菌','番茄叶霉病','番茄蜘蛛螨伤害','苹果棕斑病','玉米锈病'],
      goodsIndex: [],
      goods: { //添加的商品
        imgUrl: '',
        name: '',
        introduction: '',
        price: '',
        status: '',
        imgUrl: '',
        dirId: '',
        pushKey: ''
      },
      upGoods: {
        imgUrl: '',
        name: '',
        introduction: '',
        price: '',
        status: '',
        dirId: ''
      },
      search: {//查询条件
        id: '',
        name: ''
      },
      tableData: []
    }
  },
  created() {
    this.getAll()
  },
  computed: {
    
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
    clean() {
      this.search.id = '',
        this.search.name = '',
        this.getAll()
    },
    getAll() {
      getIndex(this.currentpage, this.pageSize, this.search).then((res) => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
      getGoods().then(res => {
        this.goodsIndex = res.data
      })
    },
    add() {
      addGoods(this.goods).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.$refs.goods.resetFields()
          this.goods.imgUrl = ''
          this.goods.pushKey = ''
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAll() })
    },
    save() {
      updateGoods(this.upGoods).then(res => {
        if (res.code === 1) {
          this.$message.success(res.data)
          this.$refs.goods.resetFields()
          this.upGoods.imgUrl = ''
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      }).finally(() => { this.getAll() })
    },
    del(id) {
      this.$confirm('确定删除该商品吗, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delGood(id).then(res => {
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
    update(data) {
      this.dialogFormVisible = true
      this.upGoods = data
    },
    handleClose() {
      this.dialogFormVisible = false
      this.getAll()

    },
    //文件上传成功
    handleSuccess(res) {
      console.log("文件上传成功", res)
      this.goods.imgUrl = res.data
    },
    handleSuccessByUp(res) {
      this.upGoods.imgUrl = res.data
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
  }
}
</script>

<style scoped>
.search-box {
  display: flex;
  align-items: center;
  height: 50px;
  padding: 0 10px;
  margin-left: 20px;
}

.el-select /deep/.el-input__inner {
  height: 40px;
}

.el-input--mini>>>.el-input__inner {
  height: 41px;
  /* margin-left: 60px; */
  width: 300px
}
div /deep/.el-upload--picture-card {
  width: 250px;
  height: 250px;
  line-height: 250px;
}
div /deep/.el-textarea__inner {
  /* margin-left: 60px; */
  width: 300px;
}

.el-form-item--mini /deep/ .el-form-item__label {
  line-height: 3;
}

.search-select {
  width: 150px;
  margin-right: 10px;
}

.search-input {
  flex: 1;
  margin-right: 10px;
  width: 150px;
}

.el-button--mini {
  height: 35px;
}

.demo-form-inline {
  margin-top: 30px;
  margin-left: 50px;
}

.content {
  margin-top: 30px;
}

div /deep/.el-tabs__nav-scroll {
  margin-left: 20px;
}

.pic {
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;
}

.des {
  flex: 1;
  width: 100px;
  margin-top: 50px;
}

.el-table>>>.cell {
  font-size: 15px;
  text-align: center;
}
</style>
