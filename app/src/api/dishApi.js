import {get} from "./index"

const prefix = "/product/"

const REQUEST_BASE_URL = "ss-product";

export  default {

    // 获取分类下的产品数组
    listByCategoryId(params) {
        return get(prefix + "list/by/category", params, REQUEST_BASE_URL);
    },

}

