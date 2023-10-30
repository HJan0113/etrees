<template>
    <div style="display: flex;">
        <el-card class="box-card">
            <div slot="header" style="display: flex;justify-content: space-between;align-items: center;">
                <div>
                    <i class="el-icon-back" style="font-size: 23px;"></i>
                    <el-button type="text" style="font-size: 20px;" @click="backCar">返回</el-button>
                </div>
                <div style="width: 200px;"><b>当前总价:</b>￥{{ totalPrice }}</div>
                <div style="display: flex;justify-content: space-between;width: 150px;">
                    <div>
                        <el-button type="danger" size="mini" @click="del">删除</el-button>
                    </div>
                    <div>
                        <el-button type="success" size="mini" @click="submit">结算</el-button>
                    </div>
                </div>
                <el-checkbox style="text-align: right;" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
            </div>
            <div style="">
                <el-table :data="items" style="width: 100%" height="350">
                    <el-table-column width="55">
                        <template slot-scope="scope">
                            <el-checkbox v-model="scope.row.selected" @change="selectItem(scope.row)"></el-checkbox>
                        </template>
                    </el-table-column>
                    <el-table-column label="商品图片">
                        <template slot-scope="scope">
                            <el-popover placement="top-start" title="" trigger="hover">
                                <img :src="scope.row.productImg" alt="" style="width: 150px;height: 150px">
                                <img slot="reference" :src="scope.row.productImg" style="width: 30px;height: 30px">
                            </el-popover>
                        </template>
                    </el-table-column>
                    <el-table-column prop="productName" label="商品名称">
                    </el-table-column>
                    <el-table-column prop="productPrice" label="价格"></el-table-column>
                    <el-table-column prop="quantity" label="数量">
                        <template scope="scope">
                            <el-input-number @change="changenum(scope.row.id, scope.row.quantity)"
                                v-model="scope.row.quantity" size="mini" :min="1"></el-input-number>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-card>
    </div>
</template>

<script>
import { delCar, getCar, changeNum } from '@/api';
export default {
    created() {
        this.getAllCarData()
    },
    data() {
        return {
            checkAll: false,
            items: []
        }
    },
    methods: {
        backCar() {
            this.$router.push('/mall/goods')
        },
        del() {
            this.$confirm('确定删除选中的数据?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                const ids = this.items.filter(res => {
                    return res.selected
                }).map(res => {
                    return res.id
                })
                console.log("检查数据是否有误: ", ids)
                delCar(ids).then(res => {
                    if (res.code === 1) {
                        this.$message.success(res.data)
                    } else {
                        this.$message.error(res.msg)
                    }
                }).finally(() => { this.getAllCarData() })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消退出'
                });
            });
        },
        handleCheckAllChange(val) {
            if (val) {
                this.items.forEach(res => {
                    res.selected = true
                })
            } else {
                this.items.forEach(res => {
                    res.selected = false
                })
            }
            // this.checkedCities = val ? cityOptions : [];
            // this.isIndeterminate = false;
        },
        selectItem(item) {
            this.items.forEach((i) => {
                if (i === item) {
                    i.selected = item.selected;
                }
            });
            const checked = this.items.filter(res => {
                return res.selected
            })
            if (checked.length === this.items.length) {
                this.checkAll = true
            } else {
                this.checkAll = false
            }
        },
        changenum(id, num) {
            changeNum(id, num)
        },
        getAllCarData() {
            getCar().then(res => {
                if (res.code === 1) {
                    const list = res.data
                    list.forEach(res => {
                        res.selected = false
                    })
                    this.items = list
                }
            })
        },
        submit() {
            const ids = this.items.filter(res => {
                return res.selected
            }).map(res => {
                return res.id
            })
            this.$store.dispatch('shopcarids',ids)
            this.$router.push('/mall/buycar')
        }
    },
    computed: {
        totalPrice() {
            let price = 0
            const list = this.items.filter(res => {
                return res.selected
            })
            list.forEach(res => {
                price += res.productPrice * res.quantity
            })
            if (price === 0) {
                return 0
            }
            return price
        }
    }
}
</script>

<style scoped>
.box-card {
    flex: 1;
    overflow: auto;
    margin: 20px auto;
    width: 1083px;
    height: 100%;
    background-color: rgba(253, 246, 227, 0.6);
    border-radius: 30px;
}
</style>