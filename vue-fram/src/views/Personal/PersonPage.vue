<template>
    <div>
        <!-- 实名认证 -->
        <el-dialog title="实名认证" :visible.sync="dialogAuth" :before-close="handleClose" width="450px">
            <el-form :model="authform" :rules="authRules" ref="authform" style="margin-top: 40px;margin-left: 30px;">
                <el-form-item label="真实姓名" label-width="80px" prop="name">
                    <el-input style="width: 300px;" v-model="authform.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="身份证号" label-width="80px" prop="auth">
                    <el-input style="width: 300px;"  v-model="authform.auth" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" style="text-align: center;">
                <el-button style="width: 250px;" type="primary" @click="subAuth">确 定</el-button>
            </div>
        </el-dialog>
        <!-- 个人信息 -->
        <el-card>
            <el-descriptions class="margin-top" title="个人信息" :column="2" border>
                <template slot="extra">
                    <el-button v-show="!isAuth" type="success" size="small" @click="dialogAuth = true">实名认证</el-button>
                </template>
                <template slot="extra">
                    <el-button type="primary" size="small" @click="dialogVisible = true">修改信息</el-button>
                </template>

                <el-descriptions-item contentStyle="{height: 700px}">
                    <template slot="label">
                        <i class="el-icon-picture-outline"></i>
                        头像
                    </template>
                    <div style="margin-left: 40px; ">
                        <el-avatar :size="100"
                            :style="{ 'background-image': 'url(' + form.imgUrl + ')', 'background-size': 'cover' }"></el-avatar>
                    </div>
                </el-descriptions-item>
                <el-descriptions-item v-if="isAuth">
                    <template slot="label">
                        <i class="el-icon-picture-outline"></i>
                        实名状态
                    </template>
                    已实名
                </el-descriptions-item>
                <el-descriptions-item v-else>
                    <template slot="label">
                        <i class="el-icon-picture-outline"></i>
                        实名状态
                    </template>
                    未实名
                </el-descriptions-item>

                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-user"></i>
                        ID
                    </template>
                    {{ form.id }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-user"></i>
                        用户名
                    </template>
                    {{ form.userName }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-mobile-phone"></i>
                        手机号
                    </template>
                    {{ form.phone.substring(0, 3) + '****' + form.phone.substring(7) }}
                </el-descriptions-item>
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-date"></i>
                        年龄
                    </template>
                    {{ form.age === '' ? '暂未填写' : form.age }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-male"></i>
                        <i class="el-icon-female"></i>
                        性别
                    </template>
                    {{ form.gender === '' ? '暂未填写' : form.gender === 0 ? '女' : '男' }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-location-outline"></i>
                        家庭住址
                    </template>
                    {{ form.address === '' ? '暂未填写' : form.address }}
                </el-descriptions-item>
            </el-descriptions>
        </el-card>
        <!-- 账号安全 -->
        <el-card style="margin-top: 5px;">
            <div class="account">
                <span style="font-weight: 700;">账号安全</span>
                <div class="change">
                    <el-button type="text" icon="el-icon-edit-outline" @click="dialogPassword = true">修改密码</el-button>
                    <el-button type="text" icon="el-icon-edit-outline" @click="dialogPhone = true">换绑手机</el-button>
                </div>
            </div>
        </el-card>
        <!-- 修改个人信息 -->
        <el-dialog title="修改个人信息" :visible.sync="dialogVisible" :before-close="handleClose">
            <el-form :model="form" ref="form" label-width="150px">
                <div class="updateinfo">
                    <div class="left">
                        <div style="border-radius: 50%;">
                            <el-upload class="avatar-uploader" :action="avatarUrl" :show-file-list="false"
                                :on-success="handleSuccess" :before-upload="beforeAvatarUpload">
                                <img v-if="form.imgUrl" :src="form.imgUrl" class="avatar">
                                <i v-else style="background-color: red;align-self: center;" class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                        </div>
                        <div class="input">
                            <el-form-item label="年龄" prop="age">
                                <el-input v-model="form.age"></el-input>
                            </el-form-item>
                            <el-form-item label="性别" prop="gender">
                                <el-select v-model="form.gender" placeholder="请选择性别">
                                    <el-option label="男" :value="1"></el-option>
                                    <el-option label="女" :value="0"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="家庭住址" prop="address">
                                <el-input v-model="form.address"></el-input>
                            </el-form-item>
                        </div>
                    </div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="submit">提 交</el-button>
            </span>
        </el-dialog>
        <!-- 修改密码 -->
        <el-dialog title="修改密码" :visible.sync="dialogPassword" :before-close="handleClose">
            <el-form :model="passform" :rules="rulepass" ref="passform" label-width="150px">
                <div class="updateinfo">
                    <div class="right">
                        <div style="padding: 20px 180px 0 50px;">
                            <el-form-item label="原密码" prop="password">
                                <el-input type="password" v-model="passform.password"></el-input>
                            </el-form-item>
                            <el-form-item label="新密码" prop="newpass">
                                <el-input type="password" v-model="passform.newpass"></el-input>
                            </el-form-item>
                            <el-form-item label="新密码" prop="checkpass">
                                <el-input type="password" v-model="passform.checkpass"></el-input>
                            </el-form-item>
                        </div>
                    </div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="passSubmit">提 交</el-button>
            </span>
        </el-dialog>
        <!-- 换绑手机 -->
        <el-dialog title="换绑手机" :visible.sync="dialogPhone" :before-close="handleClose">
            <el-form :model="phoneform" :rules="rulephone" ref="phoneform" label-width="150px">
                <div class="updateinfo">
                    <div class="right">
                        <div style="padding: 20px 200px 0 30px;">
                            <el-form-item label="原手机号" prop="phone">
                                <el-input v-model="phoneform.phone"></el-input>
                            </el-form-item>
                            <el-form-item label="新手机号" prop="newphone">
                                <el-input v-model="phoneform.newphone"></el-input>
                            </el-form-item>
                            <el-form-item label="验证码" prop="check">
                                <div style="display: flex;">
                                    <el-input v-model="phoneform.check"></el-input>
                                    <el-button @click="postCheck">发送验证码</el-button>
                                </div>
                            </el-form-item>
                        </div>
                    </div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="phoneSubmit">提 交</el-button>
            </span>
        </el-dialog>
    </div>
</template>
  
<script>
import { getPersonMsg, postVerifi, changePerson, changePhone, changePwd ,auth} from '@/api';
import { serverIp, serverPort } from '../../../public/config'
import jwtDecode from 'jwt-decode'
export default {
    name: "Info",
    data() {
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入新密码'));
            } else {
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入新密码'));
            } else if (value !== this.passform.newpass) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        return {
            dialogVisible: false,
            dialogPassword: false,
            dialogPhone: false,
            dialogAuth: false,
            isVerifi: false, //是否发送验证码
            avatarUrl: `http://${serverIp}:${serverPort}/upload`,    //头像上传地址
            form: {
                imgUrl: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
                id: '',
                age: '',
                userName: '',
                phone: '',
                address: '',
                gender: '',
            },
            authform: {
                name: '',
                auth: ''
            },
            passform: {
                password: '',
                newpass: '',
                checkpass: ''
            },
            phoneform: {
                phone: '',
                newphone: '',
                check: ''
            },
            // 密码校验
            rulepass: {
                password: [
                    { required: true, message: '请输入旧手机号', trigger: 'blur' }
                ],
                checkpass: [
                    { validator: validatePass2, trigger: 'blur' }
                ],
                newpass: [
                    { validator: validatePass, trigger: 'blur' }
                ],
            },
            authRules: {
                name: [
                    { required: true, message: '请输入真实姓名', trigger: 'blur' }
                ],
                auth: [
                    { required: true, message: '请输入身份证号', trigger: 'blur' }
                ]
            },
            // 手机号校验
            rulephone: {
                phone: [
                    { required: true, message: '请输入原手机号', trigger: 'blur' },
                    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
                ],
                newphone: [
                    { required: true, message: '请输入新手机号', trigger: 'blur' },
                    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
                ],
                check: [
                    { required: true, message: '请输入验证码', trigger: 'blur' }
                ]
            }
        };
    },
    mounted() {
        this.load();
    },
    methods: {
        load() {
            //判断token是否过期,
            const token = localStorage.getItem('token')
            const decodedToken = jwtDecode(token)
            const currentTime = Date.now() / 1000
            if (decodedToken.exp < currentTime) {
                this.$message({
                    message: '登录已过期，请重新登录',
                    type: 'warning'
                })
                localStorage.clear('token')
                this.$router.push('/')
            } else {

            }
            // 获取用户基本信息
            getPersonMsg().then(res => {
                if (res.code === 0) {
                    this.$message.error("信息获取错误")
                    return
                }
                console.log("获取到的后端数据3434为: ", res)
                this.form.id = res.data.id
                this.form.age = res.data.age === null ? '' : res.data.age
                this.form.userName = res.data.userName
                this.form.phone = res.data.phone
                this.form.address = res.data.address === null ? '' : res.data.address
                this.form.gender = res.data.gender === null ? '' : res.data.gender
                this.form.imgUrl = res.data.imgUrl == null ? 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png' : res.data.imgUrl
                this.form.isRealname = res.data.isRealname
            })
        },
        // 当点击取消时,将弹窗关闭
        handleClose() {
            this.dialogVisible = false;
            this.dialogPassword = false;
            this.dialogPhone = false;
            this.dialogAuth = false;
            if (this.$refs.form !== undefined) {
                this.$refs.form.resetFields()
            }
            if (this.$refs.phoneform !== undefined) {
                this.$refs.phoneform.resetFields()
            }
            if (this.$refs.passform !== undefined) {
                this.$refs.passform.resetFields()
            } 
            if (this.$refs.authform !== undefined) {
                this.$refs.authform.resetFields()
            }
        },
        // 修改个人信息提交
        submit() {
            changePerson(this.form).then(res => {
                this.$message({
                    message: '修改成功',
                    type: 'success'
                })
                this.load()
                this.dialogVisible = false

            })
        },
        // 修改密码
        passSubmit() {
            // 数据校验
            this.$refs.passform.validate(valid => {
                if (valid) {
                    //校验通过,发送请求
                    changePwd(this.passform).then(res => {
                        console.log("修改了密码,后端返回了", res)
                        if (res.code === 1) {
                            this.$message({
                                message: res.data,
                                type: 'success'
                            })
                            //修改成功,关闭弹窗,清除校验
                            this.handleClose()
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
                }
                else {
                    // 校验失败,提示错误信息
                    this.$message.error("登录失败")
                }
            })
        },
        //提交认证信息
        subAuth() {
            this.$refs.authform.validate(valid =>{
                if(valid) {
                    auth().then(res => {
                        if(res.code === 1) {
                            this.$message.success(res.data)
                            this.dialogAuth = false
                            this.$store.dispatch('auth')
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
                }
            })
        },
        // 修改手机号
        phoneSubmit() {
            this.$refs.phoneform.validate(valid => {
                if (!this.isVerifi) {
                    this.$message.error("请先发送验证码")
                    return
                }
                if (valid) {
                    //校验通过,发送请求,
                    changePhone(this.phoneform).then(res => {
                        if (res.code === 1) {
                            this.$message({
                                message: res.data,
                                type: 'success'
                            })
                            //修改成功,关闭弹窗,清除校验
                            this.handleClose()
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
                }
                else {
                    // 校验失败,提示错误信息
                    this.$message.error("手机号修改失败")
                }
            })
        },
        postCheck() {
            //校验手机号
            this.$refs.phoneform.validateField('phone', (valid) => {
                if (valid === '') {
                    const phone = {
                        phone: this.phoneform.newphone
                    }
                    postVerifi(phone).then(res => {
                        this.isVerifi = true    //表示已经发过短信
                        if (res.code === 1) {
                            this.$message({
                                message: res.data,
                                type: 'success'
                            })
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
                }
                else {
                    console.log("校验失败", valid)
                }
            })
        },
        // 当上传头像成功时,将后端返回的图片url赋值给imgUrl
        handleSuccess(res) {
            this.form.imgUrl = res.data;
            this.$store.dispatch('avatar', res.data)
        },
        beforeAvatarUpload(file) {
            console.log("文件类型是:", file)
            const isJPG = file.type === 'image/jpeg';
            const isPNG = file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG && !isPNG) {
                this.$message.error('上传头像图片只能是 JPG或者PNG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG || isPNG && isLt2M;
        }
    },
    computed: {
        isAuth() {
            return this.$store.state.auth
        }
    }
};
</script>
  
<style scoped>
.img {
    width: 150px;
}

.left {
    display: flex;
    justify-content: center;
}

.input {
    padding-top: 20px;
    padding-right: 30px;
}

.avatar-uploader {
    display: flex;
    justify-content: center;
    height: 200px;
    

}

.avatar {
    max-width: 300px;
    height: 100%;
}

.account {
    height: 50px;
}

.change {
    display: flex;
    justify-content: space-evenly
}

div>>>.el-dialog__body {
    padding: 0 0;
}

div>>>.el-dialog {
    position: absolute;
    top: 10%;
    left: 50%;
    transform: translate(-50%);
}
</style>
