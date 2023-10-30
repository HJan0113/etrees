<template>
    <div class="container">
        <el-card class="box-card">
            <div style="text-align: center;margin-top: 80px;">
                <img style="width: 450px;height: 75px;" src="../../assets/logo.png" alt="">
            </div>
            <div style="text-align: center;margin-top: 20px;">
                Version: 1.0.0
            </div>
            <div class="desc">
                <div class="desc-des" style="">
                    <div class="desc-title">平台介绍</div>
                    <div class="desc-content">ETREES平台集成虫害识
                        别、图鉴检索、商品销售三大
                        功能，贴合用户的需求及使用习惯。</div>
                </div>
                <div class="desc-des">
                    <div class="desc-title">商务合作</div>
                    <div class="desc-content">若您有意向与我方合作，
                        请您致电与我方工作人员联
                        系，我们将竭诚安排与您的洽谈。</div>
                </div>
            </div>
            <div style="text-align: center;margin-top: 100px;">
                <div>工信部备案号:赣ICP备2023005084号-1</div>
                <div>感觉优化不如原 版权所有</div>
            </div>

        </el-card>
    </div>
</template>

<script>
export default {
    data() {
        return {
        }
    },

}
</script>

<style scoped>
.container {
    display: flex;
    height: 600px;
    flex-direction: column;
}

.box-card {
    flex: 1;
    overflow: auto;
    margin: 20px auto;
    width: 1200px;
    height: 100%;
    background-color: rgba(253, 246, 227, 0.6);
    border-radius: 30px;
    /* text-align: center; */
}

.box-card .text {
    width: 90%;
    height: 100%;
    overflow: auto;
    margin: 0 auto;
}

.desc {
    display: flex;
    justify-content: space-evenly;
    margin-top: 30px;
}

.desc-des {
    display: flex;
    height: 100px;
    width: 400px;
}

.desc-title {
    flex: 1;
    align-self: center;
}

.desc-content {
    flex: 3;
    padding-top: 20px;
    text-indent: 2em;
}
</style>