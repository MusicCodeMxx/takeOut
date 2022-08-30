import {del, get, put} from "./index"

const prefix = "/shoppingCart/"

const REQUEST_BASE_URL = "ss-shoppingcart";

export  default {

    // 获取购物车内商品的集合
    list(params) { return get(prefix + "list", params  ,REQUEST_BASE_URL) },

    // 添加商品数量
    add(data) {
        return put(prefix + "add", data, REQUEST_BASE_URL)
    },

    // 减少产品的数量, 若减少至 0 即从购物车中删除该商品
    subtract(data) {
        return put(prefix + "subtract", data, REQUEST_BASE_URL)
    },

    // 删除购物车的商品
    clear() { return del( prefix  + "clear",undefined,REQUEST_BASE_URL) },


}