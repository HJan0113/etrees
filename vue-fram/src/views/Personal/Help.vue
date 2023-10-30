<template>
    <div>
        <el-card>
            <div style="height: 400px;display: flex;flex-direction: column;justify-content: space-evenly;">
                <div style="display: flex;justify-content: space-evenly;align-content: space-evenly;">
                    <el-card style="text-align: center;" @click.native="toLogin">
                        <div >
                            <i style="font-size: 50px;" class="el-icon-user"></i>
                        </div>
                        <div style="margin-top: 10px;">注册与登录</div>
                    </el-card>
                    <el-card style="text-align: center;" @click.native="toSecurity">
                        <div>
                            <i style="font-size: 50px;" class="el-icon-lock"></i>
                        </div>
                        <div style="margin-top: 10px;">账号与安全</div>
                    </el-card>
                    <el-card style="text-align: center;"  @click.native="toAuth">
                        <div>
                            <i style="font-size: 50px;" class="el-icon-full-screen"></i>
                        </div>
                        <div style="margin-top: 10px;">实名认证</div>
                    </el-card>
                </div>
                <div style="display: flex;justify-content: space-evenly;">
                    <el-card style="text-align: center;"  @click.native="toTips">
                        <div>
                            <i style="font-size: 50px;" class="el-icon-collection"></i>
                        </div>
                        <div style="margin-top: 10px;">健康提示与图鉴</div>
                    </el-card>
                    <el-card style="text-align: center;"  @click.native="toPay">
                        <div>
                            <i style="font-size: 50px;" class="el-icon-wallet"></i>
                        </div>
                        <div style="margin-top: 10px;">商城与支付</div>
                    </el-card>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
export default {
    methods: {
        toLogin() {
            this.$router.push('/person/login')
        },
        toAuth() {
            this.$router.push('/person/auth')
        },
        toPay() {
            this.$router.push('/person/pay')
        },
        toSecurity() {
            this.$router.push('/person/security')
        },
        toTips() {
            this.$router.push('/person/tips')
        },

    }
}
</script>

<style></style>