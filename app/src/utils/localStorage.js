/*-----------------本地存储---------------------*/
const STORAGE = window.localStorage;

const AES_KEY = "starshine-topkey"

import {decryptByAES, encryptByAES} from "./des/secruity";


export default {

    /**
     * 设置
     * @param key 键
     * @param value 值
     */
    set(key, value) {
        if (key && value) {
            let setting = arguments[0];
            if (Object.prototype.toString.call(setting).slice(8, -1) === 'Object') {
                for (let i in setting) {
                    STORAGE.setItem(i, JSON.stringify(setting[i]))
                }
            } else {
                STORAGE.setItem(key, JSON.stringify(value))
            }
        }
    },

    /**
     * 设置加密版本
     * @param key 键
     * @param value 值
     */
    setByAES(key, value) {
        if (key && value){
            let setting = arguments[0];
            if (Object.prototype.toString.call(setting).slice(8, -1) === 'Object'){
                for(let i in setting){
                    STORAGE.setItem(i, encryptByAES(JSON.stringify(setting[i]),AES_KEY))
                }
            }else{
                STORAGE.setItem(key,encryptByAES(JSON.stringify(value),AES_KEY))
            }
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
     * 获取
     * @param key 键
     * @returns {null|any} 值
     */
    getByAES(key) {
        if (key){
            var item = STORAGE.getItem(key);
            let json = decryptByAES(item,AES_KEY);
            if (json) return JSON.parse(json);
        }
        return null;
    },




    /**
     * 移除
     * @param key 值
     */
    remove(key) {
        STORAGE.removeItem(key)
    },


    /**
     * 移除所有
     */
    clearAll() {
        STORAGE.clear()
    }

}

