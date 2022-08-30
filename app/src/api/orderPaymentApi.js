import {post} from "./index"

const prefix = "/order/"

const REQUEST_BASE_URL = "ss-order";

/**
 * 订单,创建订单,订单支付 API 接口
 */
export default {

    //提交订单
    submit(body) { return post(prefix + "submit", body, REQUEST_BASE_URL) },

    //再来一单
    again(data) { return post(prefix + "again",data, REQUEST_BASE_URL) },

    // 重新支付, 立即支付
    resubmit(data) { return post(prefix + "resubmit",data, REQUEST_BASE_URL) },

}
