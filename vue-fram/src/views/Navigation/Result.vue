<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <i class="el-icon-back" style="font-size: 23px;"></i>
            <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
        </div>
        <div class="text">
            <div class="original">
                <img style="width: 100%;height: 100%;" :src="'data:image/jpg;base64,' + (!flag ? resultImg.img1:resultImg_img2)" alt="">
                <el-button v-if="!button" @click="swit" type="primary" style="margin-top: 20px;width: 160px;">{{
                    !flag ? '病害标注' : '查看原图' }}</el-button>
                <el-button v-else @click="recognition" type="primary"
                    style="margin-top: 20px;width: 160px;">立即识别</el-button>
            </div>
            <div class="result">
                <div v-if="isHasData">
                    <el-descriptions :column="1">
                        <el-descriptions-item label="病害种类">{{ resultData.res1 }}</el-descriptions-item>
                        <el-descriptions-item label="病害数量">{{ resultData.res2 }}</el-descriptions-item>
                        <el-descriptions-item label="识别时间">{{ resultData.res3 }}</el-descriptions-item>
                    </el-descriptions>
                </div>
                <div v-else style="margin-top: 150px;font-size: 40px;color: red;">
                    提示: 该图片中未包含任何病害信息,请重新上传
                </div>
                <div>
                    <el-tabs value="0">
                        <el-tab-pane :label="item.dirName + item.name" :name="index + ''" v-for="(item, index) in ResHand"
                            :key="index">
                            <div style="margin-top: 10px;">多发季节: {{ item.season }}</div>
                            <el-collapse style="margin-top: 20px;" accordion>
                                <el-collapse-item title="病害描述" name="1">
                                    <div style="background-color: transparent;text-indent: 2em;">{{ item.overview }}
                                    </div>
                                </el-collapse-item>
                                <el-collapse-item title="病发原因" name="2">
                                    <div style="background-color: transparent;text-indent: 2em;">{{ item.reason }}
                                    </div>
                                </el-collapse-item>
                                <el-collapse-item title="治疗方案" name="3">
                                    <div style="text-indent: 2em;">
                                        {{ item.treatment }}
                                    </div>
                                </el-collapse-item>
                                <el-collapse-item title="推荐商品" name="4">
                                    <div v-for="(val, no) in item.shop" :key="no"
                                        style="display: flex;justify-content: space-around;text-align: left;">
                                        <div style="flex: 1;margin-left: 60px;color: red;">
                                           <router-link :to="`/mall/details/${val.id}`">{{ val.name }}</router-link> 
                                        </div>
                                        <div style="flex: 1;">售价: ￥{{ val.price }}</div>
                                    </div>
                                </el-collapse-item>

                            </el-collapse>
                        </el-tab-pane>

                    </el-tabs>
                </div>
            </div>
        </div>
    </el-card>
</template>

<script>
import axios from 'axios';
import { getInfoAndShop, setInfo } from '@/api';
export default {
    created() {
        this.resultImg = this.$store.state.resultImg
        return
    },
    data() {
        return {
            button: true,   //识别or标注
            isHasData: true,   //是否识别到了数据
            resultImg: {},//识别结果
            resultImg_img2: '',
            resultData: {},
            flag: false,    //查看原图和标注病害
            ResHand: [],  //病害图鉴信息
        }
    },
    methods: {
        swit() {
            if (this.resultImg_img2 === '') {
                axios({
                    url: `http://127.0.0.1:5000/CL_images?fileName=${this.resultImg.fileName}`,
                    method: 'GET',
                }).then(res => {
                    console.log("标注: ", res.data.img2)
                    this.resultImg_img2 = res.data.img2
                })
            } 
            this.flag = !this.flag
        },
        back() {
            this.$router.back()
        },
        recognition() {
            axios({
                url: `http://127.0.0.1:5000/images?fileName=${this.resultImg.fileName}`,
                method: 'GET',

            }).then(res => {
                const realRes = res.data[0]
                if (realRes.res1 === 'hjw5201314lyt') {
                    this.isHasData = false
                } else {
                    this.isHasData = true
                    this.button = false
                    this.resultData = realRes
                    getInfoAndShop(this.resultData.res1).then(res => {
                        if (res.code === 1) {
                            let handBookData = res.data.map((res) => {
                                const values = Object.values(res)
                                return values[0]
                            })   //图鉴对象数组
                            let shopData = res.list[0]  //商品数据
                            for (let i = 0; i < shopData.length; i++) {
                                handBookData[i].shop = Object.values(shopData[i])[0]
                            }
                            this.ResHand = handBookData
                        }
                    })
                    const recordData = {
                        name: this.resultData.res1,
                        equipment: "PC端"
                    }
                    const recordImg = {
                        code: this.resultImg.img1
                    }
                    setInfo(recordData, recordImg)
                }

                let resdd = document.querySelector('.result')
                resdd.style.opacity = 1
                
                
            })


        }
    },
    computed: {

    }
}
</script>

<style scoped>
.box-card {
    margin: 0 auto;
    width: 1200px;
    height: 650px;
    background-color: rgba(253, 246, 227, 0.8);
    border-radius: 30px;
}

.text {
    display: flex;
    justify-content: space-around;
    align-content: center;
    height: 600px;
    /* margin-top: 60px; */
    /* background-color: #fdf6e3; */
}

.original {
    width: 350px;
    height: 350px;
    margin-top: 30px;
    text-align: center;
    /* background-color: skyblue; */
}

.result {
    width: 550px;
    height: 100%;
    opacity: 0;
    transition: 1s all;
    /* margin-top: -20px;   */
    /* margin-right: 130px; */
    /* background-color: white; */
}

.shop {
    position: absolute;
    top: 550px;
    left: 600px;
    right: 600px;
}

.shop-name:hover {
    cursor: pointer;
}

div/deep/.el-descriptions__body {
    background-color: transparent;
}

div/deep/.el-collapse-item__header {
    background-color: transparent;
}

div/deep/.el-collapse-item__wrap {
    background-color: transparent;
}
</style>