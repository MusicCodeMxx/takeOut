import {get} from "./index"

const prefix = "/page/"

const REQUEST_BASE_URL = "ss-product";

export  default {

    // 获取首页信息
    list() {
        return get(prefix + "home", null, REQUEST_BASE_URL)
    },

}

