<template>
    <div>
        <el-tabs v-model="value" type="border-card" tab-position="left" :stretch="true" @tab-click="handleClick">
            <el-tab-pane label="最新商品" name="first">
                <div>
                    <el-row :gutter="20">
                        <el-col :span="6" v-for="(item, index) in news" :key="index"
                            style="margin-top: 10px;height: 300px;">
                            <div @click="details(item.id)">
                                <div class="grid-content bg-purple">
                                    <el-image style="width: 100%; height: 210px;" :src="item.imgUrl"></el-image>
                                </div>

                                <div style="text-align: center;">
                                    {{ item.name }}
                                </div>
                                <div style="text-align: center;color: red;margin-top: 10px;">
                                    售价: ￥{{ item.price }}
                                </div>
                            </div>
                        </el-col>
                    </el-row>
                </div>
            </el-tab-pane>
            <el-tab-pane :label="item.name + ''" :name="item.id + ''" v-for="(item, index) in dir" :key="index">
                <div>
                    <el-row :gutter="20">
                        <el-col :span="6" v-for="(item, index) in dirData" :key="index" style="margin-top: 10px;">
                            <div @click="details(item.id)">
                                <div class="grid-content bg-purple">
                                    <el-image style="width: 100%; height: 210px;" :src="item.imgUrl"></el-image>
                                </div>
                                <div style="text-align: center;">
                                    {{ item.name }}
                                </div>
                                <div style="text-align: center;color: red;margin-top: 10px;">
                                    售价: ￥{{ item.price }}
                                </div>
                            </div>
                        </el-col>
                    </el-row>
                </div>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import { getGoods, getGoodsDir } from '@/api'
export default {
    created() {
        this.getDir()
    },
    data() {
        return {
            value: 'first',
            news: [],   //最新商品
            dir: [],    //一级目录
            dirData: [],    //二级目录
        }
    },
    methods: {
        handleClick(tab) {
            getGoods(tab.name).then(res => {
                this.dirData = res.data
                this.value = tab.name
            })
        },
        getDir() {
            getGoodsDir().then(res => {
                this.dir = res.data
                this.news = res.map.prodList
            })
        },
        details(id) {
            //跳转到商品详情页
            this.$router.push(`/mall/details/${id}`)
        }
    }
}
</script>

<style></style>