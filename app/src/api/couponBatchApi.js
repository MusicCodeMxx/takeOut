import {get} from "./index"

const prefix = "/couponBatch/"

const REQUEST_BASE_URL = "ss-coupon";

export default {

    // 查询可以领取的优惠券
    list() {
        return get(prefix + "list", null, REQUEST_BASE_URL)
    },

    // 领取指定的优惠券
    receiveCouponById(id) {
        return get(prefix + "receive/" + id, undefined, REQUEST_BASE_URL)
    },

}
