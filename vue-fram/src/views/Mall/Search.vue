<template>
    <div class="container">
        <el-card class="box-card">
            <div slot="header" style="display: flex;justify-content: space-between;align-items: center;">
                <div>
                    <i class="el-icon-back" style="font-size: 23px;"></i>
                    <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
                </div>
                <div style="margin-right: 550px;font-size: large;font-weight: 600;">搜索结果</div>
            </div>
            <div v-if="!isEmpty">
                <el-row :gutter="20">
                    <el-col :span="6" v-for="(item, index) in list" :key="index" style="margin-top: 10px;">
                        <div @click="details(item.id)">
                            <div class="grid-content bg-purple">
                                <el-image style="width: 100%; height: 210px;" :src="item.imgUrl"></el-image>
                            </div>
                            <div style="text-align: center;">
                                {{ item.name }}
                            </div>
                            <div style="text-align: center;color: red;margin-top: 10px;">
                                售价: ￥{{ item.price }}
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <div v-else>
                <el-empty description="查无此商品"></el-empty>
            </div>
        </el-card>
    </div>
</template>

<script>
import { searchGoods } from '@/api';
export default {
    created() {
        this.info = this.searchData
        searchGoods(this.info).then(res => {
            if (res.code === 1) {
                if (res.data.length == 0) {
                    this.isEmpiy = true
                } else {
                    this.list = res.data
                    this.isEmpiy = false
                }
            }
        })
    },
    data() {
        return {
            list: [],
            info: '',
            isEmpiy: false
        }
    },
    methods: {
        details(id) {
            //跳转到商品详情页
            this.$router.push(`/mall/details/${id}`)
        },
        getAll() {

        },
        back() {
            this.$router.go(-1)
        }
    },
    computed: {
        isEmpty() {
            return this.info === ''
        },
        searchData() {
            return this.$store.state.searchGoods
        }
    },
    watch: {
        searchData(newValue, oldValue) {
            searchGoods(newValue).then(res => {
                if (res.code === 1) {
                    if (res.data.length === 0) {
                        this.$message.error("未找到该数据")
                        this.isEmpiy = true
                    } else {
                        this.list = res.data
                        this.isEmpiy = false
                    }

                }
            })
        }
    }
}
</script>

<style scoped>
.container {
    display: flex;
    height: 450px;
    flex-direction: column;
}

div /deep/.el-dialog__body {
    padding: 20px 20px 30px 20px;
}

.box-card::-webkit-scrollbar {
    width: 0;
    /* 隐藏滚动条 */
}

.box-card {
    flex: 1;
    overflow: auto;
    margin: 20px auto;
    width: 1200px;
    height: 100%;
    background-color: rgba(253, 246, 227, 0.6);
    border-radius: 30px;
}
</style>