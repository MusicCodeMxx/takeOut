package top.starshine.commons.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Random;

/**
 * AES 加解密工具类
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/22  下午 6:33  周三
 * @Description:
 */
public final class AesUtils {

    public static final String CHAR_ENCODING = "UTF-8";
    public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final String AES_TYPE = "AES";

    /**
     * 随机生成 AesUtils Key
     * <li>传入长度至少 16 位</li>
     * @return string
     */
    public static String getAESKey(int length) throws Exception {
        Random random = new Random();
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < length; i++) {
            boolean isChar = (random.nextInt(2) % 2 == 0);// 输出字母还是数字
            if (isChar) { // 字符串
                int choice = (random.nextInt(2) % 2 == 0) ? 65 : 97; // 取得大写字母还是小写字母
                ret.append((char) (choice + random.nextInt(26)));
            } else { // 数字
                ret.append((random.nextInt(10)));
            }
        }
        return ret.toString();
    }

    /**
     * 解或加密字节数组共方法
     *
     * @param data 解密字节数组
     * @param key 密钥字节数组
     * @return 字节数组
     */
    public static byte[] coding(byte[] data, byte[] key, int mode) {
        if (key.length != 16) throw new RuntimeException("Invalid AesUtils key length (must be 16 bytes)");
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            // 初始化
            cipher.init(mode, new SecretKeySpec(new SecretKeySpec(key, AES_TYPE).getEncoded(), AES_TYPE));
            return cipher.doFinal(data); // 加密
        } catch (Exception e) {
            String msg = mode==1?"加密失败":"解密失败";
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * 加密并转换字符串输出
     * @param data 要被加密的数据
     * @param key 密钥
     * @return 字符串
     */
    public static String encryptToBase64(String data, String key){
        if ( null == data || data.isEmpty()){ return ""; }

        try {
            return Base64.getEncoder().encodeToString(
                    coding(data.getBytes(CHAR_ENCODING), key.getBytes(CHAR_ENCODING), Cipher.ENCRYPT_MODE));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 加密并转换字符串输出
     * @param data 要被加密的数据
     * @param key 密钥
     * @return 字符串
     */
    public static <T> String encryptObjectMapperToBase64(T data, String key){
        if ( null == data ){ return ""; }
        try {
            return encryptToBase64(new ObjectMapper().writeValueAsString(data),key);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 解密成字符串
     * @param data 已被加密的字符串
     * @param key 密钥
     * @return 字符串
     */
    public static String decryptFromBase64(String data, String key){
        if (!StringUtils.hasText(data)){ return null; }
        try {
            return new String( coding(Base64.getDecoder().decode(
                    data.trim().getBytes()), key.getBytes(CHAR_ENCODING), Cipher.DECRYPT_MODE), CHAR_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("解密识别", e);
        }
    }

}

