package top.starshine.commons.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * <h3>金额计算工具</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/12  下午 5:23  周二
 * @Description:
 */
public final class CurrencyUtil {

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 2;

    /**100.00 浮点精确计算的对象 */
    private static final BigDecimal BIG_ONE_HUNDRED = new BigDecimal("100.00");

    /**
     * 这个类不能实例化
     */
    private CurrencyUtil() {}

    /**
     * 提供精确的加法运算。
     * @param v1 {@link Double} 被加的数
     * @param v2 {@link Double} 加数
     * @return {@link Double} 累加之和
     */
    public static Double add(Double v1, Double v2) {
        return new BigDecimal(v1).add(new BigDecimal(v2)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }


    /**
     * 提供精确的加法运算。
     * @param params {@link double}
     * @return {@link Double} 累加之和
     */
    public static Double add(double... params) {
        BigDecimal result = new BigDecimal("0");
        for (double param : params) {
            BigDecimal bigParam = BigDecimal.valueOf(param);
            result = result.add(bigParam).setScale(2, RoundingMode.HALF_UP);
        }
        return result.doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * @param params {@link double} 第一个参数为被减数，其余数字为减数
     * @return {@link Double}
     */
    public static Double sub(double... params) {
        BigDecimal result = BigDecimal.valueOf(params[0]);
        // 获取剩余的形参，并排除第一个
        params = Arrays.stream(params).skip(1).toArray();
        // 开始循环
        for (double param : params) {
            // 转型精准计算类型
            BigDecimal bigParam = BigDecimal.valueOf(param);
            // 减去,并设置刻度(后面的小数点)
            result = result.subtract(bigParam).setScale(2, RoundingMode.HALF_UP);
        }
        // 转为浮点类型返回
        return result.doubleValue();
    }

    /**
     * 提供精确的减法运算
     * @param v1 {@link double} 被减数
     * @param v2 {@link double} 减数
     * @return {@link double} 操作结果
     */
    public static double subtract(Double v1, Double v2){
        return new BigDecimal(v1).subtract(new BigDecimal(v2)).doubleValue();
    }

    /**
     * <h3>提供精确的乘法运算</h3>
     * @param v1 {@link double} 被乘数
     * @param v2 {@link double} 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * <h3>提供精确的乘法运算</h3>
     * @param v1  {@link double}  被乘数
     * @param v2  {@link double}  乘数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的积
     */
    public static Double mul(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.multiply(b2).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 {@link double} 被除数
     * @param v2 {@link double} 除数
     * @return {@link double} 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1 {@link double} 被除数
     * @param v2 {@link double} 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return {@link Double} 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) throw new IllegalArgumentException("The scale must be a positive integer or zero");
        //如果被除数等于0，则返回0
        if (v2 == 0)  return 0;
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 金额转分
     *
     * @param money {@link Double}  金额
     * @return {@link Integer} 转换单位为分
     */
    public static Integer fen(Double money) {
        double price = mul(money, 100);
        return (int) price;
    }

    /**
     * 金额转分
     *
     * @param money {@link Double} 金额
     * @return {@link Double} double类型分
     */
    public static double reversalFen(Double money) {
        return div(money, 100);
    }

    /**
     * <h3>v1对比v2是否相等</h3>
     * @param v1 {@link Double} 对比谁
     * @param v2 {@link Double} 被谁对比
     * @return {@link int} 若相同就返回 0, 若不相同就返回 1
     */
    public static int compareTo(Double v1, Double v2){
        return new BigDecimal(v1).compareTo(new BigDecimal(v2));
    }

    /**
     * <h3>v1对比v2是否相等</h3>
     * @param v1 {@link Double} 对比谁
     * @param v2 {@link Double} 被谁对比
     * @return {@link int} 若相同就返回 true, 若不相同就返回 false
     */
    public static boolean isEqual(Double v1, Double v2){
        // 相等就是 0
        // 不相等就 1
        return compareTo(v1,v2) == 0;
    }

    /**
     * <h3>v1对比v2是否不相等</h3>
     * @param v1 {@link Double} 对比谁
     * @param v2 {@link Double} 被谁对比
     * @return {@link boolean} 若相同就返回 false, 若不相同就返回 true
     */
    public static boolean notEqual(Double v1, Double v2){
        // 相等就是 0
        // 不相等就 1
        return compareTo(v1,v2) != 0;
    }

    /**
     * <ul>
     *     <li>转换大十进制处理并除以100</li>
     *     <li>(up)四舍五入模式</li>
     *     <li>参考地址: https://blog.csdn.net/wizardforcel/article/details/107825454</li>
     * </ul>
     * @param value {@link String} 要被转换大十进制处理
     * @return {@link BigDecimal} 转换大十进制对象
     */
    public static BigDecimal fenToBigDecimal(String value){
        return new BigDecimal(value).divide(BIG_ONE_HUNDRED, RoundingMode.UP);
    }

    /**
     * <h3>大于等于比较 >= </h3>
     * <ul>
     *     <li>v1 大于等于 v2</li>
     *     <li>v1 >= v2</li>
     * </ul>
     * @param v1 {@link Double} 对比谁
     * @param v2 {@link Double} 被谁对比
     * @return {@link int} 若大于等于返回 true,若不符合就返回 false
     */
    public static boolean ge(Double v1, Double v2){
        return compareTo(v1,v2) > -1;
    }

    /**
     * <h3>大于比较 > </h3>
     * <ul>
     *     <li>v1 大于 v2</li>
     *     <li>v1 > v2</li>
     * </ul>
     * @param v1 {@link Double} 对比谁
     * @param v2 {@link Double} 被谁对比
     * @return {@link int} 若大于返回 true,若不符合就返回 false
     */
    public static boolean gt(Double v1, Double v2){
        return compareTo(v1,v2) == 1;
    }

    /**
     * <h3>小于等于比较(<=)</h3>
     * <ul>
     *     <li>v1 小于 v2</li>
     *     <li>v1 <= v2</li>
     * </ul>
     * @param v1 {@link Double} 对比谁
     * @param v2 {@link Double} 被谁对比
     * @return {@link int} 若小于返回 true,若不符合就返回 false
     */
    public static boolean le(Double v1, Double v2){
        return compareTo(v1,v2) < 1;
    }

    /**
     * <h3>小于等于比较(<=)</h3>
     * <ul>
     *     <li>v1 小于 v2</li>
     *     <li>v1 <= v2</li>
     * </ul>
     * @param v1 {@link Double} 对比谁
     * @param v2 {@link Double} 被谁对比
     * @return {@link int} 若小于返回 true,若不符合就返回 false
     */
    public static boolean lt(Double v1, Double v2){
        // -1, 0, 1
        return compareTo(v1,v2) <= 0;
    }

    public static void main(String[] args) {
        System.out.println(CurrencyUtil.lt(0.10D, 0.10D));// true
        System.out.println(CurrencyUtil.le(0.01D, 0.10D));// true
        System.out.println(CurrencyUtil.gt(0.1D, 0.10D));// false
        System.out.println(CurrencyUtil.ge(5.0D, 1.0D));// true
    }

}
