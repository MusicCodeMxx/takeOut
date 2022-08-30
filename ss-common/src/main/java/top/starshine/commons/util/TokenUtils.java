package top.starshine.commons.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author: starshine
 * @version: 1.0
 * @since: 2022/2/22  下午 6:22  周二
 * @Description: 令牌工具类
 */
public final class TokenUtils {

    /** 签发 */
    private static final Algorithm ALGORITHM ;

    /** 分钟 */
    private static final long MINUTE = 60000;

    /** 小时 */
    private static final long HOUR = 60 * MINUTE;

    /** 天 */
    private static final long DAY = 24 * HOUR;

    /** 令牌续期续期结束边界时间(单位分钟) */
    public static final long TOKEN_RENEWAL_MIN = 10L;

    /** 令牌续期续期开始边界时间(单位分钟) */
    public static final long TOKEN_RENEWAL_MAX = 60L;

    /**  token 到期时间 */
    private static final long TOKEN_EXPIRATION_TIME = 7 * DAY;// 7 天

    /** 载荷前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**  请求头的标识 */
    public static final String HEADER_STRING = "Authorization";

    /** 如果 token 过期可以使用这个获取内容 */
    private static final JWT JWT_OBJECT;

    /* 初始化对象 */
    static {
        // 注意这里可以使用配置
        ALGORITHM = Algorithm.HMAC512("Java_to_Dev_Secret");
        JWT_OBJECT = new JWT();
    }

    /**
     * 生成指定过期时间的令牌
     *
     * @param withIssuer  签发人
     * @param payload 载荷
     * @param minutes 设置过期时间(分钟)
     * @return string
     */
    public static String create(String withIssuer,String payload,long minutes){
        return JWT.create()
                /* 签发人 */
                .withIssuer(withIssuer)
                /* 载荷 - id/用户名/昵称 */
                .withSubject(payload)
                /* 设置签发时间 */
                .withIssuedAt(generateCurrentDate())
                /* 设置过期时间 */
                .withExpiresAt(generateExpiraationDate(minutes))
                /* 密钥 */
                .sign(ALGORITHM);
    }

    /** 生成令牌 - 签发人,载荷 */
    public static String create(String withIssuer,String payload){
        // 随机生成 aes 加密 Key 存入
        return JWT.create()
                        /* 签发人 */
                        .withIssuer(withIssuer)
                        /* 载荷 - id/用户名/昵称 */
                        .withSubject(payload)
                        /* 设置签发时间 */
                        .withIssuedAt(generateCurrentDate())
                        /* 设置过期时间 */
                        .withExpiresAt(generateExpiraationDate())
                        /* 密钥 */
                        .sign(ALGORITHM);
    }

    /**
     * 无视是否过期直接获取令牌中的内容
     * @param token 令牌
     * @return 返回令牌的内容
     */
    public static String getSubject(String token){
        try {
            return getDecodedJWT(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 无视是否过期直接获取令牌中过期时间
     *
     * @param token 令牌
     * @return 返回令牌的时间
     */
    //public static String getExpiresAt(String token){
    //    try {
    //        return getDecodedJWT(token).getAlgorithm();
    //    } catch (Exception e) {
    //        return null;
    //    }
    //}

    /**
     * 将 token 写入到响应头中去
     *
     * @param response 响应头
     * @param token 令牌
     */
    public static void addHeaderToken(HttpServletResponse response, String token){
        // addHeader：添加一个新的请求头字段。（一个请求头中允许有重名字段。）
        // setHeader：设置一个请求头字段，有则覆盖，无则添加。
        // https://blog.csdn.net/sunshine_YG/article/details/96882630
        /* 添加到响应头当中 */
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }

    /**
     * 获取 DecodedJWT 对象
     *
     * @param token 令牌
     * @return 对象
     */
    public static DecodedJWT getDecodedJWT(String token){
        return JWT_OBJECT.decodeJwt(token);
    }

    /**
     * 解析请求头中的令牌
     *
     * @param request 请求头
     * @return 响应头
     */
    public static String parseHeader(HttpServletRequest request){
        /* 从请求头中获取 Token */
        String header = request.getHeader(HEADER_STRING);
        /* 检查是否为空 或 获取到的载荷必须要指定的格式开头 */
        return (header == null || !header.startsWith(TOKEN_PREFIX)) ? null : header;
    }

    /** 获取令牌中的载荷,返回是空就是过期了 */
    public static String isHeader(String header) {
        try {
            return parseToken(header);// 如果令牌失效了,出现异常
        } catch (Exception ex) {
            return null;
        }
    }

    /** 获取令牌中的载荷,返回是空就是过期了 */
    public static String getSubject(DecodedJWT decodedJWT) {
        try {
            return decodedJWT.getSubject();// 如果令牌失效了,出现异常
        } catch (Exception ex) {
            return null;
        }
    }


    /**
     * 获取距离过期剩余时间
     * @param token 令牌
     * @return 剩余分钟
     */
    public static long getExpiresAtToMinutes(String token){
        Date expiresAt = getExpiresAt(token);
        if ( null == expiresAt ){
            return 0;
        }
        //方式一
        LocalDateTime localDateTime = expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        //方式二 直接使用 LocalDateTime.ofInstant
        //LocalDateTime localDateTime1 = LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault());
        // getSeconds();// 秒
        return  Duration.between(LocalDateTime.now(),localDateTime).toMinutes();// 分钟

    }


    /**
     * 获取距离过期剩余时间
     * @param decodedJWT 令牌解密对象
     * @return 剩余分钟
     */
    public static long getExpiresAtToMinutes(DecodedJWT decodedJWT){
        Date expiresAt = getExpiresAt(decodedJWT);
        if ( null == expiresAt ){
            return 0;
        }
        //方式一
        LocalDateTime localDateTime = expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        //方式二 直接使用 LocalDateTime.ofInstant
        //LocalDateTime localDateTime1 = LocalDateTime.ofInstant(date.toInstant(),ZoneId.systemDefault());
        // getSeconds();// 秒
        return  Duration.between(LocalDateTime.now(),localDateTime).toMinutes();// 分钟

    }


    /**
     * 获取距离过期剩余时间
     * @param token 令牌
     * @return 时间对象
     */
    public static Date getExpiresAt(String token){
        try {
            return checkToken(token).getExpiresAt();// 签发时间
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * 获取距离过期剩余时间
     * @param decodedJWT 令牌解密对象
     * @return 时间对象
     */
    public static Date getExpiresAt(DecodedJWT decodedJWT){
        try {
            return decodedJWT.getExpiresAt();// 签发时间
        } catch (RuntimeException e) {
            return null;
        }
    }


    /** 获取令牌中的载荷 */
    public static String parseToken(String header) throws RuntimeException {
        return checkToken(header.replace(TOKEN_PREFIX, "")).getSubject();
    }

    /**
     * 移除前缀然后解析令牌返回解密对象
     * @param header 令牌
     * @return 解密对象
     * @throws RuntimeException 运行异常
     */
    public static DecodedJWT parseTokenRemovePrefix(String header) throws RuntimeException {
        try {
            return checkToken(header.replace(TOKEN_PREFIX, ""));
        } catch (RuntimeException e) {
            return null;
        }
    }

    /** 验证是否令牌过期 */
    public static boolean isExpired(String token) {
        try {
            checkToken(token);// 如果令牌失效了,出现异常
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /** 校验令牌 */
    public static DecodedJWT checkToken(String token) throws RuntimeException {
        return JWT.require(ALGORITHM).build().verify(token);
    }

    /** 获取令牌的签发时间 */
    public static Date getCreateTime(String token){
        // 签发时间存入时间,然后存入缓存,从缓存中对比当前 token
        try {
            return checkToken(token).getIssuedAt();// 签发时间
        } catch (RuntimeException e) {
            return null;
        }
    }

    /** 当前时间 */
    private static Date generateCurrentDate(){
        return new Date();
    }

    /** 过期时间 */
    private static Date generateExpiraationDate(){
        // 当前时间 + 加上过期时间
        return new Date(DateTime.now().getMillis() + TOKEN_EXPIRATION_TIME);
    }

    /** 过期时间 */
    private static Date generateExpiraationDate(long minutes){
        // 当前时间 + 加上过期时间
        return new Date(DateTime.now().getMillis() + minutes);
    }

}


/*
需要依赖如下包

<!-- JWT生成器 -->
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.18.2</version>
</dependency>

<!--日期和时间库-->
<dependency>
    <groupId>joda-time</groupId>
    <artifactId>joda-time</artifactId>
    <version>2.10.8</version>
</dependency>

 */