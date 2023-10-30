<template>
    <el-card class="box-card">
        <div slot="header" style="display: flex;justify-content: space-between;align-items: center;">
            <div>
                <i class="el-icon-back" style="font-size: 23px;"></i>
                <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
            </div>
            <div style="margin: 0 auto;font-weight: 600;">收货地址</div>
        </div>
        <div style="">
            <el-table :data="tableData" style="width: 100%" height="350">
                <el-table-column width="300" prop="name" label="用户名">
                    <template slot-scope="scope">
                        <el-radio v-model="checked" :label="scope.row.id">{{ scope.row.name }}</el-radio>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="name" label="用户名">
                </el-table-column> -->
                <el-table-column prop="phone" label="手机号">
                </el-table-column>
                <el-table-column prop="address" label="收货地址">
                </el-table-column>
            </el-table>
            <el-button type="text" style="font-size: 20px;" @click="sure">确定选择</el-button>
        </div>
    </el-card>
</template>

<script>
import { getAddress } from '@/api';
export default {
    created() {
        this.getAll()
    },
    data() {
        return {
            checked: '',
            checkAll: true,
            tableData: []
        }
    },
    methods: {
        back() {
            this.$router.back()
        },
        sure() {
            this.$store.dispatch('selectadd',this.checked)
            this.$router.go(-1)
        },
        getAll() {
            getAddress().then(res => {
                if (res.code === 1) {
                    this.tableData = res.data
                }
            })
        },
        submit() {
            this.$router.push('/mall/buycar')
        }
    },
}
</script>

<style scoped>
.el-table>>>.cell {
    font-size: 15px;
    text-align: left;
}
</style>