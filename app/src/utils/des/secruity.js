var CryptoJS = require("./crypto-js");
import server from "../../common/system";

/**
 * 加密
 * @param data 加密内容
 * @param AES_KEY 密钥
 * @returns {string} 返回结果
 */
export function encryptByDES(data, AES_KEY = server.secruityKey) {
    var keyHex = CryptoJS.enc.Utf8.parse(AES_KEY);
    var encrypted = CryptoJS.DES.encrypt(data, keyHex, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7});
    return encrypted.toString();
}

/**
 * 加密
 * @param data 要加密解密的数据
 * @param AES_KEY 密钥
 * @returns {string}
 */
export function encryptByAES(data, AES_KEY = server.secruityKey) {
    const key = CryptoJS.enc.Utf8.parse(AES_KEY);
    const encrypted = CryptoJS.AES.encrypt(data, key, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 });
    return encrypted.toString();
}

/**
 * 解密
 * @param data 加密内容
 * @param AES_KEY 密钥
 * @returns {*} 返回结果
 */
export function decryptByDES(data, AES_KEY = server.secruityKey) {
    var keyHex = CryptoJS.enc.Utf8.parse(AES_KEY);
    var decrypted = CryptoJS.DES.decrypt(
        {data: CryptoJS.enc.Base64.parse(data)},
        keyHex,
        {mode: CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7}
    );
    return decrypted.toString(CryptoJS.enc.Utf8);
}

/**
 * 解密
 * @param data 要加密解密的数据
 * @param AES_KEY 密钥
 * @returns {*}
 */
export function decryptByAES(ciphertext, AES_KEY = server.secruityKey){
    const key = CryptoJS.enc.Utf8.parse(AES_KEY);
    const decrypt = CryptoJS.AES.decrypt(
        { ciphertext: CryptoJS.enc.Base64.parse(ciphertext) },
        key,
        { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7,}
    );
    console.log("decrypt=>>>>>>>>",decrypt)
    return decrypt.toString(CryptoJS.enc.Utf8);
}
