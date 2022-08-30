import {get} from "./index"

const prefix = "/coupon/"

const REQUEST_BASE_URL = "ss-coupon";

/**
 * 子订单 API 接口
 */
export default {

    // 查询所有订单
    pageList(params) { return get(prefix + "batch/pageList",params, REQUEST_BASE_URL) },

    list() {
        return get(prefix + "list", null, REQUEST_BASE_URL)
    },

    batchList(params) { return get(prefix + "batch/list",params, REQUEST_BASE_URL) },

    // 领取指定的优惠券
    receiveCouponById(id) { return get(prefix + "batch/receive/" + id,undefined, REQUEST_BASE_URL) },

    // 获取当前持有的优惠券列表
    meCouponPageList(params) { return get(prefix + "me/pageList",params, REQUEST_BASE_URL) },

}
