<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>个人中心</span>
        </div>
        <el-container style="height: 500px; border: 1px solid #eee">
            <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
                <el-menu :default-openeds="['1', '2']" :default-active="activePage" @select="selectPage" router>
                    <el-submenu index="1">
                        <template slot="title"><i class="el-icon-user"></i>个人中心</template>
                        <el-menu-item index="/person/personPage">个人信息</el-menu-item>
                        <el-menu-item index="/person/Records">历史记录</el-menu-item>
                        <el-menu-item index="/person/Orders">我的订单</el-menu-item>
                        <el-menu-item index="/person/Address">收货信息</el-menu-item>
                    </el-submenu>
                    <el-submenu index="2">
                        <template slot="title"><i class="el-icon-edit-outline"></i>帮助与反馈</template>
                        <el-menu-item index="/person/help">帮助</el-menu-item>
                        <el-menu-item index="/person/feedback">反馈</el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>
            <el-container>
                <el-main style="overflow: hidden;padding-top: 15px;">
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </el-card>
</template>

<script>
export default {
    data() {
        return {
            form: {
                name: '',
                phone: '',
                id: '',
            },
            // index: '/person/personPage',
            show: true,
            imageUrl: '',   //头像回显
        }
    },
    methods: {
        // 上传图片成功
        handleAvatarSuccess(res, file) {
            this.imageUrl = URL.createObjectURL(file.raw);
        },
        //   校验上传图片
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        },
        selectPage(index) {
            this.$store.dispatch('setactive',index)
            // this.index = index
        }
    },
    computed: {
        activePage() {
            return this.$store.state.activeMeau
        }
    }

}

</script>

<style scoped>
.box-card {
    margin: 0 auto;
    width: 80%;
    height: 100%;
    /* background-color: pink; */
    border-radius: 20px;
}

.clearfix {
    text-align: center;
}

.button {
    display: flex;
    text-align: center;
    justify-content: space-around;
}

.content {
    height: 100%;
    display: flex;
}

.cont {
    flex: 1;
    height: 100%;
    margin-top: 30px;
}

.img {
    flex: 1;
    text-align: center;

}

.img1 {
    width: 450px;
    height: 420px;
    border-radius: 20px;
}

.formItem {
    text-align: center;
    width: 80%;
}

.el-input>>>.el-input__inner {
    height: 40px;
}

.avatar-uploader {
    height: 100px;
    width: 100px;
    margin: 0 auto;
    margin-bottom: 20px;
    background-color: orange;
    border-radius: 50%;
    text-align: center;
    line-height: 100px;
    box-sizing: border-box;
}
</style>