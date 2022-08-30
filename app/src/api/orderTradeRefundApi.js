import {post} from "./index";

const prefix = "/tradeRefund/";

const REQUEST_BASE_URL = "ss-order";

export default {

    // 退货退款申请
    returnApply(data) {
        return post(prefix + "apply", data, REQUEST_BASE_URL)
    },

}