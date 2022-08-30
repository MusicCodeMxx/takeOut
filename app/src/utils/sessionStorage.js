/*-----------------会话存储---------------------*/
const STORAGE = window.sessionStorage;

export default {

    /**
     * 设置
     * @param key 键
     * @param value 值
     */
    set(key, value) {
        var setting = arguments[0];
        if (Object.prototype.toString.call(setting).slice(8, -1) === 'Object'){
            for(var i in setting){
                STORAGE.setItem(i, JSON.stringify(setting[i]))
            }
        }else{
            STORAGE.setItem(key, JSON.stringify(value))
        }

    },

    /**
     * 获取
     * @param key 键
     * @returns {null|any} 值
     */
    get(key) {
        if (key) return JSON.parse(STORAGE.getItem(key))
        return null;

    },

    /**
     * 移除
     * @param key 键
     */
    remove(key) {
        STORAGE.removeItem(key)
    },

    /**
     * 移除所有
     */
    clear() {
        STORAGE.clear()
    }

}