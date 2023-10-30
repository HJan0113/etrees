<template>
    <div style="overflow: hidden;">
        <el-col :span="9">
            <div class="text">
                <img style="width: 200px;height: 40px;margin-top: 20px;" src="../assets//logo.png" alt="">
            </div>
        </el-col>
        <el-col :span="9">
            <!-- <div class="input">
                <el-input class="input-content" placeholder="请输入内容" prefix-icon="el-icon-search" v-model="info">
                </el-input>
                <el-button class="butt" size="small"  @click="search">搜索</el-button>
            </div> -->
            <div class="search-box">
                <input type="text" placeholder="请输入内容" />
                <input type="submit" style="background-color: #cbf1ac;" value="搜索" @click="search"/>
            </div>
        </el-col>

        <el-col :span="6">
            <div class="home">
                <el-button style="border-radius: 20px;background-color: #cbf1ac;font-size: 17px;" type="text"
                    icon="el-icon-s-home" @click="goHome">ETREES首页</el-button>
            </div>
        </el-col>
    </div>
</template>

<script>
export default {
    data() {
        return {
            info: ''
        }

    },
    computed: {
        isLogin() {
            return this.$store.state.isLogin
        },
        avatar() {
            return this.$store.state.avatar
        }
    },
    methods: {
        goHome() {
            this.$router.push('/')
        },
        search() {
            if (this.info === '') {
                this.$message.error("请输入您要搜索的商品")
                return
            }
            this.$store.dispatch('searchgoods', this.info)
            if (this.$route.path !== '/mall/search') {
                this.$router.push('/mall/search')
            }

        }
    }
}
</script>

<style scoped>
.search-box {
    width: 400px;
    height: 40px;
    margin: 17px auto;
    background-color: #f2f2f2;
    border-radius: 20px;
    padding: 5px;
    display: flex;
    align-items: center;
}

.search-box input[type="text"] {
    flex-grow: 1;
    height: 30px;
    border: none;
    outline: none;
    font-size: 14px;
    padding-left: 10px;
    background-color: transparent;
}

.search-box input[type="submit"] {
    width: 60px;
    height: 30px;
    border: none;
    outline: none;
    background-color: #ff3300;
    color: #fff;
    font-size: 14px;
    border-radius: 15px;
    cursor: pointer;
}

.text {
    display: flex;
    margin-left: 30px;
    color: white;
    /* font-size: 20px; */
}

.input /deep/.el-input__inner {
    border-radius: 30px;
}

.avatar {
    display: flex;
    margin-left: 30px;
    height: 75px;
}

.person {
    padding-top: 15px;
    box-sizing: border-box;
}

.home {
    text-align: right;
    margin-right: 30px;
}

.input {
    display: flex;
    position: relative;
    text-align: right;
    /* top: ; */
}

.input-content {
    position: absolute;
    top: 0;
    right: 0;
}

.butt {
    position: absolute;
    top: 30px;
    right: 0px;
    margin-right: -5px;
    width: 70px;
    border-radius: 20px;
    background-color: #cbf1ac;
    color: white;


}
</style>