import axios from "axios";

import localStorage from "../utils/localStorage";
import localStorageCache from "../utils/localStorage";
import system from "../common/system";
import {Notify} from 'vant';
import router from "../router";

const TOKEN_KEY = system.TOKEN_KEY;

const instance = axios.create({
    // 响应超时
    timeout: 3000,
});

/** 请求拦截 */
instance.interceptors.request.use(
    config => {
        // 动态绑定每个服务
        config.baseURL = `${system.apiUrl}api/${config.requestBaseUrl}/`
        // 序列化 Get 请求的参数
        if (config.method === 'get' && config.params) {
            let url = config.url + '?';
            for (const propName of Object.keys(config.params)) {
                const value = config.params[propName];
                let part = encodeURIComponent(propName) + "=";
                if (value !== null && typeof(value) !== "undefined") {
                    if (typeof value === 'object') {
                        for (const key of Object.keys(value)) {
                            let params = propName + '[' + key + ']';
                            let subPart = encodeURIComponent(params) + "=";
                            url += subPart + encodeURIComponent(value[key]) + "&";
                        }
                    } else {
                        url += part + encodeURIComponent(value) + "&";
                    }
                }
            }
            url = url.slice(0, -1);
            config.params = {};
            config.url = url;
        }
        // 检查是否有 token
        const token = localStorage.get(TOKEN_KEY);
        if (token) config.headers['Authorization'] = 'Bearer ' + token;
        return config;
    },
    error => {
        /** 当产生错误时候 */
        console.log(error);
        Notify('未知错误');
        return Promise.reject(error);
    }
);

/** 响应拦截 */
instance.interceptors.response.use(
    response => {
        /** 后端返回成功的信息处理 */
        // 检查后端是否有写入令牌
        const token = response.headers['authorization'];
        // 检查是否合法
        if (token) if (token.startsWith("Bearer ")) localStorage.set(TOKEN_KEY, token.replace('Bearer ', ''));
        return statusHandle(response);
    },
    error => {
        /** 后端返回异常或错误信息处理 */
        return statusHandle(error.response);
    }
);

/** 状态表驱动 */
const STATUS = {
    "200" : response =>{return new SuccessOK(response)},
    "400" : response =>{return new BaoRequest(response)},
    "401" : response =>{return new Unauthorized(response)},
    "500" : response =>{return new InternalServerRrror(response)},
}

/** 成功类 */
class SuccessOK{
    constructor(response) { this.response = response; }
    getResponse(){
        // console.log("200 成功响应 --- ",this.response.data.message);
        return this.response.data;
    }
}
/** 400 请求参数错误  */
class BaoRequest{
    constructor(response) { this.response = response; }
    getResponse(){
        console.log(" 400 错误 --- ",this.response.data.message);
        const {code,message} = this.response.data
        if (code === 1003) Notify(message);
        return this.response.data;
    }
}

/** 401 未授权  */
class Unauthorized{
    constructor(response) { this.response = response; }
    getResponse(){
        console.log(" 401 错误 --- ",this.response.data.message);
        if (this.response.data.code === 400001 || this.response.data.code === 401){
            Notify('您当前还未登录');
            toLogin();
        }
        return this.response.data;
    }
}

/** 500 服务器错误  */
class InternalServerRrror{
    constructor(response) { this.response = response; }
    getResponse(){
        console.log(" 500 错误 --- ",this.response.data.message);
        switch (this.response.data.code) {
            case 401001:
                Notify('您当前还未登录，无法操作');
                break
            case 30001:
                this.response.data.message = '您当前还未登录，无法操作';
                toLogin();
                break;
        }
        return this.response.data;
    }
}

/** HTTP 状态处理, 增强处理 */
const statusHandle = (response) => {
    const responseObject = STATUS[response.status](response).getResponse();
    return responseObject ? responseObject : response;
}

/* 导出 Post Put Delete */
// export const { get,post,put,delete:del } = instance;
export const get = (url, params, requestBaseUrl) => {
    return instance({
        method: "get",
        url: url,
        dataType:"JSON",
        headers: { 'content-type': 'application/json' },
        params: params,
        requestBaseUrl: requestBaseUrl
    })
}
export const post = (url, data, requestBaseUrl) => {
    return instance({
        method: "post",
        url: url,
        dataType:"JSON",
        headers: { 'content-type': 'application/json' },
        data: data,
        requestBaseUrl: requestBaseUrl
    })
}
export const put = (url, data, requestBaseUrl) => {
    return instance({
        method: "put",
        url: url,
        dataType:"JSON",
        headers: { 'content-type': 'application/json' },
        data: data,
        requestBaseUrl: requestBaseUrl
    })
}
export const del = (url, data, requestBaseUrl) => {
    return instance({
        method: 'delete',
        url: url,
        dataType:"JSON",
        headers: { 'content-type': 'application/json' },
        params: undefined, // 请求参数拼接在url上
        data: data,  // 请求参数放在请求体
        requestBaseUrl: requestBaseUrl
    })
}

// 清除全部缓存并且去登录
const toLogin = ()=>{
    // 清除全部缓存
    localStorageCache.clearAll();
    // 去登录页
    router.replace("/login")
}