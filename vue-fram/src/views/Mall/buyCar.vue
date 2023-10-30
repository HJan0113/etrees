<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <i class="el-icon-back" style="font-size: 23px;"></i>
                <el-button type="text" style="font-size: 20px;" @click="back">返回</el-button>
            </div>
            <div>
                <el-form ref="dataform" :model="form" :rules="rules">
                    <div style="display: flex;width: 60%;margin: 0 auto;">
                        <div>
                            <div style="display: flex;justify-content: space-between;">
                                <div style="font-size: 20px;font-weight: 600;">填写收货信息</div>
                                <div><el-button type="text" @click="toSelect">选择收货信息</el-button></div>
                            </div>
                            <div>
                                <el-form-item label="收件人" label-width="100px" prop="name">
                                    <el-input v-model="form.name" placeholder="请输入收件人姓名" autocomplete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="手机号" label-width="100px" prop="phone">
                                    <el-input v-model="form.phone" placeholder="请输入收件人手机号" autocomplete="off"></el-input>
                                </el-form-item>
                                <el-form-item label="收货地址" label-width="100px" prop="address">
                                    <el-input v-model="form.address" placeholder="请输入收件地址" autocomplete="off"></el-input>
                                </el-form-item>
                                <el-button style="margin-left: 400px;width: 300px;" type="primary" @click="submit">提
                                    交</el-button>
                            </div>
                        </div>
                    </div>
                </el-form>
            </div>
        </el-card>
    </div>
</template>

<script>
import { getAddress, buyByCar } from '@/api'
export default {
    mounted() {
        const id = this.$store.state.selectAddress
        if (id !== '') {
            getAddress().then(res => {
                if (res.code === 1) {
                    let address = res.data
                    const result = address.filter(res => {
                        return res.id === id
                    })
                    this.form.name = result[0].name
                    this.form.address = result[0].address
                    this.form.phone = result[0].phone
                }
            })
        }
    },
    data() {
        return {
            form: {
                quantity: '1',
                name: '',
                phone: '',
                address: ''
            },
            rules: {
                name: [
                    { required: true, message: '请输入姓名', trigger: 'blur' },
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' }
                ],
                address: [
                    { required: true, message: '请输入地址', trigger: 'blur' }
                ],
            }
        }
    },
    methods: {
        submit() {
            this.$refs.dataform.validate(res => {
                if (res) {
                    const form = {
                        name: this.form.name,
                        phone: this.form.phone,
                        address: this.form.address
                    }
                    const ids = this.$store.state.shopCarIds
                    buyByCar(form,ids).then(res => {
                        if (res.code === 1) {
                            this.$message.success(res.data)
                            this.$router.back()
                            this.form.name = ''
                            this.form.phone = ''
                            this.form.address = ''
                            this.$store.dispatch('clearselect')
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
                } else {

                }
            })
        },
        back() {
            this.form.name = ''
            this.form.phone = ''
            this.form.address = ''
            this.$store.dispatch('clearselect')
            this.$router.back()
        },
        toSelect() {
            this.$router.push('/mall/checkaddress')
        }
    }
}
</script>

<style></style>