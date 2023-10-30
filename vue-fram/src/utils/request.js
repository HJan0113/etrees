import axios from 'axios'
import { serverIp, serverPort } from "../../public/config";
import { Message } from 'element-ui'
import router from '@/router';
const token = localStorage.getItem("token")
const request = axios.create({
    baseURL: `http://${serverIp}:${serverPort}`,
    // timeout: 5000,
    // headers: {
    //     'Authorization': `${token}`,
    // }
})

// request拦截器
//可以在请求发送前做一些处理,比如统一价token 对参数统一加密
request.interceptors.request.use(config => {
    // 在本地存储中获取token,
    const token = localStorage.getItem("token");
    //如果token存在,则将其放入请求头
    if (token) {
        config.headers.Authorization = `${token}`
    }
    return config;
}, error => {
    return Promise.reject(error)
});

//response 拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        console.log("这是前端的response拦截器拦截到的消息: ", res)
        //TODO 当后端响应状态码为401时,可以删除
        // if(res.code === 0){
        //     Message({
        //         message: '登录已过期，请重新登录',
        //         type: 'warning'
        //     })
        //     localStorage.clear("token")
        //     router.push("/")
        // }
        //如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res;
        }
        //兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }

        return res;
    },
    error => {
        //当token过期时,清除token

        // if (error.response.status === 401) {
        //     localStorage.clear("token")
        //     Message({
        //         message: '登录已过期，请重新登录',
        //         type: 'warning'
        //     })
        //     router.push('/')
        // }
        return Promise.reject(error)
    }
)
export default request