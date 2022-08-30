import {get} from "./index"

const prefix = "/payment/"

const REQUEST_BASE_URL = "ss-payment";

export default {

    // 发送验证码
    payOrderById(body) {
        return get(prefix + "alipay/mobile/" + body, null, REQUEST_BASE_URL)
    },

}