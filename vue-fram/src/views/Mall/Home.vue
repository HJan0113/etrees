<template>
    <div style="width: 1200px;margin: 0 auto;">
        <!-- 顶栏 -->
        <el-row style="background-color: #cbf1ac;height: 75px;line-height: 75px;">

            <Head />
        </el-row>
        <!-- 轮播图 -->
        <el-row :gutter="10">
            <el-col :span="16" style="background-color: white">
                <el-carousel :interval="3000" style="margin:10px 0;width: 780px;border-radius: 10px;" height=250px>
                    <el-carousel-item>
                        <img class="pic" :src="img1" alt="">
                    </el-carousel-item>
                    <el-carousel-item>
                        <img class="pic" :src="img2" alt="">
                    </el-carousel-item>
                    <el-carousel-item>
                        <img class="pic" :src="img3" alt="">
                    </el-carousel-item>
                </el-carousel>
            </el-col>
            <!-- 个人中心 -->
            <el-col :span="8">
                <el-card style="margin-top: 10px;border-radius: 10px;height: 250px;">
                    <div slot="header" class="clearfix">
                        <span>个人中心</span>
                        <el-button style="float: right; padding: 3px 0" type="text" @click="logout"
                            v-if="isLogin">退出登录</el-button>
                    </div>
                    <div style="display: flex;flex-direction: column;">
                        <div style="text-align: center;">
                            <img style="width: 80px;height: 80px;border-radius: 50%;" :src="avatar" alt="">
                        </div>
                        <div style="display: flex;justify-content: space-around;" v-if="isLogin">
                            <div><el-button icon="el-icon-shopping-cart-2" type="text" style="font-size: 20px;"
                                    @click="toCar">购物车</el-button></div>
                            <div><el-button icon="el-icon-truck" type="text" style="font-size: 20px;"
                                    @click="toAddress">收货地址</el-button></div>
                        </div>
                        <div style="display: flex;justify-content: space-around;margin-top: 30px;" v-else>
                            <div><el-button type="text" style="font-size: 20px;" @click="login">立即登录</el-button></div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <!-- 商品栏 -->
        <el-row>
            <el-col :span="24">
                <!-- <transition name="el-zoom-in-top"> -->
                <router-view></router-view>
                <!-- </transition> -->
            </el-col>
        </el-row>
    </div>
</template>

<script>
import Head from '@/components/MallHead.vue';
import { getGoods, getGoodsDir, getGoodsDetail, addToCar, buyGoods, getCar, delCar, changeNum } from '@/api';
export default {
    components: {
        Head
    },
    data() {
        return {
            img1: 'https://iflytek-hjan.oss-cn-beijing.aliyuncs.com/lunbobo1.jpg',
            img2: 'https://iflytek-hjan.oss-cn-beijing.aliyuncs.com/lunbobo2.jpg',
            img3: 'https://iflytek-hjan.oss-cn-beijing.aliyuncs.com/lunbobo1.jpg',
            items: [],  //
        }
    },
    methods: {
        toCar() {
            if (this.$route.path !== '/mall/shopcar') {
                this.$router.push('/mall/shopcar')
            }

        },
        toAddress() {
            if (this.$route.path !== '/mall/address') {
                this.$router.push('/mall/address')
            }

        },
        login() {
            // 登录,跳转至登录页面,
            this.$router.push('/login')
        },
        logout() {
            // 提示
            this.$confirm('确定退出登录?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //清除本地存储的token
                localStorage.removeItem('token')
                // 将头像换成登录
                this.$store.dispatch("islogin")
                //切换到首页
                // this.$router.push("/")
                this.$store.state.avatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
                this.$message({
                    type: 'success',
                    message: '退出成功!'
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消退出'
                });
            });
        },
    },
    computed: {
        isLogin() {
            return this.$store.state.isLogin
        },
        avatar() {
            return this.$store.state.avatar
        },
    },
}
</script>

<style scoped>
.pic {
    width: 100%;
    height: 100%;
}

.el-table>>>.cell {
    font-size: 15px;
    text-align: center;
}

.box-card {
    flex: 1;
    overflow: auto;
    margin: 20px auto;
    width: 1083px;
    height: 100%;
    background-color: rgba(253, 246, 227, 0.6);
    border-radius: 30px;
}

div/deep/.el-card__body,
.el-main {
    padding-bottom: 0;
    padding-top: 40px;
}

div /deep/.el-tabs--left.el-tabs--border-card .el-tabs__item.is-left {
    height: 60px;
    text-align: center;
    width: 150px
}
</style>