import {get} from "./index"

const prefix = "/setmeal/"

export default {

    // 获取菜品分类对应的套餐
    list(params) { return get(prefix + "list", { params }) },

    // 获取套餐的全部菜品
    setmealDishDetails(id) { return get(prefix + "dish/" + id ) },

}