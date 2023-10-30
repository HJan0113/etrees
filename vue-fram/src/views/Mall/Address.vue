<template>
    <div class="container">
        <div class="body">
            <el-card class="box-card">
                <div slot="header" style="display: flex;justify-content: space-between;align-items: center;">
                    <div>
                        <i class="el-icon-back" style="font-size: 23px;"></i>
                        <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
                    </div>
                    <div style="margin-right: 550px;font-weight: 600;">收货地址</div>
                </div>
                <div v-if="!isEmpty">
                    <div v-for="(item, index) in tableData" :key="index">
                        <div style="display: flex;justify-content: space-between;width: 100%;padding-left: 30px;">
                            <div
                                style="display: flex;flex-direction: column;justify-content: space-around;height: 50px;flex: 6;">
                                <div>{{ item.name }}</div>
                                <div style="margin-top: 10px;">{{ item.address }}</div>
                            </div>
                            <div style="align-self: center;flex: 3;">
                                {{ item.phone }}
                            </div>
                            <div style="align-self: center;flex: 1;">
                                <el-button type="text" size="mini" @click="del(item.id)">删除</el-button>
                            </div>
                        </div>
                        <el-divider></el-divider>
                    </div>
                </div>
                <div v-else>
                    <el-empty description="收货地址为空"></el-empty>
                </div>
            </el-card>
        </div>
    </div>
</template>
  
<script>
import { getAddress, addAdd, delAdd } from '@/api'
export default {
    created() {
        this.getAll()
    },
    data() {
        return {
            tableData: [],
            form: {},
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
        // add() {
        //     this.dialogFormVisible = true

        // },
        back() {
            this.$router.push('/mall/goods')
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
                }).finally(() => { this.getAll() })
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
    // watch: {
    //     dialogFormVisible: {
    //         handler(newValue) {
    //             if (newValue === false) {
    //                 this.$refs.form.resetFields()

    //             }
    //         }
    //     }
    // },
    computed: {
        isEmpty() {
            return this.tableData.length === 0
        }
    }
}
</script>
  
<style scoped>
.container {
    display: flex;
    height: 350px;
    flex-direction: column;
}
.body {
    display: flex;
    overflow: auto;
}
.box-card::-webkit-scrollbar {
    width: 0;
    /* 隐藏滚动条 */
}
.body::-webkit-scrollbar {
    width: 0;
}
.box-card {
    flex: 1;
    overflow: auto;
    margin: 20px auto;
    width: 1083px;
    height: 90%;
    background-color: rgba(253, 246, 227, 0.6);
    border-radius: 30px;
}
</style>