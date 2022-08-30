package top.starshine.commons.util;

import java.util.Random;

/**
 * @author: starshine
 * @version: 1.0
 * @since: 2022/4/11  下午 3:50  周一
 * @Description: 随机数
 */
public class RandomNumberUtils {


    /**
     * 随机生成指定的长度
     *
     * @return  long 类型数字
     */
    public static long randomLengthOfLong(int length){
        return Long.parseLong(randomLengthOfString(length));
    }

    /**
     * 随机生成指定的长度
     *
     * @return String 类型数字
     */
    public static String randomLengthOfString(int length){
        return randomNumber(length).toString();
    }

    /**
     *  数字的长度
     * @param length 生成的长度
     * @return 对象
     */
    public static StringBuilder randomNumber(int length){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++){
            int temp = random.nextInt(10);
            // 如果首位是 0 那么默认是 1;
            if( i == 0 && temp == 0 ){ sb.append(1);  }
            sb.append(temp);
        }
        return sb;
    }


    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code =null;
        if(length == 4){
            code = new Random().nextInt(9999);//生成随机数，最大为9999
            if(code < 1000){
                code = code + 1000;//保证随机数为4位数字
            }
        }else if(length == 6){
            code = new Random().nextInt(999999);//生成随机数，最大为999999
            if(code < 100000){
                code = code + 100000;//保证随机数为6位数字
            }
        }else{
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);
        return capstr;
    }



}
