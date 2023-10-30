<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <i class="el-icon-s-custom"></i>
        <span>查询员工</span>
      </div>
      <div class="item">
        <div class="search">
          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="员工编号">
              <el-input v-model="form.empName" placeholder="请输入员工编号" style="width: 300px;"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <div class="switch">
          <el-button class="button" @click="getl" type="primary">查询</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="box">
      <div slot="header" class="clearfix">
        <i class="el-icon-s-check"></i>
        <span>权限列表</span>
        <el-button style="margin-left: 90px;width: 90px;" @click="save">保存</el-button>
        <div class="descrip">操作对象: {{ empId }}</div>
      </div>
      <div class="content">
        <el-tree ref="tree" node-key="id" :data="data" show-checkbox 
           :props="defaultProps">
        </el-tree>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getPermission,upPermission } from '@/api'

export default {
  data() {
    return {
      form: {
        empName: ''
      },
      empId: '',
      // empLimits: [],
      data: [
        {
          id: 10,
          label: '首页',
        },
        {
          // id: 2,
          label: '用户管理',
          children: [{
            id: 21,
            label: '用户列表'
          }]
        },
        {
          // id: 4,
          label: '数据管理',
          children: [{
            id: 31,
            label: '收录列表'
          }, {
            id: 32,
            label: '识别记录'
          }]
        },
        {
          // id: 7,
          label: '员工管理',
          children: [{
            id: 41,
            label: '员工列表'
          }, {
            id: 42,
            label: '权限列表'
          }]
        },
        {
          // id: 10,
          label: '商城管理',
          children: [{
            id: 51,
            label: '目录管理'
          }, {
            id: 52,
            label: '商品管理'
          }, {
            id: 53,
            label: '订单管理'
          }]
        },
        {
          // id: 14,
          label: '平台维护',
          children: [{
            id: 61,
            label: '用户反馈'
          }, {
            id: 62,
            label: '日志管理'
          }]
        },
      ],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
    }
  },
  methods: {
    getl() {
      if (this.form.empName) {
        getPermission(this.form.empName).then(res => {
          if (res.code === 1) {
            this.empId = this.form.empName
            //取得员工的权限
            const data = JSON.parse(res.data)
            this.$refs.tree.setCheckedKeys(data);
          } else {
            this.$message.error(res.msg)
          }
          
        })
      }

    },
    save() {
      //保存权限的修改
      let keys = this.$refs.tree.getCheckedKeys()
      let ids = keys.filter(res=>{  
          return res!==undefined;
      })
      if(this.empId && ids) {
        upPermission(this.empId,ids).then(res => {
          if(res.code === 1) {
            this.$message.success(res.data)
          } else {
            this.$message.error(res.msg)
          }
        })
      }
      
    }
  },
  mounted() {
    //获取当前操作对象
    this.empId = this.$route.params.id
    if (this.empId) {
      getPermission(this.empId).then(res => {
        //取得员工的权限
        const data = JSON.parse(res.data)
        this.$refs.tree.setCheckedKeys(data);
      })
    }
    
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
</style>