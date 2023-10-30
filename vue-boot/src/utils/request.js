import axios from 'axios'
import {serverIp,serverPort} from "../../public/config";
import store from '@/store';
const request = axios.create({
    baseURL: `http://${serverIp}:${serverPort}`,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})


// request拦截器
//可以在请求发送前做一些处理,比如统一价token 对参数统一加密
request.interceptors.request.use(config => {
    //获取本地存储中的对象,将其转换为对象
    let empToken = localStorage.getItem("EmpToken")
    console.log("request拦截器拦截到了empToken: ",empToken)
    if(empToken )
    {
        console.log("request拦截器拦截到了token: ",empToken)
        //将token加入到请求头
        config.headers['Authorization'] = empToken;
    }
    return config;
},error => {
    return Promise.reject(error)
});

//response 拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        console.log("这是前端的response拦截器拦截到的消息: ",res)  
        //如果是返回的文件
        if(response.config.responseType === 'blob')
        {
            return res;
        }
        return res;
    },
    error => {
        //当token过期时,清除token
        if (error.response.status === 401) {
            localStorage.clear("EmpToken")
            store.dispatch('clear')
        }
        return Promise.reject(error)
    }
)

export default request