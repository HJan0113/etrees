<template>
    <div class="container">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <i class="el-icon-back" style="font-size: 23px;"></i>
                <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
            </div>
            <div class="text" v-if="!flag">
                <div>
                    <el-row :gutter="20">
                        <el-col :span="6" v-for="(item, index) in search" :key="index"
                            style="margin-top: 10px;padding-left: 20px;">
                            <div @click="details(item.id)">
                                <div class="grid-content bg-purple">
                                    <el-image style="width: 100%; height: 210px;" :src="item.imgUrl"></el-image>
                                </div>
                                <div style="text-align: center;">
                                    {{ item.dirName + '-' + item.name }}
                                </div>
                            </div>
                        </el-col>
                    </el-row>
                </div>
            </div>
            <div style="display: flex;" v-else>
                <div style="flex: 1;text-align: center;">
                    <img style="width: 350px;height: 350px;" :src="table.imgUrl" alt="">
                </div>
                <div style="flex: 1;">
                    <div style="display: flex;justify-content: space-between;">
                        <div style="font-weight: 600;">
                            <div>{{ table.dirName + table.name }}</div>
                        </div>
                        <div>
                            多发时期: {{ table.season }}
                        </div>
                    </div>
                    <div style="margin-top: 20px;">
                        <el-tabs value="first" :stretch="true" style="text-indent: 2em;">
                            <el-tab-pane label="病害描述" name="first">{{ table.overview }}</el-tab-pane>
                            <el-tab-pane label="病发原因" name="second">{{ table.reason }}</el-tab-pane>
                            <el-tab-pane label="治疗方案" name="third">{{ table.treatment }}</el-tab-pane>
                        </el-tabs>
                    </div>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
import { searchHandbook, getHandDetail } from '@/api';
export default {
    created() {
        const info = this.searchData
        searchHandbook(info).then(res => {
            if (res.code === 1) {
                if (res.data.length === 0) {
                    this.$message.error("未找到该数据")
                    return
                }
                this.search = res.data
            }
        })
    },
    data() {
        return {
            isNot: false,
            flag: false,
            search: [],
            table: {}
        }
    },
    methods: {
        back() {
            if (this.flag) {
                this.flag = false
            } else {
                this.$router.push('/')
            }

        },
        details(id) {
            getHandDetail(id).then(res => {
                this.table = res.data
                this.flag = true
            })
        },

    },
    computed: {
        searchData() {
            return this.$store.state.search
        }
    },
    watch: {
        searchData(newValue, oldValue) {
            searchHandbook(newValue).then(res => {
                if (res.code === 1) {
                    if (res.data.length === 0) {
                        this.$message.error("未找到该数据")
                        return
                    }
                    this.search = res.data
                }
            })
        }
    }
}
</script>

<style scoped>
.container {
    display: flex;
    height: 600px;
    flex-direction: column;
}

div /deep/.el-dialog__body {
    padding: 20px 20px 30px 20px;
}

.box-card::-webkit-scrollbar {
    width: 0;
    /* 隐藏滚动条 */
}

.box-card {
    flex: 1;
    overflow: auto;
    margin: 20px auto;
    width: 1200px;
    height: 100%;
    background-color: rgba(253, 246, 227, 0.6);
    border-radius: 30px;
}
</style>