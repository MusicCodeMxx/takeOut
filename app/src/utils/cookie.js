/*-----------------cookie---------------------*/
/* 导出为对象 */
export default {

    /**
     * 设置
     * @param key 键
     * @param value 值
     * @param day 有效天数
     */
    set(key, value, day) {
        let date = new Date();
        date.setDate(date.getDate() + day);
        document.cookie = key + '=' + value + ';expires=' + date;
    },

    /**
     * 获取
     * @param key 键
     * @returns {*} 值
     */
    get(key) {
        const value = document.cookie.match(RegExp(key + '=([^;]+)'));
        return value ? value[1]: undefined;
    },


    /*删除cookie*/
    remove(key) {
        this.set(key, 1, -1);
    }
}