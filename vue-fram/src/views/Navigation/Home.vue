<template>
  <div class="container">
    <el-dialog :close-on-click-modal="false" :title="smartOrMore ? '批量上传' : '智能标注'" :visible.sync="dialogTableVisible"
      width="600px" top="200px" style="text-align: center;" :before-close="close">
      <div v-if="!flag">
        <div v-if="!load">
          <span>请严格按照</span>
          <el-popover placement="right" width="540" trigger="click">
            <img src="../../assets/example.png" style="width: 515px;height: 240px;" alt="">
            <el-button type="text" slot="reference" style="display: inline;">示例</el-button>
          </el-popover>
          <span>上传压缩包</span>
          <el-upload drag style="width: 100%;" :on-error="error"
            :action="smartOrMore ? 'http://127.0.0.1:5000/mul_images_test' : 'http://127.0.0.1:5000/write_xml'" multiple
            :on-success="successMul" :on-progress="uping" :before-upload="beforeAvatarUpload">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传zip压缩包,且大小不能超过1MB</div>
          </el-upload>
        </div>
        <div v-else style="margin: 30px auto;font-size: 30px;">
          {{ smartOrMore ? '正在识别,请稍后...' : '正在标注,请稍后...' }}
        </div>
      </div>
      <div v-else>
        <div style="font-size: 30px;margin-bottom: 5px;">{{ smartOrMore ? '批量识别完成!' : '智能标注完成!' }}</div>
        <div style="margin-bottom: 20px;color: #999aaa;">{{ smartOrMore ? '点击下载按钮,下载识别结果。' : '点击下载按钮,下载数据配置文件。' }} </div>
        <el-button v-if="smartOrMore" type="primary" @click="downLoad">下载</el-button>
        <el-button v-else type="primary" @click="downZip">下载</el-button>
      </div>
    </el-dialog>
    <div class="content">
      <div class="title">
        ETREES
      </div>
      <div class="subhead">
        一款基于深度学习的植物表型识别系统
      </div>
      <div class="button">
        <el-button type="success" class="btn" icon="el-icon-camera" round><a href="http://127.0.0.1:5000/camera">实时监测</a>
        </el-button>

        <el-upload action="http://127.0.0.1:5000/imagesUp" :data="{ 'uuid': guid }" :on-success="handleSuccess">
          <el-button type="success" class="btn" icon="el-icon-picture" round>本地上传</el-button>
        </el-upload>

        <el-button type="success" class="btn" icon="el-icon-picture" @click="upMore">批量上传</el-button>

      </div>
      <div style="margin-top: 20px;height: 20px;">
        <el-button type="primary" class="btn" icon="el-icon-picture" round @click="smart">智能标注</el-button>
      </div>

    </div>
  </div>
</template>
<script>
import axios from 'axios'
export default {
  created() {

  },
  data() {
    return {
      dialogTableVisible: false,
      flag: false,
      txtName: '',
      smartOrMore: false, //false 为智能标注,true为批量标注
      load: false,
      uploadHeaders: {
        timeout: 240000
      }
    }
  },
  methods: {
    photoUp() {
      this.$router.push("/result")
    },
    handleSuccess(res) {
      // console.log("获取了照片:", res)

      this.$store.dispatch("resultimg", res)
      this.photoUp()
    },
    //智能标注
    smart() {
      this.smartOrMore = false
      this.dialogTableVisible = true
    },
    //批量上传
    upMore() {
      this.smartOrMore = true
      this.dialogTableVisible = true
    },
    error() {
      this.$message.error("上传失败")
      this.flag = false
      this.load = false
    },
    //关闭弹窗时
    close() {
      this.dialogTableVisible = false
      this.flag = false
    },
    //上传完成时
    successMul(res) {
      this.txtName = res
      this.flag = true
      this.load = false
    },
    uping() {
      console.log("上传时: ")
      this.load = true
    },

    downLoad() {
      axios({
        url: `http://127.0.0.1:5000/downloadfile?fileName=${this.txtName}`,
        method: 'GET',
        responseType: 'arraybuffer',
      }).then(res => {
        var arrayBuffer = res.data;
        var blob = new Blob([arrayBuffer], { type: 'application/octet-stream' });
        var url = URL.createObjectURL(blob);

        var link = document.createElement('a');
        link.href = url;
        link.download = this.txtName.replace('.zip', '.txt');  // 可以自定义文件名和扩展名

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })
    },
    downZip() {
      axios({
        url: `http://127.0.0.1:5000/downloadfile?fileName=${this.txtName}`,
        method: 'GET',
        responseType: 'arraybuffer'
      }).then(res => {
        var arrayBuffer = res.data;
        var blob = new Blob([arrayBuffer], { type: 'application/octet-stream' });
        var url = URL.createObjectURL(blob);
        var link = document.createElement('a');
        link.href = url;
        link.download = this.txtName;  // 可以自定义文件名和扩展名

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })
    },
    beforeAvatarUpload(file) {
      const isZip = file.type === 'application/x-zip-compressed';

      console.log("文件类型: ", file.type)
      const isLt2M = file.size / 1024 / 1024 < 1;

      if (!isZip) {
        this.$message.error('上传的文件类型只能是.zip');
      }
      if (!isLt2M) {
        this.$message.error('上传压缩包大小不能超过 1MB!');
      }
      return isZip && isLt2M;
    }
  },
  computed: {
    guid() {
      function S4() {
        return (((1 + Math.random()) * 0x10000) | 0)
          .toString(16).substring(1);
      }
      return (S4() + S4() + "-" + S4() + "-" + S4() +
        "-" + S4() + "-" + S4() + S4() + S4());
    }
  }
}
</script>
<style scoped>
.container {
  display: flex;
  height: 600px;
  margin-left: 180px;
  margin-right: 180px;
  position: relative;
}

a {
  text-decoration: none;
  color: white;
}

.content {
  position: absolute;
  top: 80px;
  left: 100px;
  z-index: 2;
}

.title {
  font-size: 80px;
  font-weight: 800;
  font-family: "PingFang SC";
}

.subhead {
  font-size: 21px;

}

.button {
  display: flex;
  margin-top: 40px;

}

.btn {
  font-size: 17px;
  border-radius: 30px;
  height: 60px;
  width: 150px;
  margin-right: 10px;
}
</style>

<!-- <template>
  <div>
    <input type="file" ref="fileInput" />
    <button @click="uploadFile">上传</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  methods: {
    uploadFile() {
      const fileInput = this.$refs.fileInput;
      const file = fileInput.files[0];

      const formData = new FormData();
      formData.append('file', file);

      axios.post('http://api.etrees.top/mul_images_test', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        // timeout: 5000
      })
        .then(response => {
          console.log('文件上传成功！');
        })
        .catch(error => {
          console.error('文件上传失败：', error);
        });
    }
  }
};
</script> -->