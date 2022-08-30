import {get} from "./index"

const prefix = "/category/"

export default {

    // 获取所有的菜品分类
    list(params) { return get( prefix  + "list", params,"product") },

}