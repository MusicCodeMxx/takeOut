import {del, get, post, put} from "./index"

const prefix = "/addressBook/"

const REQUEST_BASE_URL = "ss-user";

export default {

    // 获取所有地址
    list() {
        return get(prefix + "list", undefined, REQUEST_BASE_URL)
    },

    //新增地址
    add(body) {
        return post(prefix + "add", body, REQUEST_BASE_URL)
    },

    //修改地址
    update(body) {
        return put(prefix + "update", body, REQUEST_BASE_URL)
    },

    // 删除地址
    delete(body) {
        return del(prefix + "delete/by/" + body, null, REQUEST_BASE_URL)
    },

    // 查询单个地址
    find(id) {
        return get(prefix + "find/by/" + id, undefined, REQUEST_BASE_URL)
    },

    // 重置并修改成当前这设置默认地址
    reviseDefault(data) {
        return put(prefix + "reviseDefault/by/" + data, null, REQUEST_BASE_URL)
    },

    // 获取默认地址
    getDefault() { return get(prefix + "default",undefined ,REQUEST_BASE_URL) },

}