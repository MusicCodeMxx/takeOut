import {get} from "./index"

const prefix = "/child/order/"

const REQUEST_BASE_URL = "order";

/**
 * 子订单 API 接口
 */
export default {

    // 查询所有订单
    list(params) { return get(prefix + "list/" + params,undefined, REQUEST_BASE_URL) },

}
