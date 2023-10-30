import http from "@/utils/request"


//修改密码
export const changePwd = (data) => {
    return http.put('/api/pwd', {
        password: data.password,
        newpass: data.newpass
    });
}

//用户登录校验
export const loginData = (data) => {
    return http.post('/api/login', data);
}

//用户注册账号
export const resign = (data, checkCode) => {
    return http.post(`/api/register/${checkCode}`, data)
}

// 用户修改手机号
export const changePhone = (data) => {
    return http.post('/api/phone', data)
}

// 用户修改个人信息
export const changePerson = (data) => {
    return http.put('/api/personal', data)
}

// 发送手机验证码
export const postVerifi = (phone) => {
    return http.post('/sendMsg', phone)
}

//用户基本信息回显
export const getPersonMsg = () => {
    return http.get('/api')
}

//获取图鉴一级目录
export const getHandDir = () => {
    return http.get('/api/data/getDir')
}
//获取二级目录
export const getHandBook = (id) => {
    return http.get(`/api/data/getChiDir/${id}`)
}
//获取详情
export const getHandDetail = (id) => {
    return http.get(`/api/data/getInfo/${id}`)
}
//常见病害
export const getFrequent = () => {
    return http.get('/api/data/frequent')
}

//获取一级商品目录
export const getGoodsDir = () => {
    return http.get('/api/shop/getShop')
}

//获取二级商品数据
export const getGoods = (id) => {
    return http.get(`/api/shop/getChiDir/${id}`)
}

//获取商品详细数据
export const getGoodsDetail = (id) => {
    return http.get(`/api/shop/getInfo/${id}`)
}

//加入购物车
export const addToCar = (data) => {
    return http.post('/api/shop/setCart',data)
}

//购买商品
export const buyGoods = (data) => {
    return http.post('/api/shop/buy',data)
}

//获取订单记录
export const getOrder = () => {
    return http.get('/api/getOrder')
}

//收货地址显示
export const getAddress = () => {
    return http.get('/api/person/addr')
}

//添加收货地址
export const addAdd = (data) => {
    return http.post('/api/person/addr',data)
}

//删除收货地址
export const delAdd = (id) => {
    return http.delete(`/api/person/addr/${id}`)
}

//获取识别记录
export const getRecord = () => {
    return http.get('/api/person/record')
}

//提交反馈
export const subFeedback = (data) => {
    return http.post('/api/feedback',data)
}

//图鉴查询
export const searchHandbook = (search) => {
    return http.get(`/api/data/search?info=${search}`)
}

//获取购物车数据
export const getCar = () => {
    return http.get('/api/shop/getCart')
}

//删除购物车数据
export const delCar = (ids) => {
    return http.post('/api/shop/cart',ids)
}

//购物车商品数量改变
export const changeNum = (id,num) => {
    return http.post(`/api/shop/cart/setNum/${id}?num=${num}`)
}

//购物车数据结算
export const buyByCar = (form,ids) => {
    return http.post(`/api/shop/cart/buy?name=${form.name}&address=${form.address}&phone=${form.phone}`,ids)
}

//商品搜索
export const searchGoods = (info) => {
    return http.get(`/api/shop/search?info=${info}`)
}

//实名认证
export const auth = () => {
    return http.post('/api/user/auth')
}

//查询识别结果信息
export const getInfoAndShop = (data) => {
    return http.post(`/api/getX?text=${data}`)
}

//将识别结果保存到后端
export const setInfo = (data,value) => {
    return http.post(`/api/setRec?name=${data.name}&equipment=${data.equipment}`,value)
}

