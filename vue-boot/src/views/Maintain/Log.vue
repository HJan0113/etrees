<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <span>员工日志</span>
      </div>
      <div>
        <el-form ref="form" :model="form" label-width="140px" :inline="true" style="display: flex;">
          <el-form-item label="员工编号">
            <el-input v-model="form.id" placeholder="请输入内容"></el-input>
          </el-form-item>
          <div style="margin-left: 30px;">
            <el-date-picker v-model="form.date" value-format="yyyy/MM/dd hh:mm:ss" type="daterange" align="right"
              unlink-panels range-separator="到" start-placeholder="开始日期" end-placeholder="结束日期"
              :picker-options="pickerOptions">
            </el-date-picker>
          </div>
          <el-form-item>
            <el-button type="primary" style="margin-left: 70px;" @click="getAll">查询</el-button>
            <el-button @click="overload">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div style="margin-top: 40px;">
        <div class="content">
          <el-table :data="empData" :header-cell-style="{ background: '#eef1f6', color: '#606266', height: '40px' }">
            <el-table-column prop="action" label="操作类型" width="270">
            </el-table-column>
            <el-table-column prop="empId" label="员工编号" width="300">
            </el-table-column>
            <el-table-column prop="status" label="操作状态" width="500">
            </el-table-column>
            <el-table-column prop="createTime" label="操作时间" :formatter="dateFormat">
            </el-table-column>
          </el-table>
          <div style="padding: 10px 0">
            <el-pagination @size-change="empSizeChange" @current-change="empCurrentChange" :current-page="currentpage"
              :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes,prev,pager,next,jumper"
              :total="total">
            </el-pagination>
          </div>
        </div>
      </div>
    </el-card>


  </div>
</template>

<script>
import { getLog } from '@/api';
export default {
  data() {
    return {

      empData: [],  //员工数据列表

      currentpage: 0, //当前页数
      pageSize: 5,  //当前页记录数
      total: 0, //总记录数

      form: {//搜索条件
        id: '',
        date: ''
      },

      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
    }
  },
  created() {
    this.getAll()
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
    getAll() {
      const form = {
        id: this.form.id,
        start: this.form.date === '' ? '' : this.form.date[0],
        end: this.form.date === '' ? '' : this.form.date[1]
      }
      getLog(this.currentpage, this.pageSize, form).then(res => {
        this.empData = res.data.records,
          this.total = res.data.total
      })
    },
    overload() {
      this.form.id = '',
        this.form.date = ''
      this.getAll()
    },
    //在切换记录数时将pageSize属性修改为切换的页码记录数
    empSizeChange(val) {
      this.pageSize = val;
      this.getAll()
    },
    //在切换当前页码数时,将currentpage修改为切换的页码数
    empCurrentChange(val) {
      this.currentpage = val;
      this.getAll()
    },
  }
}
</script>

<style scoped>
div /deep/.el-select .el-input__inner {
  width: 100px;
}

.el-table>>>.cell {
  font-size: 15px;
  text-align: center;
}

div/deep/.el-input--mini .el-input__inner {
  width: 110px;
  height: 35px;
}

div /deep/.el-range-editor--mini.el-input__inner {
  height: 40px;
}

div /deep/.el-range-editor--mini .el-range__close-icon,
.el-range-editor--mini .el-range__icon {
  line-height: 32px;
}

div /deep/.el-range-editor--mini .el-range-separator {
  line-height: 32px;
}

div /deep/.el-form-item--mini .el-form-item__content,
.el-form-item--mini .el-form-item__label {
  line-height: 40px;
}

.el-form-item--mini .el-form-item__content,
.el-form-item--mini>>>.el-form-item__label {
  line-height: 40px;
}</style>