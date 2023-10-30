<template>
  <el-card class="container">
    <!-- 员工资料 -->
    <el-card style="margin-top: 10px;" shadow="never">
      <div slot="header" class="clearfix">
        <div>
          <i class="el-icon-user-solid"></i>
          <span>员工资料</span>
        </div>
      </div>
      <div class="text">
        <el-descriptions :column="1" size="medium">
          <el-descriptions-item label="员工编号">{{ form.id }}</el-descriptions-item>
          <el-descriptions-item label="员工账号">{{ form.empName }}</el-descriptions-item>
          <el-descriptions-item label="真实姓名">{{ form.name }}</el-descriptions-item>
          <el-descriptions-item label="手机号码">{{ form.phone }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <!-- 安全管理 -->
    <el-card class="security" shadow="never">
      <div slot="header" class="clearfix">
        <div>
          <i class="el-icon-lock"></i>
          <span>安全管理</span>
        </div>
      </div>
      <div class="button">
        <el-button type="text" icon="el-icon-edit-outline" @click="dialogPassword = true">修改密码</el-button>
        <el-button type="text" icon="el-icon-edit-outline" @click="dialogPhone = true">修改手机</el-button>
      </div>
    </el-card>

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
              <el-form-item label="确定新密码" prop="checkpass">
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
  </el-card>
</template>

<script>
import {postVerifi,getInfo} from '@/api'
export default {
  created() {
    getInfo().then(res => {
      if (res.code === 1) {
        this.form = res.data
      }
    })
  },
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
      dialogPassword: false,
      dialogPhone: false,
      isVerifi: false, //是否发送验证码
      form: {

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
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        checkpass: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ],
        newpass: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ],
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
  methods: {
    getEmp() {
      
    },
    // 当点击取消时,将弹窗关闭
    handleClose() {
      this.dialogPassword = false;
      this.dialogPhone = false;
      if (this.$refs.form !== undefined) {
        this.$refs.form.resetFields()
      }
      if (this.$refs.phoneform !== undefined) {
        this.$refs.phoneform.resetFields()
      }
      if (this.$refs.passform !== undefined) {
        this.$refs.passform.resetFields()
      }
    },
    // 修改密码
    passSubmit() {
      // 数据校验
      this.$refs.passform.validate(valid => {
        if (valid) {
          //校验通过,发送请求
          // TODO 后端未写修改密码接口
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
    // 修改手机号
    phoneSubmit() {
      this.$refs.phoneform.validate(valid => {
        if (!this.isVerifi) {
          this.$message.error("请先发送验证码")
        }
        if (valid) {
          //校验通过,发送请求,
          // TODO 后端未写员工修改手机号接口
          changePhone(this.phoneform).then(res => {
            console.log("修改手机号", res)
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
          console.log("发送短信")
          postVerifi(this.phoneform).then(res => {
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
   
   
  },
}
</script>

<style scoped>
.container {
  height: 500px;
  width: 1100px;
  margin: 50px auto;
}

.security {
  height: 290px;
}

.text {
  height: 200px;
  padding-top: 30px;
}

div /deep/.el-card {
  border: 0;
}

.text /deep/.el-descriptions--medium.is-bordered .el-descriptions-item__cell {
  padding-left: 200px;
}

.text /deep/ .el-descriptions-item__container {
  padding-left: 280px;
}

.text /deep/.el-descriptions-item__container .el-descriptions-item__content {
  margin-left: 260px;
}

.text /deep/.el-descriptions-item__label {
  color: #2e9df7;
}

.button /deep/.el-button--text {
  font-size: 17px;
}

div /deep/.el-card__header {
  background-color: #f2f2f2;
  padding: 10px 20px;
}

div /deep/.el-descriptions--medium:not(.is-bordered) .el-descriptions-item__cell {
  padding-bottom: 20px;
}

.button {
  display: flex;
  justify-content: space-evenly;
  margin-top: 10px;
}
</style>