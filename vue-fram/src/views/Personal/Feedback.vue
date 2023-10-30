<template>
  <div>
    <el-tabs value="first" :stretch="true">
      <el-tab-pane label="问题反馈" name="first">
        <div>
          <div style="border: 2px #f5f5f5 solid;padding: 10px;">
            <div>反馈主题</div>
            <div style="margin-top: 10px;" class="feedback">
              <el-input v-model="form.theme" placeholder="请输入反馈主题"></el-input>
            </div>
          </div>
          <div style="border: 2px #f5f5f5 solid;margin-top: 30px;padding: 10px;">
            <div>反馈内容</div>
            <div style="margin-top: 10px;" class="feedback">
              <el-input :rows="8" :show-word-limit="true" type="textarea" v-model="form.content"
                placeholder="请输入反馈内容(300字以内)"></el-input>
            </div>
          </div>
        </div>
        <div style="text-align: center;margin-top: 10px;">
          <el-button type="primary" @click="submit">提 交 反 馈</el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="完善数据集" name="second">

        <div style="padding: 30px;">

          <div style="height: 300px;width: 500px;margin: 10px auto;text-indent: 2em;font-size: 20px;padding: 30px;">
            <el-card>
              如果您的手上有稀缺物种的病害或者物种的稀缺病害资料欢迎联系我们，我们将在核对信息无误后，使用您的数据进行深度学习的训练，完善我们平台的模型。感谢您对我们平台的支持！
            </el-card>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { subFeedback } from '@/api'
export default {
  data() {
    return {
      form: {
        theme: '',
        content: ''
      }
    }
  },
  methods: {
    submit() {
      if (!this.form.theme) {
        this.$message.error("请填写反馈主题")
      } else if (!this.form.content) {
        this.$message.error("请填写反馈内容")
      } else {
        subFeedback(this.form).then(res => {
          if (res.code === 1) {
            this.$message({
              type: 'success',
              message: res.data
            })
            this.form.theme = '',
              this.form.content = ''
          } else {
            this.$message.error(res.msg)
          }
        })
      }
    }
  }

}
</script>

<style scoped> .feedback>>>.el-input__inner {
   border: none;
   padding: 3px;
 }

 .feedback>>>.el-textarea__inner {
   border: none;
   padding: 3px;
   font-family: 'element-icons';
 }
</style>