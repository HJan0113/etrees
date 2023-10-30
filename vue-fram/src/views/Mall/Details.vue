<template>
    <div style="display: flex;">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <i class="el-icon-back" style="font-size: 23px;"></i>
                <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
            </div>
            <div style="display: flex;">
                <div style="flex: 1;text-align: center;">
                    <img style="width: 350px;height: 350px;" :src="table.imgUrl" alt="">
                </div>
                <div style="flex: 1;">
                    <div style="display: flex;flex-direction: column; justify-content: space-around;height: 350px;">
                        <div style="display: flex;justify-content: space-between;">
                            <div style="font-weight: 600;">
                                <div>{{ table.name }}</div>
                            </div>
                            <div style="">
                                <div><b>所属目录:</b> {{ table.dirName }}</div>
                            </div>
                        </div>
                        <div style="text-indent: 2em;">
                            <div>{{ table.introduction }}</div>
                        </div>
                        <div style="text-align: center;font-size: 25px;color: red;">
                            <div>售价: ￥{{ table.price }}</div>
                        </div>
                    </div>
                    <div style="display: flex;justify-content: space-evenly;">
                        <div>
                            <el-button type="primary" @click="addCar">加入购物车</el-button>
                        </div>
                        <div>
                            <el-button type="success" @click="buy">立即购买</el-button>
                        </div>
                    </div>
                </div>
            </div>

        </el-card>
    </div>
</template>

<script>
import { getGoodsDetail, addToCar } from '@/api';
export default {
    created() {
        const id = this.$route.params.id
        getGoodsDetail(id).then(res => {
            if (res.code === 1) {
                this.table = res.data
            }
        })
    },
    data() {
        return {
            table: {}
        }
    },
    methods: {
        //回退到商品页
        back() {
            this.$router.back()
        },
        //加入购物车
        addCar() {
            if (!this.isLogin) {
                this.$message.error("请先登录~")
                return
            }
            const data = {
                productId: this.table.id,
                productName: this.table.name,
                productPrice: this.table.price,
                productImg: this.table.imgUrl
            }
            addToCar(data).then(res => {
                this.$message({
                    message: res.code === 1 ? res.data : res.msg,
                    type: res.code === 1 ? 'success' : 'error'
                });
            })
        },
        //购买商品,跳转至购买页
        buy() {
            if(!this.isLogin) {
                this.$message.error("请先登录~")
                return
            }
            // const data = JSON.stringify(this.table)
            this.$router.push(`/mall/buy/${this.table.id}`)
        }
    },
    computed: {
        isLogin() {
            return this.$store.state.isLogin
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