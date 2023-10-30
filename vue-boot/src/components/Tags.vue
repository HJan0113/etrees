<template>
  <div class="tabs">
    <el-tag
        v-for="(tag,index) in tags"
        :key="tag.path"
        :closable="tag.name!=='home'"
        @click="$route.name===tag.name? '' : clickMeau(tag)"
        @close="closeMeau(tag,index)"
        size="small"
        :effect="$route.name === tag.name? 'dark':'plain'"
    >
      {{ tag.meta.title }}
    </el-tag>
  </div>
</template>
<script>
export default {
  data() {
    return {
      tags: [
        {path:'home',name: 'home',meta: {title: '首页'}}
      ]
    };
  },
  watch: {
    $route(to,from)
    {
      console.log("to: ",to)
      console.log("from:",from)

      if (this.tags.findIndex(value => value.name === to.name) === -1) {
        this.tags.push(to)
      }

    }
  },

  methods: {
    clickMeau(tag)
    {
      //点击跳转路由
      this.$router.push({name:tag.name})
    },
    closeMeau(tag,index)
    {
      //点击删除标签
      this.tags.splice(index,1)
      //获取数组长度
      const length = this.tags.length
    //  如果删除的项不是当前所在的 路由
      if(tag.name!==this.$route.name){
        return
      }
    //  如果删除的是最后一项
      if(index===length){
          //往前一项跳转
        this.$router.push({name: this.tags[index-1].name})
      }
      else{
        this.$router.push({name: this.tags[index].name})
      }
    }
  }
}
</script>
<style lang="less" scoped>
.tabs {
  padding-top: 10px;
  padding-left: 20px;
  .el-tag {
    margin-right: 15px;
    cursor: pointer;  //鼠标放上去会有个小手的形状
  }
}
</style>
