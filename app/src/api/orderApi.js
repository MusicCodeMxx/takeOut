import {del, get, put} from "./index"

const prefix = "/order/"

const REQUEST_BASE_URL = "ss-order";

/**
 * 订单 API 接口
 */
export default {

    // 分页列表
    pageList(params) {
        return get(prefix + "pageList", params, REQUEST_BASE_URL)
    },

    // 获取聚合信息
    buildBillData(){ return  get("build/billData", null, REQUEST_BASE_URL) },

    // 查询所有订单
    list() { return get(prefix + "list",undefined, REQUEST_BASE_URL) },

    // 分页订单列表
    listPage(params) { return get(prefix + "listPage", params, REQUEST_BASE_URL) },

    // 取消订单
    cancel(data) { return put(prefix + "cancel/by/" + data, null, REQUEST_BASE_URL) },

    // 删除订单
    delete(data) {
        return del(prefix + "delete/by/" + data, null, REQUEST_BASE_URL)
    },

    // 退货退款
    returnApply(data) { return del(prefix + "returnApply", data, REQUEST_BASE_URL) },

}
