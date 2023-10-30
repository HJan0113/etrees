import http from "@/utils/request"

export const getInfo = () => {
    return http.get('/emps/self')
}
//分页请求和查询用户
export const getAllUser = (currentPage, pageSize, search) => {
    return http.get(`/user/${currentPage}/${pageSize}`, {
        //传入 ?username= &ID= 类型的参数
        params: {
            userName: search.userName,
            id: search.id
        }
    }
    )
}
// 启/禁用用户
export const banUser = (id) => {
    return http.post(`/user/status/${id}`)
}
//分页查询员工
export const getAllEmp = (currentPage, pageSize, search) => {
    return http.get(`/emps/${currentPage}/${pageSize}`, {
        params: {
            empName: search.empName,
            id: search.ID
        }
    })
}

//新增员工数据
export const save = (data) => {
    return http.post('/emps', data);
}

//修改员工数据
export const updateData = (data) => {
    return http.put('/emps', data);
}

//单个删除员工数据
export const deleteData = (id) => {
    return http.delete(`/emps/${id}`);
}

//员工登录校验
export const loginData = (data) => {
    return http.post('/emps/login', data, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
}

//echarts获取数据
export const echartsData = () => {
    return http.get('/echarts')
}

//批量删除员工
export const removeAll = (ids) => {
    return http.post('/emps/delete', ids)
}

//员工信息回显
export const getEmpMsg = (id) => {
    return http.get(`/emps/${id}`)
}

//添加商品
export const saveGoods = (goods) => {
    return http.post('/goods', goods)
}
//商品列表回显(考虑分页)
export const goodsList = () => {
    return http.get('/goods')
}
//换绑手机发送验证码
export const postVerifi = (data) => {
    return http.post('/sendMsg', data)
}
//根据id查询员工权限
export const getPermission = (id) => {
    return http.get(`/emps/perms/${id}`)
}
//修改对应id的权限数据
export const upPermission = (id, perms) => {
    return http.put(`/emps/addPerms/${id}`, perms)
}

//添加图鉴目录
export const add_index = (data) => {
    return http.post('/data/dir', data)
}

//获取全部目录信息
export const getAllIndex = () => {
    return http.get('/data/dir')
}

//修改目录信息
export const updateIndex = (data) => {
    return http.put('/data/dir', data)
}

//删除目录
export const deleteIndex = (id) => {
    return http.delete(`/data/dir/${id}`)
}

//分页获取图鉴信息
export const getHandbookLimit = (currentPage, pageSize) => {
    return http.get(`/data/info/${currentPage}/${pageSize}`)
}

//添加图鉴信息
export const addHandBook = (data) => {
    return http.post('/data/info', data)
}

//设为/取消常用
export const setOrCan = (id) => {
    return http.post(`/data/status/${id}`)
}

//删除图鉴信息
export const deleteHandBook = (id) => {
    return http.delete(`/data/info/${id}`)
}

//修改图鉴信息
export const updateHandInfo = (data) => {
    return http.put('/data/info', data)
}

//获取全部提示信息
export const getAllTips = () => {
    return http.get('/data/tips')
}

//添加提示信息
export const addTips = (text) => {
    return http.post('/data/tips', text)
}

//修改提示信息
export const updataTip = (data) => {
    return http.put('/data/tips', data)
}

//删除提示信息
export const delTips = (id) => {
    return http.delete(`/data/tips/${id}`)
}

//商品目录信息回显
export const getGoods = () => {
    return http.get('/product/dir')
}

//删除目录
export const delGoods = (id) => {
    return http.delete(`/product/dir/${id}`)
}

//添加目录
export const addIndex = (data) => {
    return http.post('/product/dir', data)
}

//获取分页商品数据
export const getIndex = (currentPage, pageSize, search) => {
    return http.get(`/product/${currentPage}/${pageSize}`, {
        //传入 ?username= &ID= 类型的参数
        params: {
            name: search.name,
            id: search.id
        }
    })
}
//添加商品
export const addGoods = (data) =>{
    return http.post('product',data)
}

//修改商品信息
export const updateGoods = (data)=>{
    return http.put('product',data)
}

//分页查询订单记录
export const getRecord = (currentPage, pageSize, form) => {
    return http.get(`/product/order/${currentPage}/${pageSize}`, {
        //传入 ?username= &ID= 类型的参数
        params: {
            orderId: form.orderId,
            productId: form.productId
        }
    })
}

//修改商品订单
export const updataGoods = (data)=> {
    return http.put('/product/dir',data)
}

//删除商品
export const delGood = (id)=> {
    return http.delete(`/product/${id}`)
}

//获取反馈信息
export const getFeedBack = (currentPage, pageSize, form) => {
    return http.get(`/maintain/feedback/${currentPage}/${pageSize}`, {
        //传入 ?username= &ID= 类型的参数
        params: {
            feedbackId: form.feedback_id,
            userId: form.user_id
        }
    })
}

//获取首页数据
export const getHome =  () =>{
    return http.get('/home')
}

//识别记录
export const getRecords = (currentPage, pageSize, form) => {
    return http.get(`/data/record/${currentPage}/${pageSize}?userId=${form.userId}&startTime=${form.start}&endTime=${form.end}`)
}

//日志
export const getLog = (currentPage, pageSize, form) => {
    return http.get(`/maintain/log/${currentPage}/${pageSize}?id=${form.id}&startTime=${form.start}&endTime=${form.end}`)
}

//获取图表信息
export const getBarData = () =>{
    return http.get('/home/shop')
}

