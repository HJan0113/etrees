<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <i class="el-icon-circle-plus-outline"></i>
        <span>查询记录</span>
      </div>
      <div style="display: flex;margin-left: 240px;">
        <div class="search">
          <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="查询">
              <el-input v-model="form.search" placeholder="请输入用户编号" style="width: 300px;"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <div style="margin-left: 30px;">
          <el-date-picker v-model="form.date" value-format="yyyy/MM/dd hh:mm:ss" type="daterange" align="right"
            unlink-panels range-separator="到" start-placeholder="开始日期" end-placeholder="结束日期"
            :picker-options="pickerOptions">
          </el-date-picker>
        </div>
        <div style="margin-left: 100px;margin-top: 7px;">
          <el-button class="button" type="primary" @click="getAll">查询</el-button>
        </div>
      </div>
    </el-card>
    <el-card class="box">
      <div slot="header" class="clearfix">
        <i class="el-icon-menu"></i>
        <span>识别记录</span>
      </div>
      <div class="content">
        <el-table :data="records" :header-cell-style="{background:'#eef1f6',color:'#606266'}">
          <el-table-column prop="id" label="记录编号">
          </el-table-column>
          <el-table-column prop="userId" label="用户">
            <template slot-scope="scope">
              <template v-if="scope.row.userId === 888">
                游客
              </template>
              <template v-else>
                {{ scope.row.userId }}
              </template>
            </template>
          </el-table-column>
          <el-table-column prop="equipment" label="设备">
          </el-table-column>
          <el-table-column prop="createTime" label="查询时间" :formatter="dateFormat">
          </el-table-column>
        </el-table>
      </div>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentpage"
        :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes,prev,pager,next,jumper"
        :total="this.total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import { getRecords } from '@/api';
export default {
  data() {
    return {
      form: {
        search: '',
        date: ''
      },
      currentpage: 0, //当前页数
      pageSize: 5,  //当前页记录数
      total: 0, //总记录数
      records: [],
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
    dateFormat (row, column, cellValue, index) {
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
    addTip() {
      console.log("选择的日期是: ", this.form.date)
    },
    getAll() {
      const form = {
        userId: this.form.search,
        start: this.form.date === '' ? '' : this.form.date[0],
        end: this.form.date === '' ? '' : this.form.date[1]
      }
      getRecords(this.currentpage,this.pageSize,form).then(res => {
        this.records = res.data.records,
        this.total = res.data.total
      })
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
  },
  computed: {
   
  }
}
</script>

<style scoped>
/* div/deep/.el-table th.el-table__cell>.cell {
  text-align: center;
} */
.el-table>>>.cell {
  font-size: 15px;
  text-align: center;
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

div /deep/.el-form-item--mini .el-form-item__label {
  line-height: 40px;
}

div /deep/.el-form-item--mini .el-form-item__content,
.el-form-item--mini .el-form-item__label {
  line-height: 40px;
}

div /deep/.el-input--mini .el-input__inner {
  height: 40px;
}</style>