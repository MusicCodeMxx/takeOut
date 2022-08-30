import {del, post} from "./index"

const prefix = "/login/"

const REQUEST_BASE_URL = "ss-login";

export default {

    // 发送验证码
    send(body) {
        return post(prefix + "sendMsm", body, REQUEST_BASE_URL)
    },

    // 提交登录
    submit(body) {
        return post(prefix + "submit", body, REQUEST_BASE_URL)
    },

    // 退出登录
    logout() {
        return del(prefix + "logout", undefined, REQUEST_BASE_URL)
    }

}